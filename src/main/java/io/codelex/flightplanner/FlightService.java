package io.codelex.flightplanner;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FlightService {
    private long id = 0;
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void clearFlights() {
        flightRepository.clearFlights();
    }

    public PageResult searchFlightByFromToAndDepartureDate(SearchFlightsRequest flight) {
        return flightRepository.searchFlightByFromToAndDepartureDate(flight);
    }

    public List<Airport> searchAirport(String search) {
        List<Airport> searchResults = new ArrayList<>();

        Set<Airport> allAirports = flightRepository.getFlights().stream()
                .map(Flight::getFrom)
                .collect(Collectors.toSet());

        for (Airport airport : allAirports) {
            if (airport.textForSearch().contains(search.trim().toLowerCase())) {
                searchResults.add(airport);
            }
        }
        return searchResults;
    }

    public synchronized void deleteFlightById(long id) {
        Flight flightToRemove = null;
        for (Flight i : flightRepository.getFlights()) {
            if (i.getId() == id) {
                flightToRemove = i;
            }
        }
        flightRepository.removeFlight(flightToRemove);
    }

    public Flight getFlightById(long id) {
        Flight flightToGet = flightRepository.getFlightById(id);
        if (flightToGet == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return flightToGet;
    }

    public synchronized Flight addFlight(Flight flight) {
        if (checkIfFlightAlreadyInRepository(flight)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        if (flight.airportsMatch()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (!checkIfDepartureBeforeArrival(flight)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        flight.setId(id++);
        flightRepository.addFlight(flight);
        return flight;
    }

    public List<Flight> getFlightList() {
        return flightRepository.getFlights();
    }

    private boolean checkIfFlightAlreadyInRepository(Flight flight) {
        return flightRepository.getFlights().stream().anyMatch(i -> i.equals(flight));
    }

    private boolean checkIfDepartureBeforeArrival(Flight flight) {
        return flight.getDepartureTime().isBefore(flight.getArrivalTime());
    }

}
