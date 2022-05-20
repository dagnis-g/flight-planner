package io.codelex.flightplanner.repository;

import io.codelex.flightplanner.model.Airport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
@ConditionalOnProperty(prefix = "flight-planner", name = "store-type", havingValue = "database")
public interface AirportDatabaseRepository extends JpaRepository<Airport, String> {
    
    @Query(value = "select a from Airport a where UPPER(a.airport) LIKE UPPER(CONCAT('%', :search, '%')) "
            + "or UPPER(a.country) LIKE UPPER(CONCAT('%', :search, '%')) "
            + "or UPPER(a.city) LIKE UPPER(CONCAT('%', :search, '%'))")
    Set<Airport> findByAllColumns(String search);

}
