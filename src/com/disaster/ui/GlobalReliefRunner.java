package com.disaster.ui;

import com.disaster.logistics.LogisticsManagerArray;
import com.disaster.model.DisasterEvent;
import com.disaster.model.EarthquakeZone;
import com.disaster.model.FieldVolunteer;
import com.disaster.model.FloodZone;
import com.disaster.model.MedicalOfficer;
import com.disaster.model.Person;
import java.util.Scanner;

public class GlobalReliefRunner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LogisticsManagerArray manager = new LogisticsManagerArray(20);
        boolean running = true;

        while (running) {
            System.out.println("\n=== GLOBAL DISASTER RESPONSE & LOGISTICS SIMULATOR ===");
            System.out.println("1. Log Disaster Event (Earthquake / Flood)");
            System.out.println("2. Add / Update Inventory");
            System.out.println("3. Allocate Supplies to Zone (0-3)");
            System.out.println("4. Register & Deploy Personnel (Medical Officer / Volunteer)");
            System.out.println("5. Display Full Status Report");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input. Enter a number from 1 to 6.");
                scanner.nextLine();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Type (1 = Earthquake, 2 = Flood): ");
                    int eventType = scanner.hasNextInt() ? scanner.nextInt() : 0;
                    scanner.nextLine();

                    System.out.print("Enter Event ID: ");
                    String eventId = scanner.nextLine();
                    System.out.print("Enter Location: ");
                    String location = scanner.nextLine();
                    System.out.print("Enter Severity (1-5): ");
                    int severity = scanner.hasNextInt() ? scanner.nextInt() : 1;
                    scanner.nextLine();

                    DisasterEvent event;
                    if (eventType == 1) {
                        System.out.print("Enter Richter Magnitude: ");
                        double mag = scanner.hasNextDouble() ? scanner.nextDouble() : 5.0;
                        scanner.nextLine();
                        event = new EarthquakeZone(eventId, location, severity, mag);
                    } else if (eventType == 2) {
                        System.out.print("Enter Water Level (meters): ");
                        double water = scanner.hasNextDouble() ? scanner.nextDouble() : 2.0;
                        scanner.nextLine();
                        event = new FloodZone(eventId, location, severity, water);
                    } else {
                        System.out.println("Invalid disaster type.");
                        break;
                    }
                    manager.registerDisasterEvent(event);
                    break;

                case 2:
                    System.out.print("Enter item name: ");
                    String item = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid quantity.");
                        scanner.nextLine();
                        break;
                    }
                    int qty = scanner.nextInt();
                    scanner.nextLine();
                    manager.addInventory(item, qty);
                    break;

                case 3:
                    System.out.print("Enter zone index (0-3): ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid zone index.");
                        scanner.nextLine();
                        break;
                    }
                    int zone = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter item name: ");
                    String allocItem = scanner.nextLine();
                    System.out.print("Enter units to allocate: ");
                    if (!scanner.hasNextInt()) {
                        System.out.println("Invalid units.");
                        scanner.nextLine();
                        break;
                    }
                    int units = scanner.nextInt();
                    scanner.nextLine();

                    manager.allocateSuppliesToZone(zone, allocItem, units);
                    break;

                case 4:
                    System.out.print("Role (1 = Medical Officer, 2 = Field Volunteer): ");
                    int role = scanner.hasNextInt() ? scanner.nextInt() : 0;
                    scanner.nextLine();

                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Personnel ID: ");
                    String id = scanner.nextLine();

                    Person person;
                    if (role == 1) {
                        System.out.print("Enter Specialization: ");
                        String spec = scanner.nextLine();
                        person = new MedicalOfficer(name, id, spec);
                    } else if (role == 2) {
                        System.out.print("Enter Assigned Task: ");
                        String task = scanner.nextLine();
                        person = new FieldVolunteer(name, id, task);
                    } else {
                        System.out.println("Invalid role selected.");
                        break;
                    }

                    System.out.print("Enter Target Zone Name: ");
                    String targetZone = scanner.nextLine();

                    person.performDuty();
                    person.deployToZone(targetZone);
                    manager.registerPersonnel(person.getEntitySummary());
                    break;

                case 5:
                    manager.displayLogisticsStatus();
                    break;

                case 6:
                    running = false;
                    System.out.println("Exiting simulator.");
                    break;

                default:
                    System.out.println("Invalid choice. Select 1-6.");
            }
        }

        scanner.close();
    }
}