package com.disaster.model;

public class FieldVolunteer extends Person {
    private String assignedTask;

    public FieldVolunteer(String name, String personnelId, String assignedTask) {
        super(name, personnelId);
        this.assignedTask = assignedTask;
    }

    public String getAssignedTask() {
        return this.assignedTask;
    }

    @Override
    public void performDuty() {
        System.out.println("Field Volunteer " + getName() + " executing task: " + this.assignedTask + ".");
    }

    @Override
    public void deployToZone(String zoneName) {
        updateLocation(zoneName);
        System.out.println("Deployed Field Volunteer [" + getPersonnelId() + "] " + getName() + " to " + zoneName + ".");
    }
}