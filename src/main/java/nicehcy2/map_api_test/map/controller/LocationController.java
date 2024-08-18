import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/location")
    public ResponseEntity<String> getLocationName(
            @RequestParam("lat") double latitude,
            @RequestParam("lng") double longitude) {

        String locationName = locationService.getLocationName(latitude, longitude);
        return ResponseEntity.ok(locationName);
    }
}