package com.jjimkong_backend.api.service.map.impl;

import com.jjimkong_backend.api.service.map.MapService;
import com.jjimkong_backend.api.service.map.dto.request.PlaceSearchRequest;
import com.jjimkong_backend.api.service.map.dto.response.PlaceSearchResponse;
import com.jjimkong_backend.api.service.map.dto.response.PlaceSearchResponse.NaverLocalSearchResult;
import com.jjimkong_backend.global.response.exception.BadRequestException;
import com.jjimkong_backend.global.response.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MapServiceImpl implements MapService {

    /** 네이버 지역 검색 API 경로 */
    private static final String LOCAL_SEARCH_PATH = "/v1/search/local.json";

    private final RestClient naverSearchRestClient;

    @Override
    public PlaceSearchResponse searchPlaces(PlaceSearchRequest request) {
        NaverLocalSearchResult result = requestLocalSearch(request);
        return PlaceSearchResponse.from(result);
    }

    private NaverLocalSearchResult requestLocalSearch(PlaceSearchRequest request) {
        try {
            return naverSearchRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(LOCAL_SEARCH_PATH)
                            .queryParam("query", request.query())
                            .queryParam("display", request.displayOrDefault())
                            .queryParam("sort", request.sortOrDefault())
                            .build())
                    .retrieve()
                    .body(NaverLocalSearchResult.class);
        } catch (RestClientException e) {
            log.error("네이버 지역 검색 API 호출 실패 - query: {}, message: {}", request.query(), e.getMessage());
            throw new BadRequestException(ExceptionCode.NAVER_SEARCH_API_ERROR);
        }
    }
}
