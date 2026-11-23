package com.gdrc.model;

public interface Deployable {
    boolean isAvailable();
    void deploy(String zoneId);
    void recall();
}