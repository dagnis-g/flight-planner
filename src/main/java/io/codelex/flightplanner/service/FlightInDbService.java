package io.codelex.flightplanner.service;

import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.model.PageResult;
import io.codelex.flightplanner.model.SearchFlightsRequest;
import io.codelex.flightplanner.repository.AirportDatabaseRepository;
import io.codelex.flightplanner.repository.FlightInDbRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@ConditionalOnProperty(prefix = "flight-planner", name = "store-type", havingValue = "database")
public class FlightInDbService extends FlightService {

    private final FlightInDbRepository flightInDbRepository;
    private final AirportDatabaseRepository airportDatabaseRepository;

    public FlightInDbService(FlightInDbRepository flightDatabaseRepository, AirportDatabaseRepository airportDatabaseRepository) {
        this.flightInDbRepository = flightDatabaseRepository;
        this.airportDatabaseRepository = airportDatabaseRepository;
    }

    @Override
    public void clearFlights() {
        flightInDbRepository.deleteAll();
        airportDatabaseRepository.deleteAll();
    }

    @Override
    public PageResult searchFlightByFromToAndDepartureDate(SearchFlightsRequest flight) {
        final double RESULTS_PER_PAGE = 10.0;
        String from = flight.getFrom();
        String to = flight.getTo();

        if (from.equalsIgnoreCase(to)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        LocalDateTime departureDateStart = flight.getDepartureDate().atStartOfDay();
        LocalDateTime departureDateEnd = LocalDateTime.from(flight.getDepartureDate().plusDays(1).atStartOfDay());

        List<Flight> items = flightInDbRepository.checkIfFlightRequestInDb(from, to, departureDateStart, departureDateEnd);

        int page = (int) Math.ceil(items.size() / RESULTS_PER_PAGE);
        return new PageResult(page, items.size(), items);
    }

    @Override
    public Set<Airport> searchAirport(String search) {
        return airportDatabaseRepository.findByAllColumns(search.trim());
    }

    @Override
    public void deleteFlightById(long id) {
        flightInDbRepository.findById(id).ifPresent(flightInDbRepository::delete);
    }

    @Override
    public Flight getFlightById(long id) {
        Flight flight = flightInDbRepository.findById(id).orElse(null);
        if (flight == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return flight;
    }

    @Override
    public Flight addFlight(Flight flight) {

        if (checkIfFlightInDB(flight)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (flight.airportsMatch() || !checkIfDepartureBeforeArrival(flight)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        flight.setFrom(findOrCreateAirport(flight.getFrom()));
        flight.setTo(findOrCreateAirport(flight.getTo()));

        return flightInDbRepository.save(flight);
    }

    private Airport findOrCreateAirport(Airport airport) {
        Optional<Airport> existingAirport = airportDatabaseRepository.findById(airport.getAirport());
        return existingAirport.orElseGet(() -> airportDatabaseRepository.save(airport));
    }

    private boolean checkIfFlightInDB(Flight flight) {
        return flightInDbRepository
                .existsByFromAndToAndCarrierAndDepartureTimeAndArrivalTime(
                        flight.getFrom(),
                        flight.getTo(),
                        flight.getCarrier(),
                        flight.getDepartureTime(),
                        flight.getArrivalTime());
    }

}
