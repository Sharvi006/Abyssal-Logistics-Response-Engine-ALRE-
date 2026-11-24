package member4;

import java.io.Serializable;

public class PriorityZoneEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String zoneId;
    private final int severity;       // 1 (low) to 5 (critical)
    private final int supplyDeficit;  // missing supply units

    public PriorityZoneEntry(String zoneId, int severity, int supplyDeficit) {
        this.zoneId = zoneId;
        this.severity = severity;
        this.supplyDeficit = supplyDeficit;
    }

    public String getZoneId() {
        return zoneId;
    }

    public int getSeverity() {
        return severity;
    }

    public int getSupplyDeficit() {
        return supplyDeficit;
    }

    @Override
    public String toString() {
        return "[" + zoneId + "] Severity: " + severity + "/5 | Supply Deficit: " + supplyDeficit;
    }
}