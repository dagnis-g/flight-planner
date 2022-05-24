package io.codelex.flightplanner.service;

import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.model.PageResult;
import io.codelex.flightplanner.model.SearchFlightsRequest;
import io.codelex.flightplanner.repository.FlightInMemoryRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(prefix = "flight-planner", name = "store-type", havingValue = "in-memory")
public class FlightInMemoryService extends FlightService {

    private volatile long id = 0;

    private final FlightInMemoryRepository flightRepository;

    public FlightInMemoryService(FlightInMemoryRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public void clearFlights() {
        flightRepository.clearFlights();
    }

    @Override
    public Set<Airport> searchAirport(String search) {
        return flightRepository.searchAirport(search);
    }

    @Override
    public void deleteFlightById(long id) {
        flightRepository.deleteFlightById(id);
    }

    @Override
    public Flight getFlightById(long id) {
        Flight flightToGet = flightRepository.getFlightById(id);
        if (flightToGet == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return flightToGet;
    }

    @Override
    public synchronized Flight addFlight(Flight flight) {

        if (checkIfFlightAlreadyInRepository(flight)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (flight.airportsMatch() || !checkIfDepartureBeforeArrival(flight)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        flight.setId(id++);
        flightRepository.addFlight(flight);
        return flight;
    }

    @Override
    public PageResult searchFlightByFromToAndDepartureDate(SearchFlightsRequest flight) {
        final double RESULTS_PER_PAGE = 10.0;
        String from = flight.getFrom();
        String to = flight.getTo();

        if (from.equalsIgnoreCase(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        List<Flight> items = flightRepository.getFlights().stream()
                .filter((Flight i) -> checkIfFlightRequestInRepo(i, flight))
                .collect(Collectors.toList());

        int page = (int) Math.ceil(items.size() / RESULTS_PER_PAGE);
        return new PageResult(page, items.size(), items);
    }

    private boolean checkIfFlightAlreadyInRepository(Flight flight) {
        return flightRepository.getFlights().stream().anyMatch(i -> i.equals(flight));
    }

    private boolean checkIfFlightRequestInRepo(Flight flight, SearchFlightsRequest request) {
        return flight.getFrom().getAirport().equalsIgnoreCase(request.getFrom())
                && flight.getTo().getAirport().equalsIgnoreCase(request.getTo())
                && flight.getDepartureTime().toString()
                .contains(String.valueOf(request.getDepartureDate()));
    }

}
