package com.gdrc.model;

import java.io.Serializable;

public abstract class RescueVehicle implements Serializable, Deployable, Trackable {
    private static final long serialVersionUID = 1L;

    protected String vehicleId;
    protected String modelName;
    protected double maxPayloadKg;
    protected double currentLoadKg;
    protected boolean available;
    protected String deployedZoneId;
    protected double latitude;
    protected double longitude;

    public RescueVehicle(String vehicleId, String modelName, double maxPayloadKg) {
        this.vehicleId = vehicleId;
        this.modelName = modelName;
        this.maxPayloadKg = maxPayloadKg;
        this.currentLoadKg = 0.0;
        this.available = true;
        this.deployedZoneId = "BASE";
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public abstract String getTerrainCapability();

    public void loadCargo(double weightKg) {
        this.currentLoadKg += weightKg;
    }

    public void unloadCargo() {
        this.currentLoadKg = 0.0;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public void deploy(String zoneId) {
        this.available = false;
        this.deployedZoneId = zoneId;
    }

    @Override
    public void recall() {
        this.available = true;
        this.deployedZoneId = "BASE";
        unloadCargo();
    }

    @Override
    public String getCoordinates() {
        return String.format("%.4f, %.4f", latitude, longitude);
    }

    @Override
    public void updateCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getVehicleId() { return vehicleId; }
    public String getModelName() { return modelName; }
    public double getMaxPayloadKg() { return maxPayloadKg; }
    public double getCurrentLoadKg() { return currentLoadKg; }

    @Override
    public String toString() {
        return String.format("[%s] %s | Load: %.1f/%.1f kg | Status: %s",
                vehicleId, modelName, currentLoadKg, maxPayloadKg,
                available ? "Available" : "Deployed to " + deployedZoneId);
    }
}