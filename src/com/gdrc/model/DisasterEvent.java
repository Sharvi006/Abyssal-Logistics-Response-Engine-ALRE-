package com.gdrc.model;

import java.io.Serializable;

public abstract class DisasterEvent implements Serializable, Trackable {
    private static final long serialVersionUID = 1L;

    protected String zoneId;
    protected String regionName;
    protected int severityLevel; // 1 (Low) to 10 (Catastrophic)
    protected int estimatedAffectedPopulation;
    protected boolean active;
    protected double latitude;
    protected double longitude;

    public DisasterEvent(String zoneId, String regionName, int severityLevel,
                         int estimatedAffectedPopulation, double latitude, double longitude) {
        this.zoneId = zoneId;
        this.regionName = regionName;
        this.severityLevel = severityLevel;
        this.estimatedAffectedPopulation = estimatedAffectedPopulation;
        this.active = true;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public abstract String getPrimaryHazardType();
    public abstract int getRequiredClearanceLevel();

    @Override
    public String getCoordinates() {
        return String.format("%.4f, %.4f", latitude, longitude);
    }

    @Override
    public void updateCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getZoneId() { return zoneId; }
    public String getRegionName() { return regionName; }
    public int getSeverityLevel() { return severityLevel; }
    public void setSeverityLevel(int severityLevel) { this.severityLevel = severityLevel; }
    public int getEstimatedAffectedPopulation() { return estimatedAffectedPopulation; }
    public boolean isActive() { return active; }
    public void resolveDisaster() { this.active = false; }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) | Severity: %d/10 | Affected: %d | Active: %s",
                zoneId, regionName, getPrimaryHazardType(), severityLevel,
                estimatedAffectedPopulation, active ? "YES" : "NO");
    }
}