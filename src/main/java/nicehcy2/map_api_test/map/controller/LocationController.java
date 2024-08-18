package nicehcy2.map_api_test.map.controller;

import nicehcy2.map_api_test.map.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/address")
    public ResponseEntity<String> getAddress(@RequestParam double latitude, @RequestParam double longitude) {
        String address = locationService.getAddressFromCoordinates(latitude, longitude);
        return ResponseEntity.ok(address);
    }
}
