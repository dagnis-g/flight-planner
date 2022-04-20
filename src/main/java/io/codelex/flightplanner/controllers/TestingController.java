package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.FlightService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testing-api")
public class TestingController {

    private final FlightService flightService;

    public TestingController(FlightService flightService) {
        this.flightService = flightService;
    }
    
    @PostMapping("/clear")
    public void clearFlights() {
        flightService.clearFlights();
    }
}
