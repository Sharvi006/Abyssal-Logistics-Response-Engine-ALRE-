package member4;

import java.io.Serializable;

public class RescueVehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String vehicleId;
    private final String vehicleType;
    private final String targetZone;
    private final int zoneSeverity;

    public RescueVehicle(String vehicleId, String vehicleType, String targetZone, int zoneSeverity) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.targetZone = targetZone;
        this.zoneSeverity = zoneSeverity;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getTargetZone() {
        return targetZone;
    }

    public int getZoneSeverity() {
        return zoneSeverity;
    }

    @Override
    public String toString() {
        return "[" + vehicleId + "] " + vehicleType + " -> Zone: " + targetZone + " (Severity " + zoneSeverity + "/5)";
    }
}