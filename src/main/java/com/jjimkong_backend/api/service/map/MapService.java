package com.jjimkong_backend.api.service.map;

import com.jjimkong_backend.api.service.map.dto.request.PlaceSearchRequest;
import com.jjimkong_backend.api.service.map.dto.response.PlaceSearchResponse;

public interface MapService {

    /**
     * 네이버 지역 검색 API로 장소(맛집)를 키워드 검색한다.
     */
    PlaceSearchResponse searchPlaces(PlaceSearchRequest request);
}
