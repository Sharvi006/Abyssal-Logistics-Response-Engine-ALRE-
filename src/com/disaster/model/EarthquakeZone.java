package com.disaster.model;

public class EarthquakeZone extends DisasterEvent {
    private double richterMagnitude;

    public EarthquakeZone(String eventId, String location, int severityLevel, double richterMagnitude) {
        super(eventId, location, severityLevel);
        this.richterMagnitude = richterMagnitude;
    }

    public double getRichterMagnitude() {
        return this.richterMagnitude;
    }

    @Override
    public void assessCrisis() {
        System.out.println("[EARTHQUAKE] ID: " + getEventId() + " | Location: " + getLocation()
                + " | Severity: " + getSeverityLevel() + "/5 | Magnitude: " + this.richterMagnitude + " Richter");
    }
}
