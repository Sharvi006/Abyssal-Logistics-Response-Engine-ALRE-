package com.disaster.model;

public class FloodZone extends DisasterEvent {
    private double waterLevelMeters;

    public FloodZone(String eventId, String location, int severityLevel, double waterLevelMeters) {
        super(eventId, location, severityLevel);
        this.waterLevelMeters = waterLevelMeters;
    }

    public double getWaterLevelMeters() {
        return this.waterLevelMeters;
    }

    @Override
    public void assessCrisis() {
        System.out.println("[FLOOD] ID: " + getEventId() + " | Location: " + getLocation()
                + " | Severity: " + getSeverityLevel() + "/5 | Water Level: " + this.waterLevelMeters + "m");
    }
}
