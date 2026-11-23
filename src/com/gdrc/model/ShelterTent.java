package com.gdrc.model;

public class ShelterTent extends ReliefResource {
    private static final long serialVersionUID = 1L;
    private int occupantCapacity;

    public ShelterTent(String resourceId, double unitWeightKg, int occupantCapacity) {
        super(resourceId, "Shelter Tent", unitWeightKg, false);
        this.occupantCapacity = occupantCapacity;
    }

    public int getOccupantCapacity() { return occupantCapacity; }

    @Override
    public String getStorageInstructions() {
        return "Keep canvas dry before folding to prevent mold.";
    }
}