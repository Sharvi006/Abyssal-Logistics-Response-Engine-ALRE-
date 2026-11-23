package com.gdrc.model;

public interface Trackable {
    String getCoordinates();
    void updateCoordinates(double latitude, double longitude);
}