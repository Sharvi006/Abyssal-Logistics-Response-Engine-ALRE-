package member4;

import com.disaster.logistics.LogisticsManagerArray;
import com.disaster.model.DisasterEvent;
import com.disaster.model.Person;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReliefState implements Serializable {
    private static final long serialVersionUID = 4L;

    private String centerName;
    private List<String> activeZones;
    private Map<String, Integer> supplyInventory;
    private LogisticsManagerArray logisticsManager;

    private transient List<DisasterEvent> disasterEvents;
    private transient List<Person> personnelList;
    private transient String sessionNote;

    public ReliefState(String centerName, int logisticsCapacity) {
        this.centerName = centerName;
        this.activeZones = new ArrayList<>();
        this.supplyInventory = new HashMap<>();
        this.logisticsManager = new LogisticsManagerArray(logisticsCapacity);
        this.disasterEvents = new ArrayList<>();
        this.personnelList = new ArrayList<>();
        this.sessionNote = "Active Console Session";
    }

    private void ensureTransientLists() {
        if (this.disasterEvents == null) {
            this.disasterEvents = new ArrayList<>();
        }
        if (this.personnelList == null) {
            this.personnelList = new ArrayList<>();
        }
    }

    public String getCenterName() { return centerName; }
    public void setCenterName(String centerName) { this.centerName = centerName; }

    public List<String> getActiveZones() { return activeZones; }
    public Map<String, Integer> getSupplyInventory() { return supplyInventory; }
    public LogisticsManagerArray getLogisticsManager() { return logisticsManager; }

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

    public void updateSupply(String item, int quantity) {
        if (item != null && !item.isEmpty()) {
            supplyInventory.put(item, quantity);
            if (logisticsManager != null && quantity > 0) {
                logisticsManager.addInventory(item, quantity);
            }
        }
    }

    public String getSessionNote() { return sessionNote; }
    public void setSessionNote(String sessionNote) { this.sessionNote = sessionNote; }
}