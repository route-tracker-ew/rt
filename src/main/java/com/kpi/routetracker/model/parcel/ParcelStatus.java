package com.kpi.routetracker.model.parcel;

public enum ParcelStatus {
    CHECKING("CHECKING", 0),
    PICK_UP("PICK_UP", 1),
    ON_THE_WAY("ON_THE_WAY", 2),
    DELIVERED("DELIVERED", 3),
    CANCELED("CANCELED", 4),
    REFUSED("REFUSED", 5);

    private final String status;
    private final int order;

    ParcelStatus(String status, int order) {
        this.status = status;
        this.order = order;
    }

    public String getStatus() {
        return status;
    }

    public int getOrder() {
        return order;
    }
}