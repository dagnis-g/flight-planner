package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.FlightService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class TestingController {

    private final FlightService flightService;

    public TestingController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/testing-api")
    public String hello() {
        return "hello";
    }
    
    @PostMapping("/testing-api/clear")
    public void clearFlights() {
        flightService.clearFlights();
    }
}
