package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.Flight;
import io.codelex.flightplanner.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin-api")
public class AdminController {

    private final FlightService flightService;

    public AdminController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PutMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public Flight addFlight(@Valid @RequestBody Flight flight) {
        return flightService.addFlight(flight);
    }

    @DeleteMapping("/flights/{id}")
    public void deleteFlightById(@PathVariable long id) {
        flightService.deleteFlightById(id);
    }

    @GetMapping("/flights/{id}")
    public Flight getFlight(@PathVariable long id) {
        return flightService.getFlightById(id);
    }

}
