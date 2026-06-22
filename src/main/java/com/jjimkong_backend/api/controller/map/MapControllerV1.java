package com.jjimkong_backend.api.controller.map;

import com.jjimkong_backend.api.service.map.MapService;
import com.jjimkong_backend.api.service.map.dto.request.PlaceSearchRequest;
import com.jjimkong_backend.api.service.map.dto.response.PlaceSearchResponse;
import com.jjimkong_backend.global.config.oauth2.CustomOAuth2User;
import com.jjimkong_backend.global.response.api.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/maps")
@RequiredArgsConstructor
@Slf4j
public class MapControllerV1 {

    private final MapService mapService;

    /**
     * ✅ 장소(맛집) 키워드 검색
     * GET /api/v1/maps/places
     */
    @GetMapping("/places")
    public ApiResponse<PlaceSearchResponse> searchPlaces(
            @AuthenticationPrincipal CustomOAuth2User customOAuth2User,
            @Valid @ModelAttribute PlaceSearchRequest request) {
        log.info("MapControllerV1.searchPlaces - userId: {}, query: {}",
                customOAuth2User.getUser().getId(), request.query());

        PlaceSearchResponse response = mapService.searchPlaces(request);
        return ApiResponse.ok(response);
    }
}
