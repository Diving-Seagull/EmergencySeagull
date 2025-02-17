package EmergencySeagull.common.utils;

import static EmergencySeagull.common.exception.ExceptionCode.ADDRESS_NOT_FOUND;
import static EmergencySeagull.common.exception.ExceptionCode.GEOCODING_API_ERROR;

import EmergencySeagull.common.exception.CustomException;
import EmergencySeagull.common.utils.dto.GoogleGeocodingResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GoogleGeocodingUtils {

    private static RestTemplate restTemplate;
    private static String apiKey;
    private static final String API_URL = "https://maps.googleapis.com/maps/api/geocode/json";

    private GoogleGeocodingUtils() {
        // private 생성자로 인스턴스화 방지
    }

    @Autowired
    public void setDependencies(RestTemplate restTemplate,
        @Value("${google.api.key}") String apiKey) {
        GoogleGeocodingUtils.restTemplate = restTemplate;
        GoogleGeocodingUtils.apiKey = apiKey;
    }

    public static String getAddressFromCoordinates(Double latitude, Double longitude) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(API_URL)
            .queryParam("latlng", latitude + "," + longitude)
            .queryParam("key", apiKey)
            .queryParam("language", "ko");

        String url = builder.toUriString();

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            ObjectMapper mapper = new ObjectMapper();
            GoogleGeocodingResponseDto responseDto = mapper.readValue(response.getBody(),
                GoogleGeocodingResponseDto.class);

            if (responseDto != null && responseDto.getResults() != null && !responseDto.getResults()
                .isEmpty()) {
                return responseDto.getResults().get(0).getFormatted_address();
            } else {
                throw new CustomException(ADDRESS_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new CustomException(GEOCODING_API_ERROR);
        }
    }
}