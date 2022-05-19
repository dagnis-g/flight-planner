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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@ConditionalOnProperty(prefix = "flight-planner", name = "store-type", havingValue = "database")
public class FlightInDbService implements FlightService {

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

        List<Flight> items = flightInDbRepository.findAll().stream()
                .filter((Flight i) -> checkIfSameFlightFromFlightRequest(i, flight))
                .collect(Collectors.toList());

        int page = (int) Math.ceil(items.size() / RESULTS_PER_PAGE);
        return new PageResult(page, items.size(), items);
    }

    @Override
    public Set<Airport> searchAirport(String search) {
        return airportDatabaseRepository.findAll().stream()
                .filter((Airport i) -> i.textForSearch().contains(search.trim().toLowerCase()))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
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
    @Transactional
    public Flight addFlight(Flight flight) {
        if (!checkIfAirportInDB(flight.getTo())) {
            flight.setTo(airportDatabaseRepository.save(flight.getTo()));
        }
        if (!checkIfAirportInDB(flight.getFrom())) {
            flight.setFrom(airportDatabaseRepository.save(flight.getFrom()));
        }

        if (checkIfFlightInDB(flight)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (flight.airportsMatch() || !checkIfDepartureBeforeArrival(flight)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return flightInDbRepository.save(flight);
    }

    private boolean checkIfSameFlightFromFlightRequest(Flight flight, SearchFlightsRequest request) {
        return flight.getFrom().getAirport().equalsIgnoreCase(request.getFrom())
                && flight.getTo().getAirport().equalsIgnoreCase(request.getTo())
                && flight.getDepartureTime().toString()
                .contains(String.valueOf(request.getDepartureDate()));
    }

    private boolean checkIfAirportInDB(Airport airport) {
        return airportDatabaseRepository.findById(airport.getAirport()).isPresent();
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

    private boolean checkIfDepartureBeforeArrival(Flight flight) {
        return flight.getDepartureTime().isBefore(flight.getArrivalTime());
    }

}
