package member4;

import com.disaster.model.DisasterEvent;
import com.disaster.model.EarthquakeZone;
import com.disaster.model.FieldVolunteer;
import com.disaster.model.FloodZone;
import com.disaster.model.MedicalOfficer;
import com.disaster.model.Person;
import com.gdrc.model.MedicalKit;
import exceptions.ContaminationException;
import exceptions.SafetyViolationException;
import exceptions.SignalLostException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import safety.DisasterSafetyService;
import safety.HazardDetector;
import safety.SafetyAuditLogger;

public class ConsoleMenu {
    private final Scanner scanner;
    private final StateSerializer serializer;
    private final ActionLogger logger;
    private final SafetyAuditLogger auditLogger;
    private final DisasterSafetyService safetyService;
    private ReliefState state;

    public ConsoleMenu(ReliefState initialState, StateSerializer serializer, ActionLogger logger) {
        this.scanner = new Scanner(System.in);
        this.state = initialState;
        this.serializer = serializer;
        this.logger = logger;

        int[][] defaultGrid = {
            {0, 1, 2, 3},
            {1, 4, 2, 0},
            {3, 2, 1, 4}
        };
        HazardDetector detector = new HazardDetector(defaultGrid);

        SafetyAuditLogger tempLogger = null;
        try {
            tempLogger = new SafetyAuditLogger("safety_audit.log", "jdbc:sqlite:safety_audit.db");
            Thread loggerThread = new Thread(tempLogger);
            loggerThread.setDaemon(true);
            loggerThread.start();
        } catch (Exception e) {
            System.out.println("Warning: SafetyAuditLogger init skipped (" + e.getMessage() + ")");
        }

        this.auditLogger = tempLogger;
        this.safetyService = new DisasterSafetyService(detector, this.auditLogger);
    }

