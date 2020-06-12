import bagel.Input;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.List;


/**
 * The type Wave.
 */
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
    //The enemy types of the wave
    private final List<Slicer> slicers;
    private final List<SuperSlicer> superSlicers;
    private final List<MegaSlicer> megaSlicers;
    private final List<ApexSlicer> apexSlicers;
    // Map of the wave
    private List<Point> polyline;
    // Attributes for event of the wave
    private List<String> events;
    private SpawnEvent spawnEvent;
    private DelayEvent delayEvent;
    private String currEvent;
    private boolean isFinished;
    private Player player;

    /**
     * Instantiates a new Wave.
     *
     * @param polyline the polyline
     * @param player   the player
     */
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

    /**
     * Is finished boolean.
     *
     * @return the boolean
     */
    public boolean isFinished() { return isFinished; }

    /**
     * Spawn slicer.
     *
     * @param slicerType the type of slicer needs to be spawned.
     */
    /* A method to initialise the wave of 5 slicers */
    public void spawnSlicer(String slicerType) {
        String type = String.format(IMAGE_FILE, slicerType);
        // Add a new slicer to the appropriate enemy list
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

    /**
     * Start wave.
     */
    /* A method to start the wave when 'S' is pressed */
    public void startWave() {
        // Condition to ensure the wave activates only once
        if(!this.waveStarted) {
            this.waveStarted = true;
            // Load the first event
            loadEvent();
        }
    }

    /**
     * Returns the status of the current wave
     * This status notifies whether the wave is ongoing or not.
     *
     * @return the boolean which determines the current status.
     */
    public boolean waveStatus() { return waveStarted; }

    /**
     * Update the state of the wave.
     *
     * @param input - input given by the player
     */
    /* A method to update the wave's current status */
    public void updateWave(Input input) {
        // Update the state of wave's regular slicers
        updateSlicer(slicers);
        // Update the state of wave's super slicers
        updateSlicer(superSlicers);
        // Update the state of wave's mega slicers
        updateSlicer(megaSlicers);
        // Update the state of wave's super slicers
        updateSlicer(apexSlicers);

        // Update the current event of the wave
        if(waveStarted) {
            // If current event is a delay event then pause
            if(currEvent.equals(DELAY)) {
                if(delayEvent.eventStatus()) {
                    // load the next event
                    loadEvent();
                } else {
                    // update current event
                    delayEvent.updateEvent();
                }
            }
            // If it's a spawn event, add appropriate type of slicer when time
            if(currEvent.equals(SPAWN)) {
                if(spawnEvent.eventStatus()) {
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
                if(currEvent.equals(DELAY) && delayEvent.eventStatus()) {
                    isFinished = true;
                } else if(currEvent.equals(SPAWN) && spawnEvent.eventStatus()) {
                    isFinished = true;
                }
            }
        }
}

    /**
     * Add event to the event list of the wave.
     *
     * @param event - the event needs ot be added to the event list
     */
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
                delayEvent = new DelayEvent(eventInfo);
                System.out.println("delay event");
            }
            // Remove old event from the list
            events.remove(TOP);
        }
    }

    /**
     * Gets a list of present in the wave slicers.
     *
     * @return the slicers
     */
    public List<Slicer> getSlicers() { return slicers; }

    /**
     * Gets super slicers of the wave.
     *
     * @return the list of super slicers
     */
    public List<SuperSlicer> getSuperSlicers() { return superSlicers; }

    /**
     * Gets mega slicers.
     *
     * @return the list 0f mega slicers
     */
    public List<MegaSlicer> getMegaSlicers() { return megaSlicers; }

    /**
     * Gets apex slicers.
     *
     * @return the list of apex slicers
     */
    public List<ApexSlicer> getApexSlicers() { return apexSlicers; }

    /**
     * Method to update the list of the given type of slicer.
     * This method is also used to move its position and render int the map
     *
     * @param <T> - Defines the type of enemy
     * @param slicerList - a list containing the slicers
     */
    private <T extends Slicer> void updateSlicer(List<T> slicerList) {
        // Update the state of wave's regular slicers
        for (int i = slicerList.size() - 1; i >= 0; i--) {
            T s = slicerList.get(i);
            // Update the state
            s.update();
            // If slicer is finished, remove from the list
            if (s.isFinished()) {
                slicerList.remove(i);
                // If the slicer is killed give reward to player
                if(s.isKilled()) {
                    player.addMoney(s.giveReward());
                } else {
                    // Reduce lives if slicer exits the map
                    player.reduceLives(s.getPenalty());
                }
                // If the slicer has children and is killed, spawn children
                if(s.isKilled() && s.hasChildren) {
                    spawnChildren(s);

                }
            }
        }
    }

    /**
     * Method to spawn children of slicer if any
     *
     * @param slicer - The slicer whose children need to be spawned.
     * @param <T> - The class type of the slicer.
     */
    private <T extends Slicer> void spawnChildren(T slicer) {
        // Add according child slicer
        if(slicer.getClass() == SuperSlicer.class) {
            for(int i = 0; i < slicer.getChildNum(); i++) {
                // Collect the image file
                String imageSrc = String.format(IMAGE_FILE, SLICER);
                // Add to appropriate list
                Slicer newSlicer = new Slicer(polyline, imageSrc);
                // Set the position where the parent was killed
                newSlicer.setPosition(slicer.getCenter());
                newSlicer.setTargetIndex(slicer.getTargetIndex());
                slicers.add(newSlicer);
            }

        } else if(slicer.getClass() == MegaSlicer.class) {
            for(int i = 0; i < slicer.getChildNum(); i++) {
                // Collect the image file
                String imageSrc = String.format(IMAGE_FILE, SUPER_SLICER);
                // Add to appropriate list
                SuperSlicer newSlicer = new SuperSlicer(polyline, imageSrc);
                // Set the position where the parent was killed
                newSlicer.setPosition(slicer.getCenter());
                newSlicer.setTargetIndex(slicer.getTargetIndex());
                superSlicers.add(newSlicer);
            }
        } else {
            for(int i = 0; i < slicer.getChildNum(); i++) {
                // Collect the image file
                String imageSrc = String.format(IMAGE_FILE, MEGA_SLICER);
                // Add to appropriate list
                MegaSlicer newSlicer = new MegaSlicer(polyline, imageSrc);
                // Set the position where the parent was killed
                newSlicer.setPosition(slicer.getCenter());
                newSlicer.setTargetIndex(slicer.getTargetIndex());
                megaSlicers.add(newSlicer);
            }
        }
    }

}
