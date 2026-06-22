package com.jjimkong_backend.api.service.map.dto.response;

import java.util.List;

/**
 * 장소(맛집) 키워드 검색 응답.
 * <p>
 * 클라이언트에 노출하는 응답({@link PlaceSearchResponse}, {@link PlaceItem})과
 * 네이버 지역 검색 API의 원본 응답({@link NaverLocalSearchResult})을 한곳에 모아 둔다.
 * 원본 응답은 내부 변환용이며 컨트롤러 밖으로 직접 노출하지 않는다.
 *
 * @param total   전체 검색 결과 수
 * @param display 실제 반환된 결과 수
 * @param items   장소 목록
 */
public record PlaceSearchResponse(
        int total,
        int display,
        List<PlaceItem> items
) {

    public static PlaceSearchResponse from(NaverLocalSearchResult result) {
        List<PlaceItem> items = (result.items() == null)
                ? List.of()
                : result.items().stream().map(PlaceItem::from).toList();
        return new PlaceSearchResponse(result.total(), result.display(), items);
    }

    /**
     * 클라이언트에 노출하는 개별 장소 정보.
     *
     * @param title       장소명 (HTML 태그 제거됨)
     * @param category    업종 분류
     * @param description 설명
     * @param telephone   전화번호
     * @param address     지번 주소
     * @param roadAddress 도로명 주소
     * @param longitude   경도(WGS84)
     * @param latitude    위도(WGS84)
     */
    public record PlaceItem(
            String title,
            String category,
            String description,
            String telephone,
            String address,
            String roadAddress,
            Double longitude,
            Double latitude
    ) {

        // 네이버 지역 검색의 mapx/mapy는 WGS84 좌표에 10^7을 곱한 정수 문자열로 내려온다.
        private static final double COORDINATE_DIVISOR = 1e7;

        public static PlaceItem from(NaverLocalSearchResult.Item item) {
            return new PlaceItem(
                    removeHtmlTags(item.title()),
                    item.category(),
                    removeHtmlTags(item.description()),
                    item.telephone(),
                    item.address(),
                    item.roadAddress(),
                    parseCoordinate(item.mapx()),
                    parseCoordinate(item.mapy())
            );
        }

        private static String removeHtmlTags(String value) {
            return value == null ? null : value.replaceAll("<[^>]+>", "");
        }

        private static Double parseCoordinate(String value) {
            if (value == null || value.isBlank()) {
                return null;
            }
            try {
                return Long.parseLong(value.trim()) / COORDINATE_DIVISOR;
            } catch (NumberFormatException e) {
                return null;
            }
        }
    }

    /**
     * 네이버 지역 검색 API의 원본 응답(raw)을 매핑하는 내부 DTO.
     * <p>
     * Jackson이 알 수 없는 필드(lastBuildDate 등)는 무시한다(Spring Boot 기본 설정).
     */
    public record NaverLocalSearchResult(
            int total,
            int start,
            int display,
            List<Item> items
    ) {

        public record Item(
                String title,
                String link,
                String category,
                String description,
                String telephone,
                String address,
                String roadAddress,
                String mapx,
                String mapy
        ) {
        }
    }
}
