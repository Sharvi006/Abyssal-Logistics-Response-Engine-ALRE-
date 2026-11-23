package com.gdrc.model;

public class MedicalOfficer extends Person implements EmergencyResponder {
    private static final long serialVersionUID = 1L;
    private String specialization;
    private boolean available;

    public MedicalOfficer(String id, String name, int age, String specialization) {
        super(id, name, age);
        this.specialization = specialization;
        this.available = true;
    }

    @Override
    public int getClearanceLevel() {
        return 3; // Highest clearance for red zones
    }

    @Override
    public String performDuty() {
        return "Dr. " + name + " (" + specialization + ") is treating trauma victims in " + assignedZoneId;
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
        return "Medical Officer (" + specialization + ")";
    }
}