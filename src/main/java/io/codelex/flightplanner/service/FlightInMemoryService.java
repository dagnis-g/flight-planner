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

import java.util.Set;

@Service
@ConditionalOnProperty(prefix = "flight-planner", name = "store-type", havingValue = "in-memory")
public class FlightInMemoryService implements FlightService {

    private final FlightInMemoryRepository flightRepository;

    public FlightInMemoryService(FlightInMemoryRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public void clearFlights() {
        flightRepository.clearFlights();
    }

    @Override
    public PageResult searchFlightByFromToAndDepartureDate(SearchFlightsRequest flight) {
        return flightRepository.searchFlightByFromToAndDepartureDate(flight);
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
    public Flight addFlight(Flight flight) {
        return flightRepository.addFlight(flight);
    }

}
