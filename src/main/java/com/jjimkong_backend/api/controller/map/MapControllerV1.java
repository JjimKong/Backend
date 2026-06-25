package com.jjimkong_backend.api.controller.map;

import com.jjimkong_backend.api.service.map.MapService;
import com.jjimkong_backend.api.service.map.dto.request.PlaceSearchRequest;
import com.jjimkong_backend.api.service.map.dto.response.PlaceSearchResponse;
import com.jjimkong_backend.global.response.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Map", description = "네이버 지도/장소 검색 API")
@RestController
@RequestMapping("/api/v1/maps")
@RequiredArgsConstructor
@Slf4j
public class MapControllerV1 {

    private final MapService mapService;

    /**
     * ✅ 장소(맛집) 키워드 검색 (공개 API)
     * GET /api/v1/maps/places
     */
    @Operation(
            summary = "장소(맛집) 키워드 검색",
            description = """
                    네이버 지역 검색을 이용해 키워드로 장소(맛집)를 검색한다.

                    **인증 불필요** — 토큰 없이 호출할 수 있는 공개 API다.

                    - `query`(필수): 검색어. 예) `강남 초밥`
                    - `display`(선택, 1~5, 기본 5): 결과 개수
                    - `sort`(선택, 기본 `random`): `random`(정확도순) | `comment`(리뷰 많은 순)

                    응답의 `longitude`/`latitude` 는 네이버 `mapx`/`mapy` 를 WGS84 경·위도로 변환한 값이다.
                    """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "검색 성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            {
                              "code": "SUC",
                              "status": "OK",
                              "message": "SUCCESS",
                              "data": {
                                "total": 5,
                                "display": 2,
                                "items": [
                                  {
                                    "title": "스시 오마카세 강남점",
                                    "category": "일식>초밥,롤",
                                    "description": "",
                                    "telephone": "",
                                    "address": "서울특별시 강남구 역삼동 123-45",
                                    "roadAddress": "서울특별시 강남구 테헤란로 1",
                                    "longitude": 127.0276368,
                                    "latitude": 37.4979517
                                  },
                                  {
                                    "title": "라멘집 강남본점",
                                    "category": "일식>라멘",
                                    "description": "",
                                    "telephone": "02-123-4567",
                                    "address": "서울특별시 강남구 역삼동 678-90",
                                    "roadAddress": "서울특별시 강남구 강남대로 100",
                                    "longitude": 127.0285310,
                                    "latitude": 37.4965200
                                  }
                                ]
                              }
                            }
                            """)
            )
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "502",
            description = "네이버 검색 API 호출 실패",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
                            { "code": "E_MAP_001", "status": "BAD_GATEWAY", "message": "네이버 검색 API 호출 중 오류가 발생했습니다." }
                            """)
            )
    )
    @GetMapping("/places")
    public ApiResponse<PlaceSearchResponse> searchPlaces(
            @Valid @ParameterObject PlaceSearchRequest request) {
        log.info("MapControllerV1.searchPlaces - query: {}", request.query());

        PlaceSearchResponse response = mapService.searchPlaces(request);
        return ApiResponse.ok(response);
    }
}
