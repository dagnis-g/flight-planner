package io.codelex.flightplanner.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "flight_id")
    private long id;

    @JoinColumn(name = "airport_from")
    @ManyToOne
    @Valid
    @NotNull
    private Airport from;

    @JoinColumn(name = "airport_to")
    @ManyToOne
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
        this.departureTime = formatDate(departureTime);
        this.arrivalTime = formatDate(arrivalTime);
    }

    public Flight() {

    }

    private LocalDateTime formatDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return LocalDateTime.parse(date, formatter);
    }

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

    public boolean airportsMatch() {
        return from.matches(to);
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

        if (from != null ? !from.equals(flight.from) : flight.from != null) return false;
        if (to != null ? !to.equals(flight.to) : flight.to != null) return false;
        if (carrier != null ? !carrier.equals(flight.carrier) : flight.carrier != null) return false;
        if (departureTime != null ? !departureTime.equals(flight.departureTime) : flight.departureTime != null)
            return false;
        return arrivalTime != null ? arrivalTime.equals(flight.arrivalTime) : flight.arrivalTime == null;
    }

    @Override
    public int hashCode() {
        int result = from != null ? from.hashCode() : 0;
        result = 31 * result + (to != null ? to.hashCode() : 0);
        result = 31 * result + (carrier != null ? carrier.hashCode() : 0);
        result = 31 * result + (departureTime != null ? departureTime.hashCode() : 0);
        result = 31 * result + (arrivalTime != null ? arrivalTime.hashCode() : 0);
        return result;
    }
}
