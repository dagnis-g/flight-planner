package io.codelex.flightplanner.controllers;

import io.codelex.flightplanner.Flight;
import io.codelex.flightplanner.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin()
@RestController
public class AdminController {

    private final FlightService flightService;

    public AdminController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PutMapping("/admin-api/flights")
    public synchronized ResponseEntity<Flight> addFlight(@Valid @RequestBody Flight flight) {
        return flightService.addFlight(flight);
    }

    @DeleteMapping("/admin-api/flights/{id}")
    public synchronized HttpStatus deleteFlightById(@PathVariable int id) {
        Flight flightToDelete = flightService.getFlightById(id);
        if (flightToDelete != null) {
            flightService.deleteFlightById(id);
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }

    @GetMapping("/admin-api/flights/{id}")
    public synchronized ResponseEntity<Flight> getFlight(@PathVariable int id) {
        Flight flightToGet = flightService.getFlightById(id);
        if (flightToGet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(flightToGet, HttpStatus.OK);
    }

}
