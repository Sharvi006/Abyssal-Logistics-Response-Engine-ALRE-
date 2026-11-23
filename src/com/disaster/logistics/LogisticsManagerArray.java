package com.disaster.logistics;

import com.disaster.model.DisasterEvent;

public class LogisticsManagerArray {
    private static final int MAX_ZONES = 4;
    private static int totalManagerInstances = 0;

    private String[] inventoryItems;
    private int[] itemQuantities;
    private String[] personnelRoster;
    private DisasterEvent[] activeEvents;
    private int[][] zoneSupplyMatrix; // 2D array: [zoneIndex][itemIndex]

    private int itemCount;
    private int personnelCount;
    private int eventCount;

    public LogisticsManagerArray(int capacity) {
        int validCapacity = (capacity > 0) ? capacity : 10;
        this.inventoryItems = new String[validCapacity];
        this.itemQuantities = new int[validCapacity];
        this.personnelRoster = new String[validCapacity];
        this.activeEvents = new DisasterEvent[MAX_ZONES];
        this.zoneSupplyMatrix = new int[MAX_ZONES][validCapacity];
        this.itemCount = 0;
        this.personnelCount = 0;
        this.eventCount = 0;
        totalManagerInstances++;
    }

    public static int getTotalManagerInstances() {
        return totalManagerInstances;
    }

    private String cleanText(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().replaceAll("\\s+", " ");
    }

    public void addInventory(String item, int quantity) {
        String normalizedItem = cleanText(item);
        if (normalizedItem.isEmpty()) {
            System.out.println("Error: Item name cannot be empty.");
            return;
        }
        if (quantity <= 0) {
            System.out.println("Error: Quantity must be greater than 0.");
            return;
        }

        for (int i = 0; i < this.itemCount; i++) {
            if (this.inventoryItems[i].equalsIgnoreCase(normalizedItem)) {
                this.itemQuantities[i] += quantity;
                System.out.println("Updated '" + this.inventoryItems[i] + "' stock to: " + this.itemQuantities[i]);
                return;
            }
        }

        if (this.itemCount < this.inventoryItems.length) {
            this.inventoryItems[this.itemCount] = normalizedItem;
            this.itemQuantities[this.itemCount] = quantity;
            this.itemCount++;
            System.out.println("Added inventory item: " + normalizedItem + " (" + quantity + ")");
        } else {
            System.out.println("Inventory storage is full!");
        }
    }

    public void allocateSuppliesToZone(int zoneIndex, String item, int units) {
        String normalizedItem = cleanText(item);
        if (zoneIndex < 0 || zoneIndex >= this.zoneSupplyMatrix.length) {
            System.out.println("Error: Invalid zone index (" + zoneIndex + "). Use 0 to " + (this.zoneSupplyMatrix.length - 1));
            return;
        }
        if (units <= 0) {
            System.out.println("Error: Units must be greater than 0.");
            return;
        }

        for (int i = 0; i < this.itemCount; i++) {
            if (this.inventoryItems[i].equalsIgnoreCase(normalizedItem)) {
                if (this.itemQuantities[i] >= units) {
                    this.itemQuantities[i] -= units;
                    this.zoneSupplyMatrix[zoneIndex][i] += units;
                    System.out.println("Allocated " + units + " of '" + this.inventoryItems[i] + "' to Zone " + zoneIndex);
                } else {
                    System.out.println("Insufficient stock for '" + this.inventoryItems[i] + "'. Available: " + this.itemQuantities[i]);
                }
                return;
            }
        }
        System.out.println("Error: Item '" + normalizedItem + "' not found.");
    }

    public void registerPersonnel(String personnelSummary) {
        String normalized = cleanText(personnelSummary);
        if (normalized.isEmpty()) {
            System.out.println("Error: Personnel details cannot be empty.");
            return;
        }

        for (int i = 0; i < this.personnelCount; i++) {
            if (this.personnelRoster[i].equalsIgnoreCase(normalized)) {
                System.out.println("Personnel already registered: " + this.personnelRoster[i]);
                return;
            }
        }

        if (this.personnelCount < this.personnelRoster.length) {
            this.personnelRoster[this.personnelCount] = normalized;
            this.personnelCount++;
            System.out.println("Registered personnel: " + normalized);
        } else {
            System.out.println("Personnel roster is full!");
        }
    }

    public void registerDisasterEvent(DisasterEvent event) {
        if (event == null) {
            return;
        }
        if (this.eventCount < this.activeEvents.length) {
            this.activeEvents[this.eventCount] = event;
            this.eventCount++;
            event.assessCrisis();
        } else {
            System.out.println("Maximum active disaster zones (" + MAX_ZONES + ") reached!");
        }
    }

    public String formatStatusReport() {
        StringBuilder report = new StringBuilder();
        report.append("\n--- LOGISTICS STATUS REPORT (25CSE0202 CHO) ---\n");
        report.append("Active Manager Instances: ").append(totalManagerInstances).append("\n");

        report.append("\nActive Disaster Events:\n");
        if (this.eventCount == 0) {
            report.append("  No active disaster zones logged.\n");
        } else {
            for (int i = 0; i < this.eventCount; i++) {
                report.append(String.format("  [Zone %d] %s | Location: %s | Severity: %d/5%n",
                        i, this.activeEvents[i].getEventId(), this.activeEvents[i].getLocation(),
                        this.activeEvents[i].getSeverityLevel()));
            }
        }

        report.append("\n1D Inventory Stock:\n");
        if (this.itemCount == 0) {
            report.append("  No items in inventory.\n");
        } else {
            for (int i = 0; i < this.itemCount; i++) {
                report.append(String.format("  %d. %-20s | Qty: %d%n", (i + 1), this.inventoryItems[i], this.itemQuantities[i]));
            }
        }

        report.append("\n2D Zone Allocation Matrix ([Zone][Item]):\n");
        for (int z = 0; z < this.zoneSupplyMatrix.length; z++) {
            report.append("  Zone ").append(z).append(": ");
            if (this.itemCount == 0) {
                report.append("No items tracked");
            } else {
                for (int i = 0; i < this.itemCount; i++) {
                    report.append(this.inventoryItems[i]).append("=").append(this.zoneSupplyMatrix[z][i]);
                    if (i < this.itemCount - 1) {
                        report.append(", ");
                    }
                }
            }
            report.append("\n");
        }

        report.append("\nPersonnel Roster:\n");
        if (this.personnelCount == 0) {
            report.append("  No personnel registered.\n");
        } else {
            for (int i = 0; i < this.personnelCount; i++) {
                report.append(String.format("  %d. %s%n", (i + 1), this.personnelRoster[i]));
            }
        }

        return report.toString();
    }

    public void displayLogisticsStatus() {
        System.out.print(formatStatusReport());
    }
}