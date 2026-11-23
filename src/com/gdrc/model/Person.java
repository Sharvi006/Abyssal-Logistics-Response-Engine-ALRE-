package com.gdrc.model;

import java.io.Serializable;

public abstract class Person implements Serializable, Comparable<Person> {
    private static final long serialVersionUID = 1L;

    protected String id;
    protected String name;
    protected int age;
    protected String assignedZoneId;

    public Person(String id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.assignedZoneId = "HQ";
    }

    public abstract String getRoleSummary();

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getAssignedZoneId() { return assignedZoneId; }
    public void setAssignedZoneId(String assignedZoneId) { this.assignedZoneId = assignedZoneId; }

    @Override
    public int compareTo(Person other) {
        return this.id.compareToIgnoreCase(other.id);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s (%s) - Location: %s", id, name, getRoleSummary(), assignedZoneId);
    }
}