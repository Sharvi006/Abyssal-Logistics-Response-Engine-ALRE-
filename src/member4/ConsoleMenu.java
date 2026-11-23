package member4;

import com.disaster.model.DisasterEvent;
import com.disaster.model.EarthquakeZone;
import com.disaster.model.FieldVolunteer;
import com.disaster.model.FloodZone;
import com.disaster.model.MedicalOfficer;
import com.disaster.model.Person;
import com.gdrc.model.MedicalKit;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner scanner;
    private final StateSerializer serializer;
    private final ActionLogger logger;
    private ReliefState state;

    public ConsoleMenu(ReliefState initialState, StateSerializer serializer, ActionLogger logger) {
        this.scanner = new Scanner(System.in);
        this.state = initialState;
        this.serializer = serializer;
        this.logger = logger;
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
                case 0 -> {
                    logSafe("EXIT", "Console session ended");
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