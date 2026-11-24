package member4;

import com.disaster.logistics.LogisticsManagerArray;
import com.disaster.model.DisasterEvent;
import com.disaster.model.Person;
import exceptions.ContaminationException;
import exceptions.SignalLostException;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

public class ReliefState implements Serializable {
    private static final long serialVersionUID = 10L;
    public static final String BLACKOUT_FILE = "blackout_queue.dat";

    private static class TriageComparator implements Comparator<PriorityZoneEntry>, Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(PriorityZoneEntry a, PriorityZoneEntry b) {
            if (a.getZoneId().equalsIgnoreCase(b.getZoneId())) {
                return 0;
            }
            int bySeverity = Integer.compare(b.getSeverity(), a.getSeverity());
            if (bySeverity != 0) {
                return bySeverity;
            }
            int byDeficit = Integer.compare(b.getSupplyDeficit(), a.getSupplyDeficit());
            if (byDeficit != 0) {
                return byDeficit;
            }
            return a.getZoneId().compareToIgnoreCase(b.getZoneId());
        }
    }

    private static final Comparator<PriorityZoneEntry> TRIAGE_COMPARATOR = new TriageComparator();

    private String centerName;
    private int logisticsCapacity;
    private List<String> activeZones;
    private Map<String, Integer> supplyInventory;
    private int[][] contaminationGrid;
    private List<RescueVehicle> activeDeployments;
    private Map<String, String> quarantineRoster;
    private TreeSet<PriorityZoneEntry> triageRoster;

    private transient LogisticsManagerArray logisticsManager;
    private transient List<DisasterEvent> disasterEvents;
    private transient List<Person> personnelList;
    private transient String sessionNote;

    public ReliefState(String centerName, int logisticsCapacity) {
        this.centerName = centerName;
        this.logisticsCapacity = logisticsCapacity;
        this.activeZones = new ArrayList<>();
        this.supplyInventory = new HashMap<>();
        this.supplyInventory.put("Water Rations", 100);
        this.supplyInventory.put("Purification Tablets", 40);
        this.logisticsManager = new LogisticsManagerArray(logisticsCapacity);
        this.contaminationGrid = new int[4][4];
        this.activeDeployments = new ArrayList<>();
        this.quarantineRoster = new LinkedHashMap<>();
        this.triageRoster = new TreeSet<>(TRIAGE_COMPARATOR);
        this.disasterEvents = new ArrayList<>();
        this.personnelList = new ArrayList<>();
        this.sessionNote = "Active Console Session";
    }

    private void ensureTransientLists() {
        if (this.logisticsManager == null) {
            int cap = (this.logisticsCapacity > 0) ? this.logisticsCapacity : 4;
            this.logisticsManager = new LogisticsManagerArray(cap);
        }
        if (this.disasterEvents == null) {
            this.disasterEvents = new ArrayList<>();
        }
        if (this.personnelList == null) {
            this.personnelList = new ArrayList<>();
        }
        if (this.contaminationGrid == null) {
            this.contaminationGrid = new int[4][4];
        }
        if (this.activeDeployments == null) {
            this.activeDeployments = new ArrayList<>();
        }
        if (this.quarantineRoster == null) {
            this.quarantineRoster = new LinkedHashMap<>();
        }
        if (this.triageRoster == null) {
            this.triageRoster = new TreeSet<>(TRIAGE_COMPARATOR);
        }
    }

    public String getCenterName() { return centerName; }
    public void setCenterName(String centerName) { this.centerName = centerName; }

    public List<String> getActiveZones() { return activeZones; }
    public Map<String, Integer> getSupplyInventory() { return supplyInventory; }

    public LogisticsManagerArray getLogisticsManager() {
        ensureTransientLists();
        return logisticsManager;
    }

    public List<RescueVehicle> getActiveDeployments() {
        ensureTransientLists();
        return activeDeployments;
    }

    public Map<String, String> getQuarantineRoster() {
        ensureTransientLists();
        return quarantineRoster;
    }

    public TreeSet<PriorityZoneEntry> getTriageRoster() {
        ensureTransientLists();
        return triageRoster;
    }

    public void addOrUpdateTriageZone(String zoneId, int severity, int supplyDeficit) {
        ensureTransientLists();
        Iterator<PriorityZoneEntry> it = triageRoster.iterator();
        while (it.hasNext()) {
            PriorityZoneEntry entry = it.next();
            if (entry.getZoneId().equalsIgnoreCase(zoneId)) {
                it.remove();
            }
        }
        triageRoster.add(new PriorityZoneEntry(zoneId, severity, supplyDeficit));
        addZone(zoneId);
    }

    public int purgeResolvedZones(int maxSeverityToPurge) {
        ensureTransientLists();
        int removedCount = 0;
        Iterator<PriorityZoneEntry> it = triageRoster.iterator();
        while (it.hasNext()) {
            PriorityZoneEntry entry = it.next();
            if (entry.getSeverity() <= maxSeverityToPurge && entry.getSupplyDeficit() <= 0) {
                it.remove();
                removedCount++;
            }
        }
        return removedCount;
    }

    public int[][] getContaminationGrid() {
        ensureTransientLists();
        return contaminationGrid;
    }

    public List<DisasterEvent> getDisasterEvents() {
        ensureTransientLists();
        return disasterEvents;
    }

    public List<Person> getPersonnelList() {
        ensureTransientLists();
        return personnelList;
    }

    public void addZone(String zone) {
        if (zone != null && !zone.isEmpty() && !activeZones.contains(zone)) {
            activeZones.add(zone);
        }
    }

    public void addDisasterEvent(DisasterEvent event) {
        if (event != null) {
            ensureTransientLists();
            disasterEvents.add(event);
            addZone(event.getEventId() + " - " + event.getLocation());
        }
    }

    public void addPerson(Person person) {
        if (person != null) {
            ensureTransientLists();
            personnelList.add(person);
            if (logisticsManager != null) {
                logisticsManager.registerPersonnel(person.getName());
            }
        }
    }

    public String rescueCivilianVictim(String rawVictimId, String bioZone) {
        ensureTransientLists();
        String scrambled = QuarantineCipher.scrambleId(rawVictimId);
        quarantineRoster.put(scrambled, bioZone);
        addZone(bioZone);
        return scrambled;
    }

    public void updateSupply(String item, int quantity) {
        if (item != null && !item.isEmpty()) {
            ensureTransientLists();
            supplyInventory.put(item, quantity);
            if (logisticsManager != null && quantity > 0) {
                logisticsManager.addInventory(item, quantity);
            }
        }
    }

    public void deployVehicle(RescueVehicle vehicle) throws SignalLostException {
        ensureTransientLists();
        if (vehicle.getZoneSeverity() >= 5) {
            activeDeployments.remove(vehicle);
            throw new SignalLostException(
                    "Radio blackout! Zone severity " + vehicle.getZoneSeverity()
                    + "/5 reached critical threshold for " + vehicle.getVehicleId()
            );
        }
        activeDeployments.add(vehicle);
        addZone(vehicle.getTargetZone());
    }

    public void saveVehicleToBlackoutFile(RescueVehicle vehicle) throws IOException {
        ensureTransientLists();
        activeDeployments.remove(vehicle);

        List<RescueVehicle> existing = readBlackoutVehiclesFromFile();
        existing.add(vehicle);

        try (FileOutputStream fos = new FileOutputStream(BLACKOUT_FILE);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            for (RescueVehicle v : existing) {
                oos.writeObject(v);
            }
        }
    }

    private List<RescueVehicle> readBlackoutVehiclesFromFile() throws IOException {
        List<RescueVehicle> list = new ArrayList<>();
        File file = new File(BLACKOUT_FILE);
        if (!file.exists() || file.length() == 0) {
            return list;
        }

        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            while (true) {
                try {
                    Object obj = ois.readObject();
                    if (obj instanceof RescueVehicle rv) {
                        list.add(rv);
                    }
                } catch (EOFException eof) {
                    break;
                } catch (ClassNotFoundException cnf) {
                    throw new IOException("Corrupted blackout object: " + cnf.getMessage(), cnf);
                }
            }
        }
        return list;
    }

    public List<RescueVehicle> repairTowerAndDispatch() throws IOException {
        ensureTransientLists();
        List<RescueVehicle> dispatchQueue = new ArrayList<>(readBlackoutVehiclesFromFile());

        for (RescueVehicle v : dispatchQueue) {
            activeDeployments.add(v);
            addZone(v.getTargetZone());
        }

        File file = new File(BLACKOUT_FILE);
        if (file.exists()) {
            file.delete();
        }
        return new ArrayList<>(activeDeployments);
    }

    public String triggerCascadingSpill() {
        ensureTransientLists();
        Random rand = new Random();
        int r = rand.nextInt(contaminationGrid.length);
        int c = rand.nextInt(contaminationGrid[r].length);
        contaminationGrid[r][c] = 3;
        return "Toxic spill (3) triggered at (" + r + "," + c + ")";
    }

    public void traverseZone(int row, int col) throws ContaminationException {
        ensureTransientLists();
        if (row < 0 || row >= contaminationGrid.length || col < 0 || col >= contaminationGrid[row].length) {
            throw new IndexOutOfBoundsException("Coordinates out of 4x4 grid bounds");
        }

        if (contaminationGrid[row][col] == 3) {
            int currentWater = supplyInventory.getOrDefault("Water Rations", 0);
            int reducedWater = currentWater / 2;
            supplyInventory.put("Water Rations", reducedWater);

            int tablets = supplyInventory.getOrDefault("Purification Tablets", 0);
            throw new ContaminationException(
                    "Toxic spill at (" + row + "," + col + ")! Water Rations cut to "
                    + reducedWater + ". Rely on Purification Tablets (" + tablets + " available)."
            );
        }
    }

    public String getSessionNote() { return sessionNote; }
    public void setSessionNote(String sessionNote) { this.sessionNote = sessionNote; }
}