package safety;

import exceptions.ContaminationException;
import exceptions.SignalLostException;
import exceptions.SafetyViolationException;

public class DisasterSafetyService {

    private HazardDetector detector;
    private SafetyAuditLogger logger;

    public DisasterSafetyService(
            HazardDetector detector,
            SafetyAuditLogger logger) {

        this.detector = detector;
        this.logger = logger;
    }

    public <T> T validateInput(T value, String fieldName) {

        if (value == null) {
            throw new IllegalArgumentException(
                    fieldName + " cannot be null");
        }

        return value;
    }

    public void inspectZone(
            int row,
            int col,
            boolean contaminated,
            boolean authorized,
            boolean protectiveGear)
            throws ContaminationException,
                   SignalLostException,
                   SafetyViolationException {

        HazardLevel level = detector.getHazardAt(row, col);

        if (contaminated) {
            if (logger != null) {
                logger.submitEvent("CONTAMINATION detected at "
                        + row + "," + col);
            }
            throw new ContaminationException(
                    "Zone is contaminated!");
        }

        if (level == HazardLevel.CRITICAL) {
            if (logger != null) {
                logger.submitEvent("CRITICAL hazard at "
                        + row + "," + col);
            }
            throw new SignalLostException(
                    "Communication safety alert: Critical zone!");
        }

        if (level == HazardLevel.HIGH
                && (!authorized || !protectiveGear)) {
            if (logger != null) {
                logger.submitEvent("SAFETY VIOLATION at "
                        + row + "," + col);
            }
            throw new SafetyViolationException(
                    "Authorization or protective gear missing!");
        }

        if (logger != null) {
            logger.submitEvent("Zone inspected successfully at "
                    + row + "," + col);
        }

        System.out.println("Zone inspection successful: " + level);
    }

    public void displayHighRiskZones() {

        System.out.println("\n--- HIGH RISK ZONES ---");

        for (String zone : detector.getHighRiskZones()) {
            System.out.println(zone);
        }
    }

    public void displayCriticalCount() {

        System.out.println(
                "Total Critical Zones: "
                + detector.countCriticalZones());
    }
}