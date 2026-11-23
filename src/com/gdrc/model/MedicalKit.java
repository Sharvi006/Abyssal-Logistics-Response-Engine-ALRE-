package com.gdrc.model;

public class MedicalKit extends ReliefResource {
    private static final long serialVersionUID = 1L;
    private boolean containsAntibiotics;

    public MedicalKit(String resourceId, double unitWeightKg, boolean containsAntibiotics) {
        super(resourceId, "Medical Kit", unitWeightKg, true);
        this.containsAntibiotics = containsAntibiotics;
    }

    public boolean hasAntibiotics() { return containsAntibiotics; }

    @Override
    public String getStorageInstructions() {
        return "Store in dry, temperature-controlled container (2°C - 8°C).";
    }
}