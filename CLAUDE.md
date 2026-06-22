# CLAUDE.md

JjimKong 백엔드. 소셜 로그인(OAuth2) 기반으로 사용자가 맛집(Post)을 저장·리뷰하고 그룹으로 공유하는 서비스의 Spring Boot REST API 서버.

## 기술 스택

- **Java 17**, **Spring Boot 3.5.6** (Gradle, `build.gradle`)
- **Spring Data JPA** + Hibernate, DB는 MySQL(운영) / H2(로컬·테스트)
- **Spring Security + OAuth2 Client** — Google / Kakao / Naver 소셜 로그인
- **JWT** (`com.auth0:java-jwt`) — AccessToken / RefreshToken
- **Redis** (`spring-boot-starter-data-redis`) — RefreshToken 등 토큰 저장
- **SpringDoc OpenAPI (Swagger)** — `/swagger-ui.html`
- **Lombok**, Bean Validation

## 빌드 & 실행

PowerShell 환경(Windows) 기준:

```powershell
.\gradlew.bat build          # 빌드
.\gradlew.bat test           # 전체 테스트
.\gradlew.bat bootRun        # 로컬 실행
.\gradlew.bat test --tests "com.jjimkong_backend.SomeTest"   # 단일 테스트
```

> `src/main/resources`는 `.gitignore`에 포함되어 추적되지 않는다. `application.yml`(DB·OAuth·Redis·JWT 시크릿)은 로컬에만 존재하므로 새 환경에서는 직접 준비해야 한다. `processResources { expand(project.properties) }` 설정이 있어 리소스 내 `${...}` 플레이스홀더가 Gradle 프로퍼티로 치환된다.

## 패키지 구조

기능을 **레이어 우선**으로 나누고, 그 안에서 **도메인별**로 다시 나눈다.

```
com.jjimkong_backend
├── api
│   ├── controller/<도메인>/XxxControllerV1.java   # REST 엔드포인트
│   └── service/<도메인>/
│       ├── XxxService.java                        # 인터페이스
│       ├── impl/XxxServiceImpl.java               # 구현체
│       └── dto/request|response/                  # 요청·응답 DTO (record)
├── domain/<도메인>/
│   ├── entity/                                    # JPA 엔티티, Enum
│   └── repository/                                # Spring Data JPA 레포지토리
└── global
    ├── config/                                    # security, jwt, oauth2, redis, cookie, cors
    └── response/
        ├── api/ApiResponse.java                   # 공통 성공 응답
        └── exception/                             # 예외 처리 일체
```

도메인: `users`, `posts`, `reviews`, `groups`, `favorites`. (DTO는 `domain`이 아니라 `api.service.<도메인>.dto` 아래에 둔다.)

## 핵심 규약 (새 기능 추가 시 따를 것)

### 1. 레이어 흐름
`Controller → Service(interface) → ServiceImpl → Repository`. 컨트롤러는 검증·위임만, 비즈니스 로직은 ServiceImpl에 둔다. 서비스는 항상 **인터페이스 + `impl` 구현체** 쌍으로 만든다.

### 2. 컨트롤러
- 클래스명은 `XxxControllerV1`, 매핑은 `/api/v1/<복수형>` (예: `/api/v1/posts`).
- `@RestController @RequiredArgsConstructor @RequestMapping(...) @Slf4j`.
- 인증된 사용자는 `@AuthenticationPrincipal CustomOAuth2User customOAuth2User`로 받고 `customOAuth2User.getUser()`로 `User` 획득.
- 메서드 진입 시 `log.info("XxxController.method - userId: {}", ...)` 로깅.
- 반환 타입은 항상 `ApiResponse<T>`. 각 메서드 위에 한 줄 설명 + HTTP 메서드/경로 Javadoc 주석을 단다.

### 3. 응답 — `ApiResponse<T>`
직접 `new` 하지 말고 정적 팩토리만 사용:
- 조회·수정 성공: `ApiResponse.ok(data)`
- 생성: `ApiResponse.created(data)`
- 본문 없는 삭제 등: `ApiResponse.noContent()`

