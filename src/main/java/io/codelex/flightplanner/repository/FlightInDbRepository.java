package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.model.Airport;
import io.codelex.flightplanner.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightInDbRepository extends JpaRepository<Flight, Long> {

    boolean existsFlightByFromAndToAndCarrierAndDepartureTimeAndArrivalTime(
            Airport from,
            Airport to,
            String carrier,
            LocalDateTime departureTime,
            LocalDateTime arrivalTime);

    @Query(value = "select f from Flight f join Airport a on f.from = a.airport join Airport a2 on f.to = a2.airport where a.airport = :from and a2.airport = :to and f.departureTime >= :date_start AND f.departureTime < :date_end")
    List<Flight> checkIfFlightRequestInDb(@Param("from") String from,
                                          @Param("to") String to,
                                          @Param("date_start") LocalDateTime dateStart,
                                          @Param("date_end") LocalDateTime dateEnd);

}
