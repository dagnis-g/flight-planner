package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private final FlightService flightService;

    public CustomerController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/airports")
    public List<Airport> searchAirport(@RequestParam String search) {
        return flightService.searchAirport(search);
    }

    @PostMapping("/flights/search")
    public PageResult searchFlights(@Valid @RequestBody SearchFlightsRequest flight) {
        return flightService.searchFlightByFromToAndDepartureDate(flight);
    }

    @GetMapping("/flights/{id}")
    public Flight searchFlights(@PathVariable long id) {
        return flightService.getFlightById(id);
    }

}
