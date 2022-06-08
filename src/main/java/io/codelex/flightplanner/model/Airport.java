package io.codelex.flightplanner.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

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

        if (country != null ? !country.equals(airport1.country) : airport1.country != null) return false;
        if (city != null ? !city.equals(airport1.city) : airport1.city != null) return false;
        return airport != null ? airport.equals(airport1.airport) : airport1.airport == null;
    }

    @Override
    public int hashCode() {
        int result = country != null ? country.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (airport != null ? airport.hashCode() : 0);
        return result;
    }
}
