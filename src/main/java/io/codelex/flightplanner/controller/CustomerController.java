package io.codelex.flightplanner.controller;

import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.model.PageResult;
import io.codelex.flightplanner.model.SearchFlightsRequest;
import io.codelex.flightplanner.service.FlightService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CustomerController {

    private final FlightService flightService;

    public CustomerController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/airports")
    public Set<Airport> searchAirport(@RequestParam String search) {
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
