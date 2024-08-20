package nicehcy2.map_api_test.map.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LocationService {

    private final RestTemplate restTemplate;

    @Value("${kakao.api.key}")  // Value 어노테이션으로 주입
    private String kakaoApiKey;

    public String getAddressFromCoordinates(double latitude, double longitude) {
        String url = buildRequestUrl(latitude, longitude);
        HttpHeaders headers = createHttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            return extractAddressFromResponse(response).orElse("Address not found");
        } catch (Exception e) {
            return "Address not found";
        }
    }

    // URL 생성 메소드
    private String buildRequestUrl(double latitude, double longitude) {
        return String.format("https://dapi.kakao.com/v2/local/geo/coord2address.json?x=%s&y=%s", longitude, latitude);
    }

    // HTTP 헤더 생성 메소드
    private HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);
        return headers;
    }

    // API 응답에서 주소 추출 메소드
    private Optional<String> extractAddressFromResponse(ResponseEntity<Map> response) {
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> responseBody = response.getBody();
            List<Map<String, Object>> documents = (List<Map<String, Object>>) responseBody.get("documents");

            if (!documents.isEmpty()) {
                Map<String, Object> addressData = (Map<String, Object>) documents.get(0).get("address");
                return Optional.ofNullable((String) addressData.get("address_name"));
            }
        }
        return Optional.empty();
    }
}
