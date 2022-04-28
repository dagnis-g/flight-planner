package io.codelex.flightplanner;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

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

    public List<Flight> getFlights() {
        return flights;
    }

    public void removeFlight(Flight flight) {
        flights.remove(flight);
    }

    public Flight getFlightById(long id) {
        for (Flight i : flights) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    private boolean checkIfSameFlightFromFlightRequest(Flight flight, SearchFlightsRequest request) {

        return flight.getFrom().getAirport().equalsIgnoreCase(request.getFrom())
                && flight.getTo().getAirport().equalsIgnoreCase(request.getTo())
                && flight.getDepartureTime().toString()
                .contains(String.valueOf(request.getDepartureDate()));
    }

    public PageResult searchFlightByFromToAndDepartureDate(SearchFlightsRequest flight) {
        final double RESULTS_PER_PAGE = 10.0;
        String from = flight.getFrom();
        String to = flight.getTo();

        if (from.equalsIgnoreCase(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        List<Flight> items = new ArrayList<>();

        for (Flight i : flights) {
            if (checkIfSameFlightFromFlightRequest(i, flight)) {
                items.add(i);
            }
        }

        int page = (int) Math.ceil(items.size() / RESULTS_PER_PAGE);
        return new PageResult(page, items.size(), items);
    }
}
