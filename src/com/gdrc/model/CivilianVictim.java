package com.gdrc.model;

public class CivilianVictim extends Person {
    private static final long serialVersionUID = 1L;
    private String triageStatus; // Critical, Stable, Minor

    public CivilianVictim(String id, String name, int age, String zoneId, String triageStatus) {
        super(id, name, age);
        this.assignedZoneId = zoneId;
        this.triageStatus = triageStatus;
    }

    public String getTriageStatus() { return triageStatus; }
    public void setTriageStatus(String triageStatus) { this.triageStatus = triageStatus; }

    @Override
    public String getRoleSummary() {
        return "Civilian Victim [Triage: " + triageStatus + "]";
    }
}