package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.model.PageResult;
import io.codelex.flightplanner.model.SearchFlightsRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class FlightInMemoryRepository {

    private final List<Flight> flights = new ArrayList<>();
    private volatile long id = 0;

    public void clearFlights() {
        flights.clear();
    }

    public synchronized List<Flight> getFlights() {
        return flights;
    }

    public synchronized Flight getFlightById(long id) {
        return getFlights().stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public synchronized void deleteFlightById(long id) {
        getFlights().remove(getFlightById(id));
    }

    public synchronized Flight addFlight(Flight flight) {

        if (checkIfFlightAlreadyInRepository(flight)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (flight.airportsMatch() || !checkIfDepartureBeforeArrival(flight)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        flight.setId(id++);
        getFlights().add(flight);
        return flight;
    }

    public Set<Airport> searchAirport(String search) {
        return getFlights().stream()
                .map(Flight::getFrom)
                .filter((Airport i) -> i.textForSearch().contains(search.trim().toLowerCase()))
                .collect(Collectors.toSet());
    }

    public PageResult searchFlightByFromToAndDepartureDate(SearchFlightsRequest flight) {
        final double RESULTS_PER_PAGE = 10.0;
        String from = flight.getFrom();
        String to = flight.getTo();

        if (from.equalsIgnoreCase(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        List<Flight> items = getFlights().stream()
                .filter((Flight i) -> checkIfSameFlightFromFlightRequest(i, flight))
                .collect(Collectors.toList());

        int page = (int) Math.ceil(items.size() / RESULTS_PER_PAGE);
        return new PageResult(page, items.size(), items);
    }

    private synchronized boolean checkIfSameFlightFromFlightRequest(Flight flight, SearchFlightsRequest request) {
        return flight.getFrom().getAirport().equalsIgnoreCase(request.getFrom())
                && flight.getTo().getAirport().equalsIgnoreCase(request.getTo())
                && flight.getDepartureTime().toString()
                .contains(String.valueOf(request.getDepartureDate()));
    }

    private synchronized boolean checkIfFlightAlreadyInRepository(Flight flight) {
        return getFlights().stream().anyMatch(i -> i.equals(flight));
    }

    private synchronized boolean checkIfDepartureBeforeArrival(Flight flight) {
        return flight.getDepartureTime().isBefore(flight.getArrivalTime());
    }
}
