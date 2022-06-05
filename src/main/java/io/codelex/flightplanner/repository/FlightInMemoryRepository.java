package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class FlightInMemoryRepository {

    private final List<Flight> flights = new ArrayList<>();

    public void clearFlights() {
        flights.clear();
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public Flight getFlightById(long id) {
        return getFlights().stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public synchronized void deleteFlightById(long id) {
        flights.removeIf(f -> f.getId() == id);
    }

    public void addFlight(Flight flight) {
        flights.add(flight);
    }

    public Set<Airport> searchAirport(String search) {
        return getFlights().stream()
                .map(Flight::getFrom)
                .filter((Airport i) -> i.textForSearch().contains(search.trim().toLowerCase()))
                .collect(Collectors.toSet());
    }

}
