package com.gdrc.model;

public class FoodRation extends ReliefResource {
    private static final long serialVersionUID = 1L;
    private int caloriesPerPack;

    public FoodRation(String resourceId, double unitWeightKg, int caloriesPerPack) {
        super(resourceId, "Food Ration", unitWeightKg, true);
        this.caloriesPerPack = caloriesPerPack;
    }

    public int getCaloriesPerPack() { return caloriesPerPack; }

    @Override
    public String getStorageInstructions() {
        return "Keep sealed away from moisture and direct sunlight.";
    }
}