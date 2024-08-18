package nicehcy2.map_api_test.map.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class LocationService {

    private final RestTemplate restTemplate;
    private final String kakaoApiKey;

    @Autowired
    public LocationService(RestTemplateBuilder restTemplateBuilder, @Value("${kakao.api.key}") String kakaoApiKey) {
        this.restTemplate = restTemplateBuilder.build();
        this.kakaoApiKey = kakaoApiKey;
    }

    public String getAddressFromCoordinates(double latitude, double longitude) {
        String url = String.format("https://dapi.kakao.com/v2/local/geo/coord2address.json?x=%s&y=%s", longitude, latitude);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoApiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseBody = response.getBody();
                if (responseBody != null && responseBody.containsKey("documents")) {
                    List<Map<String, Object>> documents = (List<Map<String, Object>>) responseBody.get("documents");
                    if (!documents.isEmpty()) {
                        Map<String, Object> addressData = (Map<String, Object>) documents.get(0).get("address");
                        return (String) addressData.get("address_name");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Address not found";
    }
}
