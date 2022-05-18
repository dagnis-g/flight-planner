package io.codelex.flightplanner.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
public class Airport {

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @NotBlank
    @Id
    private String airport;

    public Airport(String country, String city, String airport) {
        this.country = country;
        this.city = city;
        this.airport = airport;
    }

    public Airport() {
    }

    public String getCountry() {
        return country;
    }

    public String textForSearch() {
        return (country + city + airport).toLowerCase();
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public boolean matches(Airport airport) {
        return getCity().trim().equalsIgnoreCase(airport.getCity().trim())
                && getCountry().trim().equalsIgnoreCase(airport.getCountry().trim())
                && getAirport().trim().equalsIgnoreCase(airport.getAirport().trim());
    }

    @Override
    public String toString() {
        return "Airport{"
                + "country='"
                + country + '\'' + ", city='" + city + '\''
                + ", airport='" + airport + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport1 = (Airport) o;
        return Objects.equals(country, airport1.country) && Objects.equals(city, airport1.city) && Objects.equals(airport, airport1.airport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, airport);
    }
}