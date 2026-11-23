package com.disaster.model;

public interface Trackable {
    String getCoordinates();
    void updateLocation(String newLocation);
}
