package io.codelex.flightplanner;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Flight {

    private long id;
    @Valid
    @NotNull
    private Airport from;
    @Valid
    @NotNull
    private Airport to;
    @NotBlank
    private String carrier;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureTime;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalTime;

    public Flight(long id, Airport from, Airport to, String carrier, String departureTime, String arrivalTime) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.carrier = carrier;
        this.departureTime = LocalDateTime.parse(departureTime, formatter);
        this.arrivalTime = LocalDateTime.parse(arrivalTime, formatter);
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Airport getFrom() {
        return from;
    }

    public void setFrom(Airport from) {
        this.from = from;
    }

    public Airport getTo() {
        return to;
    }

    public void setTo(Airport to) {
        this.to = to;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "Flight{"
                + "id=" + id
                + ", from=" + from
                + ", to=" + to
                + ", carrier='" + carrier + '\''
                + ", departureTime=" + departureTime
                + ", arrivalTime=" + arrivalTime
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equals(from, flight.from) && Objects.equals(to, flight.to) && Objects.equals(carrier, flight.carrier) && Objects.equals(departureTime, flight.departureTime) && Objects.equals(arrivalTime, flight.arrivalTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, carrier, departureTime, arrivalTime);
    }
}
