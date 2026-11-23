package com.gdrc.model;

public class FieldVolunteer extends Person implements EmergencyResponder {
    private static final long serialVersionUID = 1L;
    private String skillSet;
    private boolean available;

    public FieldVolunteer(String id, String name, int age, String skillSet) {
        super(id, name, age);
        this.skillSet = skillSet;
        this.available = true;
    }

    @Override
    public int getClearanceLevel() {
        return 1; // Standard clearance for safe/yellow zones
    }

    @Override
    public String performDuty() {
        return "Volunteer " + name + " is assisting with " + skillSet + " in " + assignedZoneId;
    }

    @Override
    public boolean isAvailable() { return available; }

    @Override
    public void deploy(String zoneId) {
        this.available = false;
        this.assignedZoneId = zoneId;
    }

    @Override
    public void recall() {
        this.available = true;
        this.assignedZoneId = "HQ";
    }

    @Override
    public String getRoleSummary() {
        return "Field Volunteer (" + skillSet + ")";
    }
}