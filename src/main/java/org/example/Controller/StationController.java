package org.example.Controller;

import org.example.Model.StationInfo;
import org.example.Service.StationService;
import org.example.Model.Station;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/stations")
public class StationController {
    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @GetMapping
    public List<StationInfo> getAllStations() {
        return stationService.getAllStations();
    }

    @PostMapping
    public Station createStation(@RequestBody Station station) {
        return stationService.createStation(station);
    }

    @PutMapping("/{id}")
    public Station updateStation(@PathVariable int id, @RequestBody Station station) {
        return stationService.updateStation(id, station);
    }

    @DeleteMapping("/{id}")
    public void deleteStation(@PathVariable int id) {
        stationService.deleteStation(id);
    }

    @PostMapping("/control")
    public void controlStation(@PathVariable int id, @RequestBody ControlRequest request) {
        request.setStationId(id); // Gán ID của station vào request
        stationService.controlStation(request);
    }
}
