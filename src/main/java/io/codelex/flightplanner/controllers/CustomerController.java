package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin()
@RestController
public class CustomerController {

    private final FlightService flightService;

    public CustomerController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/api/airports")
    public List<Airport> searchAirport(@RequestParam String search) {
        return flightService.searchAirport(search);
    }

    @PostMapping("/api/flights/search")
    public ResponseEntity<PageResult> searchFlights(@Valid @RequestBody SearchFlightsRequest flight) {
        return flightService.searchFlightByFromToAndDepartureDate(flight);
    }

    @GetMapping("/api/flights/{id}")
    public ResponseEntity<Flight> searchFlights(@PathVariable int id) {
        Flight result = flightService.getFlightById(id);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(flightService.getFlightById(id), HttpStatus.OK);
    }

}
