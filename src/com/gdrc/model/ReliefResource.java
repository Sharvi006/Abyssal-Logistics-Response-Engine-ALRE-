package com.gdrc.model;

import java.io.Serializable;

public abstract class ReliefResource implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String resourceId;
    protected String name;
    protected double unitWeightKg;
    protected boolean perishable;

    public ReliefResource(String resourceId, String name, double unitWeightKg, boolean perishable) {
        this.resourceId = resourceId;
        this.name = name;
        this.unitWeightKg = unitWeightKg;
        this.perishable = perishable;
    }

    public abstract String getStorageInstructions();

    public String getResourceId() { return resourceId; }
    public String getName() { return name; }
    public double getUnitWeightKg() { return unitWeightKg; }
    public boolean isPerishable() { return perishable; }

    @Override
    public String toString() {
        return String.format("%s (%s) | Weight: %.2f kg | Perishable: %s",
                name, resourceId, unitWeightKg, perishable ? "Yes" : "No");
    }
}