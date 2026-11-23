package com.disaster.model;

public interface Deployable {
    void deployToZone(String zoneName);
    String getEntitySummary();
}