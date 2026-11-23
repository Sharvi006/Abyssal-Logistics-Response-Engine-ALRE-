package com.disaster.model;

public abstract class DisasterEvent implements Trackable {
    private String eventId;
    private String location;
    private int severityLevel; // 1 to 5

    public DisasterEvent(String eventId, String location, int severityLevel) {
        this.eventId = eventId;
        this.location = location;
        this.severityLevel = (severityLevel >= 1 && severityLevel <= 5) ? severityLevel : 1;
    }

    public String getEventId() {
        return this.eventId;
    }

    public String getLocation() {
        return this.location;
    }

    public int getSeverityLevel() {
        return this.severityLevel;
    }

    @Override
    public String getCoordinates() {
        return this.location;
    }

    @Override
    public void updateLocation(String newLocation) {
        if (newLocation != null && !newLocation.trim().isEmpty()) {
            this.location = newLocation.trim();
        }
    }

    public abstract void assessCrisis();
}
