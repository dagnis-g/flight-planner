package io.codelex.flightplanner.service;

import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.model.PageResult;
import io.codelex.flightplanner.model.SearchFlightsRequest;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface FlightService {

    void clearFlights();

    PageResult searchFlightByFromToAndDepartureDate(SearchFlightsRequest flight);

    Set<Airport> searchAirport(String search);

    void deleteFlightById(long id);

    Flight getFlightById(long id);

    Flight addFlight(Flight flight);
}
