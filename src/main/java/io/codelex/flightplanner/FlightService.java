package io.codelex.flightplanner;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightService {
    private volatile static int id = 0;
    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public void clearFlights() {
        flightRepository.clearFlights();
    }

    public ResponseEntity<PageResult> searchFlightByFromToAndDepartureDate(SearchFlightsRequest flight) {
        return flightRepository.searchFlightByFromToAndDepartureDate(flight);
    }

    public List<Airport> searchAirport(String search) {
        List<Flight> allFlights = flightRepository.getFlights();
        List<Airport> allAirports = new ArrayList<>();
        List<Airport> searchResults = new ArrayList<>();
        for (Flight i : allFlights) {
            allAirports.add(i.getFrom());
        }
        for (Airport airport : allAirports) {
            if (airport.textForSearch().contains(search.trim().toLowerCase())) {
                searchResults.add(airport);
            }
        }
        return searchResults;
    }

    public void deleteFlightById(int id) {
        Flight flightToRemove = null;
        for (Flight i : flightRepository.getFlights()) {
            if (i.getId() == id) {
                flightToRemove = i;
            }
        }
        flightRepository.removeFlight(flightToRemove);
    }

    public Flight getFlightById(int id) {
        return flightRepository.getFlightById(id);
    }

    public ResponseEntity<Flight> addFlight(Flight flight) {
        if (checkIfFlightAlreadyInRepository(flight)) {
            return new ResponseEntity<Flight>(flight, HttpStatus.CONFLICT);
        }
        if (checkIfSameAirports(flight)) {
            return new ResponseEntity<Flight>(flight, HttpStatus.BAD_REQUEST);
        }
        if (!checkIfDepartureBeforeArrival(flight)) {
            return new ResponseEntity<Flight>(flight, HttpStatus.BAD_REQUEST);
        }

        flight.setId(id++);
        flightRepository.addFlight(flight);
        return new ResponseEntity<Flight>(flight, HttpStatus.CREATED);
    }

    @Override
    public String toString() {
        return "FlightService{" +
                "flightRepository=" + flightRepository +
                '}';
    }

    public List<Flight> getFlightList() {
        return flightRepository.getFlights();
    }

    public boolean checkIfFlightAlreadyInRepository(Flight flight) {
        return flightRepository.getFlights().stream()
                .anyMatch(i -> i.equals(flight));
    }

    public boolean checkIfSameAirports(Flight flight) {
        String cityFrom = flight.getFrom().getCity().trim().toLowerCase();
        String cityTo = flight.getTo().getCity().trim().toLowerCase();

        String countryFrom = flight.getFrom().getCountry().trim().toLowerCase();
        String countryTo = flight.getTo().getCountry().trim().toLowerCase();

        String airportFrom = flight.getFrom().getAirport().trim().toLowerCase();
        String airportTo = flight.getTo().getAirport().trim().toLowerCase();

        return cityFrom.equals(cityTo)
                && countryFrom.equals(countryTo)
                && airportFrom.equals(airportTo);
    }

    public boolean checkIfDepartureBeforeArrival(Flight flight) {
        LocalDateTime departure = LocalDateTime.parse(flight.getDepartureTime().replace(" ", "T"));
        LocalDateTime arrival = LocalDateTime.parse(flight.getArrivalTime().replace(" ", "T"));
        return departure.isBefore(arrival);
    }

}
