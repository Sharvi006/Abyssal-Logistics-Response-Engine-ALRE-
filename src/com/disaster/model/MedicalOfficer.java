package com.disaster.model;

public class MedicalOfficer extends Person {
    private String specialization;

    public MedicalOfficer(String name, String personnelId, String specialization) {
        super(name, personnelId);
        this.specialization = specialization;
    }

    public String getSpecialization() {
        return this.specialization;
    }

    @Override
    public void performDuty() {
        System.out.println("Medical Officer " + getName() + " providing " + this.specialization + " medical aid.");
    }

    @Override
    public void deployToZone(String zoneName) {
        updateLocation(zoneName);
        System.out.println("Deployed Medical Officer [" + getPersonnelId() + "] " + getName() + " to " + zoneName + ".");
    }
}