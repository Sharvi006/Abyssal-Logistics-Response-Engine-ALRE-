package com.gdrc.model;

public class EarthquakeZone extends DisasterEvent {
    private static final long serialVersionUID = 1L;
    private double richterMagnitude;

    public EarthquakeZone(String zoneId, String regionName, int severityLevel,
                          int estimatedAffectedPopulation, double latitude, double longitude,
                          double richterMagnitude) {
        super(zoneId, regionName, severityLevel, estimatedAffectedPopulation, latitude, longitude);
        this.richterMagnitude = richterMagnitude;
    }

    public double getRichterMagnitude() { return richterMagnitude; }

    @Override
    public String getPrimaryHazardType() {
        return "Earthquake (Magnitude " + richterMagnitude + ")";
    }

    @Override
    public int getRequiredClearanceLevel() {
        return (richterMagnitude >= 6.5) ? 3 : 2;
    }
}