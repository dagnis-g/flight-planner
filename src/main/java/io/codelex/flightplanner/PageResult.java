package io.codelex.flightplanner;

import java.util.List;

public class PageResult {
    private int page;
    private int totalItems;
    private List<Flight> items;

    public PageResult(int page, int totalItems, List<Flight> items) {
        this.page = page;
        this.totalItems = totalItems;
        this.items = items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public List<Flight> getItems() {
        return items;
    }

    public void setItems(List<Flight> items) {
        this.items = items;
    }
}
