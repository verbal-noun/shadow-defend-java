import bagel.Input;
import bagel.util.Point;
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
    private List<Event> events;
    private Event currEvent;

    public int getSpawnedSlicers() {
        return spawnedSlicers;
    }

    public int getSlicerCount() {
        return slicers.size();
    }

    //Constructor
    public Wave(List<Point> polyline) {
        // Load polyline points from the map to the wave
        this.polyline = polyline;
        this.slicers = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    /* A method to initialise the wave of 5 slicers */
    public void spawnSlicer() {
        slicers.add(new Slicer(polyline));
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
            if(currEvent.getEventType().equals(DELAY)) {
                currEvent.updateEvent();
            }
            // If it's a spawn event, add appropriate type of slicer when time
            if(currEvent.getEventType().equals(SPAWN)) {
                currEvent.updateEvent();
                // When it's time to add a new slicer

            }
        }
}

    // Add new in the wave event list
    public void addEvent(Event event) {
        events.add(event);
    }

    // Load the next event in the event list as current event
    private void loadEvent() {
        // Process event appropriately
        currEvent = events.get(TOP);
    }
}
