import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class LocationService {

    @Value("${kakao.api.key}")
    private String kakaoApiKey; // 카카오 REST API 키

    private final RestTemplate restTemplate;

    public LocationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getLocationName(double latitude, double longitude) {
        String url = "https://dapi.kakao.com/v2/local/geo/coord2address.json";

        // 좌표를 주소로 변환하기 위한 URL 생성
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("x", longitude)  // 경도
                .queryParam("y", latitude);  // 위도

        // HTTP 헤더 설정 (Authorization에 카카오 API 키를 넣음)
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "KakaoAK " + kakaoApiKey);

        // 카카오 API 호출
        String response = restTemplate.getForObject(
                uriBuilder.toUriString(),
                String.class,
                headers
        );

        // 응답에서 지역명을 추출하여 반환
        return extractAddressFromResponse(response);
    }

    private String extractAddressFromResponse(String response) {
        // JSON 파싱 로직 (Gson 또는 Jackson을 사용 가능)
        // 예제에서는 단순한 문자열 파싱 로직을 사용합니다.

        if (response.contains("address_name")) {
            int startIndex = response.indexOf("address_name") + 15;
            int endIndex = response.indexOf("\"", startIndex);
            return response.substring(startIndex, endIndex);
        } else {
            return "Address not found";
        }
    }
}
