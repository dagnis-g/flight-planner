package io.codelex.flightplanner;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class SearchFlightsRequest {
    @NotNull
    private String from;
    @NotNull
    private String to;
    @NotNull
    private LocalDate departureDate;

    public SearchFlightsRequest(String from, String to, LocalDate departureDate) {
        this.from = from;
        this.to = to;
        this.departureDate = departureDate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    @Override
    public String toString() {
        return "SearchFlightsRequest{"
                + "from='" + from + '\''
                + ", to='" + to + '\''
                + ", departureDate=" + departureDate + '}';
    }
}