### 4. DTO
- `request` / `response` 모두 **Java `record`**.
- 요청 DTO는 엔티티 변환 메서드 `toEntity(...)`를 가진다.
- 응답 DTO는 정적 팩토리 `from(Entity)`로 만든다. 엔티티를 컨트롤러 밖으로 직접 노출하지 않는다.

### 5. 엔티티
- `BaseEntity` 상속 → `createdAt`, `updatedAt`(Hibernate `@CreationTimestamp`/`@UpdateTimestamp`), `status`(`Status` Enum, 기본 `ACTIVE`) 자동 포함.
- `@NoArgsConstructor(access = AccessLevel.PROTECTED)` + 필요한 필드만 받는 명시적 생성자(또는 `@Builder`). Setter 금지.
- 값 변경은 의미 있는 메서드로 노출: `update(...)`, `delete()`(소프트 삭제 — `status = DELETED`) 등.
- 테이블·컬럼명은 `@Table(name=...)`, `@Column(name=...)`로 스네이크 케이스 명시. PK는 `IDENTITY` 전략에 `<도메인>_id`.
- Enum 필드는 `@Enumerated(EnumType.STRING)`. 연관관계는 `@ManyToOne(fetch = FetchType.LAZY)` 기본.
- 소프트 삭제 필터: `User`에 `@FilterDef/@Filter("activeFilter")`가 있고 `UserRepositoryImpl.enableActiveFilter()`로 활성화한다.

### 6. 레포지토리
- 기본은 `JpaRepository<Entity, Long>` 확장, 쿼리 메서드(`findByUser` 등) 활용.
- 커스텀 동작이 필요하면 `XxxRepositoryCustom` 인터페이스 + `impl/XxxRepositoryImpl`(EntityManager 직접 사용) 패턴.

### 7. 예외 처리
- 비즈니스 예외는 `throw new BadRequestException(ExceptionCode.XXX)` 형태. (`CustomException` 하위: `BadRequestException`, `AuthenticationException`, `RedisException`)
- 새 에러는 **반드시 `ExceptionCode` Enum에 추가** — `(code, HttpStatus, 한글 메시지)`. 코드 규칙 예: `E_PST_001`(Post 관련). 메시지는 한국어.
- 전역 처리는 `GlobalExceptionHandler`(`@RestControllerAdvice`)가 담당. 컨트롤러에서 try-catch 하지 않는다.
- 권한 검증 패턴: 소유자 비교 후 불일치 시 `NO_PERMISSION_TO_*` 예외 (예: `PostServiceImpl.updatePost`).

### 8. 트랜잭션
- `ServiceImpl`에 클래스 레벨 `@Transactional(readOnly = true)`, 쓰기 메서드에만 `@Transactional` 오버라이드.

### 9. 인증/보안
- 세션 STATELESS, OAuth2 로그인 성공 시 `OAuth2LoginSuccessHandler`에서 JWT 발급.
- `JwtAuthFilter`가 `UsernamePasswordAuthenticationFilter` 앞에서 토큰 검증.
- 공개 엔드포인트는 `SecurityConfig`의 `authorizeHttpRequests`에 추가(예: swagger, reissue). 그 외 기본 `authenticated()`.
- 신규 OAuth2 공급자는 `global/config/oauth2/userinfo/`에 `OAuth2UserInfo` 구현 추가.

## 컨벤션 요약

- 패키지·클래스는 영어, 로그·예외 메시지·주석은 한국어 혼용(기존 스타일 유지).
- 커밋 메시지: `feat : ...`, `feat: ...` 등 한국어 설명 (기존 히스토리 참고). 이슈/PR 템플릿은 `.github/` 참고.
- 새 기능은 위 5개 레이어(controller / service / impl / dto / entity·repository)를 모두 채우고, 응답은 `ApiResponse`, 에러는 `ExceptionCode`로 일관되게 처리할 것.
