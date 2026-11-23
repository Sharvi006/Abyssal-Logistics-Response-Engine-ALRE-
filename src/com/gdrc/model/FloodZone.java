package com.gdrc.model;

public class FloodZone extends DisasterEvent {
    private static final long serialVersionUID = 1L;
    private double waterLevelMeters;

    public FloodZone(String zoneId, String regionName, int severityLevel,
                     int estimatedAffectedPopulation, double latitude, double longitude,
                     double waterLevelMeters) {
        super(zoneId, regionName, severityLevel, estimatedAffectedPopulation, latitude, longitude);
        this.waterLevelMeters = waterLevelMeters;
    }

    public double getWaterLevelMeters() { return waterLevelMeters; }

    @Override
    public String getPrimaryHazardType() {
        return "Severe Flooding (" + waterLevelMeters + "m surge)";
    }

    @Override
    public int getRequiredClearanceLevel() {
        return (waterLevelMeters >= 2.5) ? 3 : 1;
    }
}