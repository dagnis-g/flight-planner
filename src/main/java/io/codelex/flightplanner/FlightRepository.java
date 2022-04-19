package io.codelex.flightplanner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FlightRepository {

    private final List<Flight> flights = new ArrayList<>();

    public void clearFlights() {
        flights.clear();
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    @Override
    public String toString() {
        return "FlightRepository{" +
                "flights=" + flights +
                '}';
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void removeFlight(Flight flight) {
        flights.remove(flight);
    }

    public Flight getFlightById(int id) {
        for (Flight i : flights) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    public boolean checkIfSameFlightFromFlightRequest(Flight flight, SearchFlightsRequest request) {
        return flight.getFrom().getAirport().equalsIgnoreCase(request.getFrom())
                && flight.getTo().getAirport().equalsIgnoreCase(request.getTo())
                && flight.getDepartureTime().contains(String.valueOf(request.getDepartureDate()));
    }

    public ResponseEntity<PageResult> searchFlightByFromToAndDepartureDate(SearchFlightsRequest flight) {
        final double RESULTS_PER_PAGE = 10.0;
        String from = flight.getFrom();
        String to = flight.getTo();

        if (from.equalsIgnoreCase(to)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        int totalItems = 0;
        List<Flight> items = new ArrayList<>();

        for (Flight i : flights) {
            if (checkIfSameFlightFromFlightRequest(i, flight)) {
                totalItems++;
                items.add(i);
            }
        }
        
        int page = (int) Math.ceil(totalItems / RESULTS_PER_PAGE);
        PageResult result = new PageResult(page, totalItems, items);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
