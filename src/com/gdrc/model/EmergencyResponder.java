package com.gdrc.model;

public interface EmergencyResponder extends Deployable {
    int getClearanceLevel();
    String performDuty();
}