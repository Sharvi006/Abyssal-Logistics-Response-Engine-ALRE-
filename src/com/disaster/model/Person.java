package com.disaster.model;

public abstract class Person implements Deployable, Trackable {
    private String name;
    private String personnelId;
    private String currentLocation;

    public Person(String name, String personnelId) {
        this.name = name;
        this.personnelId = personnelId;
        this.currentLocation = "Central Base";
    }

    public String getName() {
        return this.name;
    }

    public String getPersonnelId() {
        return this.personnelId;
    }

    @Override
    public String getCoordinates() {
        return this.currentLocation;
    }

    @Override
    public void updateLocation(String newLocation) {
        if (newLocation != null && !newLocation.trim().isEmpty()) {
            this.currentLocation = newLocation.trim();
        }
    }

    public abstract void performDuty();

    @Override
    public String getEntitySummary() {
        return "ID: " + this.personnelId + " | Name: " + this.name + " | Location: " + this.currentLocation;
    }
}
