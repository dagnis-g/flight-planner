package io.codelex.flightplanner.service;

import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import io.codelex.flightplanner.model.PageResult;
import io.codelex.flightplanner.model.SearchFlightsRequest;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public abstract class FlightService {

    public abstract void clearFlights();

    public abstract PageResult searchFlightByFromToAndDepartureDate(SearchFlightsRequest flight);

    public abstract Set<Airport> searchAirport(String search);

    public abstract void deleteFlightById(long id);

    public abstract Flight getFlightById(long id);

    public abstract Flight addFlight(Flight flight);

    boolean checkIfDepartureBeforeArrival(Flight flight) {
        return flight.getDepartureTime().isBefore(flight.getArrivalTime());
    }

}
