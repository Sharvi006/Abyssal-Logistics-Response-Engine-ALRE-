package com.gdrc.model;

public class Helicopter extends RescueVehicle {
    private static final long serialVersionUID = 1L;
    private double maxAltitudeMeters;

    public Helicopter(String vehicleId, String modelName, double maxPayloadKg, double maxAltitudeMeters) {
        super(vehicleId, modelName, maxPayloadKg);
        this.maxAltitudeMeters = maxAltitudeMeters;
    }

    public double getMaxAltitudeMeters() { return maxAltitudeMeters; }

    @Override
    public String getTerrainCapability() {
        return "Aerial / Flooded / Blocked Roads (Max Alt: " + maxAltitudeMeters + "m)";
    }
}