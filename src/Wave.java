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
    //An attribute to determine when the wave has started and finished.
    private boolean waveStarted = false;
    private int spawnedSlicers = 0;

    //The slicers and the path of the wave
    private final List<Slicer> slicers;
    private List<Point> polyline;
    private List<String> events;
    private SpawnEvent spawnEvent;
    private Event delayEvent;
    private String currEvent;
    private boolean isFinished;

    public int getSpawnedSlicers() {
        return spawnedSlicers;
    }

    public int getSlicerCount() {
        return slicers.size();
    }

    public boolean isFinished() { return isFinished; }

    //Constructor
    public Wave(List<Point> polyline) {
        // Load polyline points from the map to the wave
        this.polyline = polyline;
        this.slicers = new ArrayList<>();
        this.events = new ArrayList<>();
        this.isFinished = false;
    }

    /* A method to initialise the wave of 5 slicers */
    public void spawnSlicer(Slicer newSlicer) {
        slicers.add(newSlicer);
        spawnedSlicers += 1;
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
        /* Set wave is estimated to finish at the end of update. This will be changed if any of
         * the slicers are still active.
         */
        for (int i = slicers.size() - 1; i >= 0; i--) {
            Slicer s = slicers.get(i);
            s.update(input);
            if (s.isFinished()) {
                slicers.remove(i);
            }
        }

        // Update current event of the wave
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
                        spawnSlicer(spawnEvent.generateSlicer(polyline));
                    }
                }
            }
            //
            if(events.size()==0 && slicers.size()==0) {
                if(currEvent.equals(DELAY) && !delayEvent.eventStatus()) {
                    isFinished = true;
                    System.out.println(true);
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
}
