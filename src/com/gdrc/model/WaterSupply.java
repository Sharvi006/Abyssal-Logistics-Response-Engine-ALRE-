package com.gdrc.model;

public class WaterSupply extends ReliefResource {
    private static final long serialVersionUID = 1L;
    private double litersPerUnit;

    public WaterSupply(String resourceId, double unitWeightKg, double litersPerUnit) {
        super(resourceId, "Water Supply", unitWeightKg, false);
        this.litersPerUnit = litersPerUnit;
    }

    public double getLitersPerUnit() { return litersPerUnit; }

    @Override
    public String getStorageInstructions() {
        return "Stack upright; avoid puncture hazards.";
    }
}