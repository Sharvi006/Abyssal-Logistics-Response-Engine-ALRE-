package com.gdrc.model;

public class SupplyTruck extends RescueVehicle {
    private static final long serialVersionUID = 1L;
    private int axleCount;

    public SupplyTruck(String vehicleId, String modelName, double maxPayloadKg, int axleCount) {
        super(vehicleId, modelName, maxPayloadKg);
        this.axleCount = axleCount;
    }

    public int getAxleCount() { return axleCount; }

    @Override
    public String getTerrainCapability() {
        return "Heavy Off-Road / Highway (" + axleCount + "-Axle Heavy Hauler)";
    }
}