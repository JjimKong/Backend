# AWS EC2 + Docker 배포 가이드

`main` 브랜치에 push되면 GitHub Actions가 이미지를 빌드해 **ECR**에 올리고, **EC2**에 SSH로 접속해 컨테이너를 재기동한다.

```
push(main) ─▶ GitHub Actions
                ├─ docker build → ECR push (deploy.yml)
                └─ SSH → EC2: docker compose pull && up -d
```

## 1. AWS 사전 준비 (1회)

1. **ECR 리포지토리 생성**
   ```bash
   aws ecr create-repository --repository-name jjimkong-backend --region ap-northeast-2
   ```
2. **EC2 인스턴스** (Amazon Linux 2023 등) 준비 후:
   - Docker / docker compose 설치, AWS CLI 설치
   - **인스턴스 IAM 역할**에 `AmazonEC2ContainerRegistryReadOnly` 부여 (ECR pull용 — 키 없이 동작)
   - 보안그룹: 8080(또는 80/443) 인바운드, 22(SSH) 허용
   - `~/app/` 에 `docker-compose.yml` 과 `.env` 배치 (`.env`는 `.env.example` 복사 후 채움)
3. **RDS for MySQL** 생성 (MySQL은 EC2 컨테이너로 띄우지 않고 RDS 사용)
   - EC2와 **같은 리전/VPC(ap-northeast-2)** 에 생성해 지연을 최소화
   - **RDS 보안그룹 인바운드 3306**을 **EC2의 보안그룹**에서만 허용(소스에 EC2 SG 지정)
   - 퍼블릭 액세스는 끄고 VPC 내부 통신만 사용 권장
   - 엔드포인트/계정/비밀번호를 `.env`의 `SPRING_DATASOURCE_*` 에 입력
   - (Redis는 compose 컨테이너로 운영. 필요 시 ElastiCache로 이전)
   ```bash
   aws rds create-db-instance \
     --db-instance-identifier jjimkong-mysql \
     --db-instance-class db.t4g.micro \
     --engine mysql --engine-version 8.0 \
     --allocated-storage 20 \
     --db-name jjimkong \
     --master-username admin --master-user-password '<password>' \
     --vpc-security-group-ids <rds-sg-id> \
     --no-publicly-accessible \
     --region ap-northeast-2
   ```
4. **OAuth redirect URI** — Google/Kakao/Naver 콘솔에 배포 도메인 콜백 URL 추가

## 2. GitHub Secrets 등록

`Settings → Secrets and variables → Actions` 에 등록:

| Secret | 설명 |
|---|---|
| `AWS_ACCESS_KEY_ID` / `AWS_SECRET_ACCESS_KEY` | ECR push 권한을 가진 IAM 사용자 키 |
| `AWS_REGION` | 예: `ap-northeast-2` |
| `ECR_REPOSITORY` | 예: `jjimkong-backend` |
| `EC2_HOST` | EC2 퍼블릭 IP/도메인 |
| `EC2_USER` | 예: `ec2-user` |
| `EC2_SSH_KEY` | EC2 접속용 개인키(.pem) 전체 내용 |

> push용 IAM 키 대신 GitHub OIDC + IAM Role 방식으로 바꾸면 장기 키를 없앨 수 있다(권장).

## 3. 설정값 주입 원칙

- `src/main/resources`(application.yml, 시크릿 포함)는 git에 올리지 않는다.
- 운영 설정은 전부 **EC2의 `~/app/.env`** 로 주입한다. → [`.env.example`](../.env.example) 참고
- 환경변수명은 `application.yml(prod)`의 `${...}` 플레이스홀더와 **정확히 일치**해야 한다.

## 4. CI 테스트 주의

`src/main/resources`가 gitignore되어 CI 체크아웃에 없으므로, `./gradlew build`의 테스트가
설정을 못 찾아 실패할 수 있다. **`src/test/resources/application.yml`** (시크릿 없는 H2/더미 OAuth 설정)을
커밋해 두면 해결된다. (test 리소스는 gitignore 대상이 아님)
