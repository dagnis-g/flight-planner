package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.Flight;
import io.codelex.flightplanner.FlightService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class TestingController {

    private final FlightService flightService;

    public TestingController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping("/testing-api")
    public String hello(){
        return "hello";
    }

//    @GetMapping("/testing-api/getall")
//    public List<Flight> getFlights(){
//        return flightService.getFlightList();
//    }

    @PostMapping("/testing-api/clear")
    public void clearFlights(){
        flightService.clearFlights();
    }
}
