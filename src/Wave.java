import bagel.Input;
import bagel.util.Point;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;


public class Wave {
    // Different type of events
    private static final String SPAWN = "spawn";
    private static final String DELAY = "delay";
    private static final int TOP = 0;
    // Type of slicers possible in a wave
    private static final String SLICER = "slicer";
    private static final String SUPER_SLICER = "superslicer";
    private static final String MEGA_SLICER = "megaslicer";
    private static final String APEX_SLICER = "apexslicer";
    private static final String IMAGE_FILE = "res/images/%s.png";
    //An attribute to determine when the wave has started and finished.
    private boolean waveStarted = false;
    private int spawnedSlicers = 0;

    //The slicers and the path of the wave
    private final List<Slicer> slicers;
    private final List<SuperSlicer> superSlicers;
    private final List<MegaSlicer> megaSlicers;
    private final List<ApexSlicer> apexSlicers;
    private List<Point> polyline;
    private List<String> events;
    private SpawnEvent spawnEvent;
    private Event delayEvent;
    private String currEvent;
    private boolean isFinished;
    private Player player;

    public int getSpawnedSlicers() {
        return spawnedSlicers;
    }

    public int getSlicerCount() {
        return slicers.size();
    }

    public boolean isFinished() { return isFinished; }

    //Constructor
    public Wave(List<Point> polyline, Player player) {
        // Load polyline points from the map to the wave
        this.polyline = polyline;
        this.slicers = new ArrayList<>();
        this.superSlicers = new ArrayList<>();
        this.megaSlicers = new ArrayList<>();
        this.apexSlicers = new ArrayList<>();
        this.events = new ArrayList<>();
        this.isFinished = false;
        this.player = player;
    }

    /* A method to initialise the wave of 5 slicers */
    public void spawnSlicer(String slicerType) {
        String type = String.format(IMAGE_FILE, slicerType);
        switch (slicerType) {
            case SLICER:
                slicers.add(new Slicer(polyline, type));
                break;
            // Add a super slicer
            case SUPER_SLICER:
                superSlicers.add(new SuperSlicer(polyline, type));
                break;
            // Add a mega slicer
            case MEGA_SLICER:
                megaSlicers.add(new MegaSlicer(polyline, type));
                break;
            // Add an apex slicer
            default:
                apexSlicers.add(new ApexSlicer(polyline, type));
                break;
        }
    }

    /* A method to start the wave when 'S' is pressed */
    public void startWave() {
        // Condition to ensure the wave activates only once
        if(!this.waveStarted) {
            this.waveStarted = true;
            // Load the first event
            loadEvent();
        }
    }

    public boolean waveStatus() { return waveStarted; }

    /* A method to update the wave's current status */
    public void updateWave(Input input) {
        // Update the state of wave's regular slicers
        updateSlicer(slicers, input);
        // Update the state of wave's super slicers
        updateSlicer(superSlicers, input);
        // Update the state of wave's mega slicers
        updateSlicer(megaSlicers, input);
        // Update the state of wave's super slicers
        updateSlicer(apexSlicers, input);

        // Update the current event of the wave
        if(waveStarted) {
            // If current event is a delay event then pause
            if(currEvent.equals(DELAY)) {
                if(!delayEvent.eventStatus()) {
                    // load the next event
                    loadEvent();
                } else {
                    // update current event
                    delayEvent.updateEvent();
                }
            }
            // If it's a spawn event, add appropriate type of slicer when time
            if(currEvent.equals(SPAWN)) {
                if(!spawnEvent.eventStatus()) {
                    // load the next event
                    loadEvent();
                } else {
                    // update current event
                    spawnEvent.updateEvent();
                    // When it's time to add a new slicer
                    if(spawnEvent.isAddSlicer()) {
                        // Add the appropriate type of slicer
                        spawnSlicer(spawnEvent.generateSlicer());
                    }
                }
            }
            // if events list and slicer list is empty
            if(events.size()==0 && slicers.size()==0 &&
                    superSlicers.size()==0 && megaSlicers.size() == 0 && apexSlicers.size() == 0) {
                // Check if current event is also finished or not
                if(currEvent.equals(DELAY) && !delayEvent.eventStatus()) {
                    isFinished = true;
                } else if(currEvent.equals(SPAWN) && !spawnEvent.eventStatus()) {
                    isFinished = true;
                }
            }
        }
}

    // Add new in the wave event list
    public void addEvent(String event) {
        events.add(event);
    }

    // Load the next event in the event list as current event
    private void loadEvent() {
        // If there are more events to process
        if(events.size() > 0) {
            // Process event appropriately
            String[] eventInfo = events.get(TOP).split(",");
            currEvent = eventInfo[TOP];
            if(currEvent.equals(SPAWN)) {
                spawnEvent = new SpawnEvent(eventInfo);
            } else {
                delayEvent = new Event(eventInfo);
                System.out.println("delay event");
            }
            // Remove old event from the list
            events.remove(TOP);
        }
    }

    // Methods to access the existing slicers of a wave
    public List<Slicer> getSlicers() { return slicers; }
    public List<SuperSlicer> getSuperSlicers() { return superSlicers; }
    public List<MegaSlicer> getMegaSlicers() { return megaSlicers; }
    public List<ApexSlicer> getApexSlicers() { return apexSlicers; }

    // Method to update different kinds of slicers
    private <T extends Slicer> void updateSlicer(List<T> slicerList, Input input) {
        // Update the state of wave's regular slicers
        for (int i = slicerList.size() - 1; i >= 0; i--) {
            T s = slicerList.get(i);
            s.update(input);
            if (s.isFinished()) {
                slicerList.remove(i);
            }
        }
    }

}
