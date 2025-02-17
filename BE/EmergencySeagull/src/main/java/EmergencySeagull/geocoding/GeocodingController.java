package EmergencySeagull.geocoding;

import EmergencySeagull.common.utils.GoogleGeocodingUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/geocoding")
public class GeocodingController {

    @GetMapping
    public ResponseEntity<String> getAddress(@RequestParam Double latitude,
        @RequestParam Double longitude) {
        String address = GoogleGeocodingUtils.getAddressFromCoordinates(latitude, longitude);
        return ResponseEntity.ok(address);
    }
}
