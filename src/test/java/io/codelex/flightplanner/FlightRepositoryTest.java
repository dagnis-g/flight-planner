package io.codelex.flightplanner;

import io.codelex.flightplanner.controller.AdminController;
import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class FlightRepositoryTest {

    @Autowired
    AdminController adminController;

    @Test
    void addFlight() {
        Airport from = new Airport("Latvia", "Riga", "RIX");
        Airport to = new Airport("Estonia", "Tallin", "EST");
        String departureTime = "2022-04-02 08:30";
        String arrivalTime = "2022-04-02 09:30";
        String carrier = "Air";

        Flight flight = new Flight(1, from, to, carrier, departureTime, arrivalTime);

        Flight savedFlight = adminController.addFlight(flight);

        assertEquals(savedFlight.getFrom(), from);
        assertEquals(savedFlight.getTo(), to);
        assertEquals(savedFlight.getCarrier(), carrier);
        assertEquals(savedFlight.getDepartureTime(), LocalDateTime.of(2022, 4, 2, 8, 30));
        assertEquals(savedFlight.getArrivalTime(), LocalDateTime.of(2022, 4, 2, 9, 30));

    }
}