    public void run() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1 -> handleAddDisasterEvent();
                case 2 -> handleAddResponder();
                case 3 -> handleUpdateSupply();
                case 4 -> handleAllocateSupplies();
                case 5 -> handleInspectMedicalKit();
                case 6 -> handleViewStatus();
                case 7 -> handleSaveState();
                case 8 -> handleLoadState();
                case 9 -> handleZoneSafetyInspection();
                case 10 -> handleCascadingContamination();
                case 11 -> handleDeployRescueVehicle();
                case 12 -> handleRepairRadioTower();
                case 13 -> handleQuarantineCipher();
                case 14 -> handlePriorityTriageRoster();
                case 0 -> {
                    logSafe("EXIT", "Console session ended");
                    if (auditLogger != null) {
                        auditLogger.shutdown();
                        try {
                            auditLogger.close();
                        } catch (Exception ignored) {
                        }
                    }
                    running = false;
                    System.out.println("Exiting console menu.");
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }

        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n==========================================");
        System.out.printf("%-28s %-12s%n", "GLOBAL DISASTER RESPONSE", "MEMBER 4 UI");
        System.out.println("==========================================");
        System.out.println("1. Register Disaster Event");
        System.out.println("2. Register Responder");
        System.out.println("3. Update Supply Quantity");
        System.out.println("4. Allocate Supplies to Zone");
        System.out.println("5. Inspect Medical Kit (GDRC Model)");
        System.out.println("6. View Center & Logistics Status");
        System.out.println("7. Save State (Serialization)");
        System.out.println("8. Load State (Deserialization)");
        System.out.println("9. Zone Safety Inspection (Safety & Exceptions)");
        System.out.println("10. Cascading Contamination Matrix");
        System.out.println("11. Deploy Rescue Vehicle (Ghost Queue Blackout)");
        System.out.println("12. Repair Radio Tower & Dispatch Queue");
        System.out.println("13. Quarantine Cipher (Victim ID & Roster)");
        System.out.println("14. Priority Triage & Supply Roster (TreeSet)");
        System.out.println("0. Exit");
    }

    private void handleAddDisasterEvent() {
        System.out.println("Select Event Type: 1. Earthquake  2. Flood");
        int type = readInt("Type: ");
        String eventId = readLine("Event ID (e.g., E-01): ");
        String location = readLine("Location (e.g., Tokyo): ");
        int severity = readInt("Severity Level (1-5): ");

        DisasterEvent event;
        if (type == 1) {
            double mag = readDouble("Richter Magnitude: ");
            event = new EarthquakeZone(eventId, location, severity, mag);
        } else {
            double water = readDouble("Water Level (meters): ");
            event = new FloodZone(eventId, location, severity, water);
        }

        state.addDisasterEvent(event);
        event.assessCrisis();
        logSafe("ADD_EVENT", eventId + " at " + location + " (Severity " + event.getSeverityLevel() + ")");
    }

    private void handleAddResponder() {
        System.out.println("Select Role: 1. Medical Officer  2. Field Volunteer");
        int role = readInt("Role: ");
        String name = readLine("Full Name: ");
        String personnelId = readLine("Personnel ID (e.g., P-101): ");

        Person person;
        if (role == 1) {
            String spec = readLine("Medical Specialization: ");
            person = new MedicalOfficer(name, personnelId, spec);
        } else {
            String task = readLine("Assigned Task: ");
            person = new FieldVolunteer(name, personnelId, task);
        }

        state.addPerson(person);
        person.performDuty();
        logSafe("ADD_RESPONDER", personnelId + " - " + name);
    }

    private void handleUpdateSupply() {
        String item = readLine("Enter supply item name: ");
        int qty = readInt("Enter quantity: ");
        state.updateSupply(item, qty);
        System.out.println("Updated supply: " + item + " -> " + qty);
        logSafe("UPDATE_SUPPLY", item + " = " + qty);
    }

    private void handleAllocateSupplies() {
        int zoneIndex = readInt("Enter zone index (0-3): ");
        String item = readLine("Enter supply item name: ");
        int units = readInt("Enter units to allocate: ");
        if (state.getLogisticsManager() != null) {
            state.getLogisticsManager().allocateSuppliesToZone(zoneIndex, item, units);
        }
        logSafe("ALLOCATE_SUPPLY", "Zone " + zoneIndex + " <- " + units + " of " + item);
    }

    private void handleInspectMedicalKit() {
        String id = readLine("Kit ID (e.g., MK-01): ");
        double weight = readDouble("Unit Weight (kg): ");
        System.out.println("Contains Antibiotics? 1. Yes  2. No");
        boolean antibiotics = (readInt("Choice: ") == 1);

        MedicalKit kit = new MedicalKit(id, weight, antibiotics);
        System.out.println("Kit Created: [" + kit.getResourceId() + "] " + kit.getName()
                + " | Antibiotics: " + kit.hasAntibiotics()
                + " | Storage: " + kit.getStorageInstructions());
        logSafe("INSPECT_KIT", id + " (Antibiotics=" + antibiotics + ")");
    }

    private void handleViewStatus() {
        System.out.println("\n--- Relief Center Status ---");
        System.out.printf("%-20s %s%n", "Center Name:", state.getCenterName());
        System.out.printf("%-20s %s%n", "Session Note:", state.getSessionNote());

        System.out.println("\nRegistered Disaster Events (" + state.getDisasterEvents().size() + "):");
        for (DisasterEvent e : state.getDisasterEvents()) {
            e.assessCrisis();
        }

        System.out.println("\nRegistered Personnel (" + state.getPersonnelList().size() + "):");
        for (Person p : state.getPersonnelList()) {
            System.out.println("  " + p.getEntitySummary());
        }

        System.out.println("\nActive Vehicle Deployments (" + state.getActiveDeployments().size() + "):");
        for (RescueVehicle v : state.getActiveDeployments()) {
            System.out.println("  " + v);
        }

        System.out.println("\nQuarantine Locked IDs (" + state.getQuarantineRoster().size() + "):");
        for (Map.Entry<String, String> entry : state.getQuarantineRoster().entrySet()) {
            System.out.println("  Locked ID: " + entry.getKey() + " | Zone: " + entry.getValue());
        }

        System.out.println("\nPriority Triage Roster (" + state.getTriageRoster().size() + "):");
        for (PriorityZoneEntry pze : state.getTriageRoster()) {
            System.out.println("  " + pze);
        }

        System.out.println("\nMap Supply Inventory:");
        if (state.getSupplyInventory().isEmpty()) {
            System.out.println("  (No supplies recorded)");
        } else {
            for (Map.Entry<String, Integer> entry : state.getSupplyInventory().entrySet()) {
                System.out.printf("  %-20s %d%n", entry.getKey(), entry.getValue());
            }
        }

        if (state.getLogisticsManager() != null) {
            state.getLogisticsManager().displayLogisticsStatus();
        }
        logSafe("VIEW_STATUS", "Displayed full center and logistics status");
    }

    private void handleSaveState() {
        String path = readLine("Enter save file path (e.g., relief_state.ser): ");
        try {
            serializer.saveState(state, path);
            System.out.println("State saved to " + path);
            logSafe("SAVE_STATE", "Saved to " + path);
        } catch (IOException e) {
            System.out.println("Error saving state: " + e.getMessage());
        }
    }

    private void handleLoadState() {
        String path = readLine("Enter load file path (e.g., relief_state.ser): ");
        try {
            ReliefState loaded = serializer.loadState(path);
            loaded.setSessionNote("Restored from " + path);
            this.state = loaded;
            System.out.println("State loaded from " + path);
            logSafe("LOAD_STATE", "Loaded from " + path);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state: " + e.getMessage());
        }
    }

    private void handleZoneSafetyInspection() {
        int row = readInt("Enter grid row (0-2): ");
        int col = readInt("Enter grid col (0-3): ");
        System.out.println("Contaminated? 1. Yes  2. No");
        boolean contaminated = (readInt("Choice: ") == 1);
        System.out.println("Authorized Responder? 1. Yes  2. No");
        boolean authorized = (readInt("Choice: ") == 1);
        System.out.println("Protective Gear Worn? 1. Yes  2. No");
        boolean gear = (readInt("Choice: ") == 1);

        try {
            safetyService.inspectZone(row, col, contaminated, authorized, gear);
            logSafe("SAFETY_INSPECT_OK", "Zone (" + row + "," + col + ") passed");
        } catch (ContaminationException | SignalLostException | SafetyViolationException e) {
            System.out.println("Safety Alert: " + e.getMessage());
            logSafe("SAFETY_ALERT", "Zone (" + row + "," + col + ") -> " + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid coordinates: " + e.getMessage());
        }
    }

    private void handleCascadingContamination() {
        String spillMsg = state.triggerCascadingSpill();
        System.out.println(spillMsg);
        logSafe("CASCADING_SPILL", spillMsg);

        int row = readInt("Enter rescue team row to traverse (0-3): ");
        int col = readInt("Enter rescue team col to traverse (0-3): ");

        try {
            state.traverseZone(row, col);
            System.out.println("Traversed (" + row + "," + col + ") safely. Water Rations: "
                    + state.getSupplyInventory().getOrDefault("Water Rations", 0));
            logSafe("TRAVERSE_OK", "Traversed (" + row + "," + col + ")");
        } catch (ContaminationException e) {
            System.out.println("ContaminationException: " + e.getMessage());
            logSafe("CONTAMINATION_HIT", "At (" + row + "," + col + ") -> " + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid coordinates: " + e.getMessage());
        }
    }

    private void handleDeployRescueVehicle() {
        String vehicleId = readLine("Vehicle ID (e.g., RV-01): ");
        String vehicleType = readLine("Vehicle Type (e.g., Fire Truck / Ambulance): ");
        String targetZone = readLine("Target Zone: ");
        int severity = readInt("Target Zone Severity (1-5, 5 = Critical): ");

        RescueVehicle vehicle = new RescueVehicle(vehicleId, vehicleType, targetZone, severity);

        try {
            state.deployVehicle(vehicle);
            System.out.println("Deployed to active state: " + vehicle);
            logSafe("DEPLOY_VEHICLE", vehicle.toString());
        } catch (SignalLostException e) {
            System.out.println("SignalLostException: " + e.getMessage());
            try {
                state.saveVehicleToBlackoutFile(vehicle);
                System.out.println("Removed from active state and written to " + ReliefState.BLACKOUT_FILE);
                logSafe("BLACKOUT_QUEUED", vehicleId + " -> " + ReliefState.BLACKOUT_FILE);
            } catch (IOException ioEx) {
                System.out.println("Failed to write blackout queue file: " + ioEx.getMessage());
            }
        }
    }

    private void handleRepairRadioTower() {
        System.out.println("Repairing radio tower and loading trapped deployments from " + ReliefState.BLACKOUT_FILE + "...");
        try {
            List<RescueVehicle> dispatched = state.repairTowerAndDispatch();
            System.out.println("Radio tower online! Active deployments now (" + dispatched.size() + "):");
            for (RescueVehicle v : dispatched) {
                System.out.println("  Dispatched: " + v);
            }
            logSafe("TOWER_REPAIRED", "Dispatched " + dispatched.size() + " active vehicle(s)");
        } catch (IOException e) {
            System.out.println("Error restoring blackout queue: " + e.getMessage());
        }
    }

    private void handleQuarantineCipher() {
        System.out.println("1. Rescue CivilianVictim (Lock ID)   2. MedicalOfficer View Roster (Decrypt IDs)");
        int op = readInt("Choice: ");

        if (op == 1) {
            String victimId = readLine("Enter CivilianVictim ID to rescue: ");
            String bioZone = readLine("Enter Bio-Hazard Zone: ");
            String lockedId = state.rescueCivilianVictim(victimId, bioZone);
            System.out.println("Rescued & anonymized. Locked ID stored: " + lockedId);
            logSafe("QUARANTINE_LOCK", "Locked ID " + lockedId + " in " + bioZone);
        } else {
            String officerName = readLine("Enter MedicalOfficer Name: ");
            String officerId = readLine("Enter MedicalOfficer ID: ");
            String spec = readLine("Enter Medical Specialization: ");
            MedicalOfficer officer = new MedicalOfficer(officerName, officerId, spec);
            officer.performDuty();

            System.out.println("\n--- Decrypted Quarantine Roster for Dr. " + officer.getName() + " ---");
            if (state.getQuarantineRoster().isEmpty()) {
                System.out.println("  (No civilians in quarantine roster)");
            } else {
                for (Map.Entry<String, String> entry : state.getQuarantineRoster().entrySet()) {
                    String decryptedId = QuarantineCipher.decryptId(entry.getKey());
                    System.out.println("  Decrypted ID: " + decryptedId
                            + " | Locked ID: " + entry.getKey()
                            + " | Zone: " + entry.getValue());
                }
            }
            logSafe("QUARANTINE_DECRYPT", "Viewed by MedicalOfficer " + officerId);
        }
    }

    private void handlePriorityTriageRoster() {
        System.out.println("1. Add/Update Priority Zone   2. View Sorted Roster   3. Purge Resolved Zones (0 Deficit)");
        int op = readInt("Choice: ");

        if (op == 1) {
            String zoneId = readLine("Zone ID (e.g., Z-NORTH): ");
            int severity = readInt("Severity (1-5): ");
            int deficit = readInt("Supply Deficit (0 if fully supplied): ");
            state.addOrUpdateTriageZone(zoneId, severity, deficit);
            System.out.println("Added/updated " + zoneId + " in TreeSet triage roster.");
            logSafe("TRIAGE_ADD", zoneId + " sev=" + severity + " def=" + deficit);
        } else if (op == 2) {
            System.out.println("\n--- Sorted Priority Triage Roster (Highest Priority First) ---");
            if (state.getTriageRoster().isEmpty()) {
                System.out.println("  (Roster empty)");
            } else {
                int rank = 1;
                for (PriorityZoneEntry entry : state.getTriageRoster()) {
                    System.out.println("  " + rank++ + ". " + entry);
                }
            }
            logSafe("TRIAGE_VIEW", "Displayed sorted triage roster");
        } else {
            int maxSev = readInt("Purge resolved (0 deficit) zones with severity up to (1-5): ");
            int removed = state.purgeResolvedZones(maxSev);
            System.out.println("Purged " + removed + " resolved zone(s) safely via Iterator.");
            logSafe("TRIAGE_PURGE", "Removed " + removed + " resolved zone(s)");
        }
    }

    private void logSafe(String action, String details) {
        try {
            logger.logAction(action, details);
        } catch (IOException e) {
            System.out.println("Warning: could not write log (" + e.getMessage() + ")");
        }
    }

    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextInt()) {
                int value = scanner.nextInt();
                scanner.nextLine();
                return value;
            }
            System.out.println("Please enter a valid whole number.");
            scanner.nextLine();
        }
    }

    private double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                double value = scanner.nextDouble();
                scanner.nextLine();
                return value;
            }
            System.out.println("Please enter a valid decimal number.");
            scanner.nextLine();
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}