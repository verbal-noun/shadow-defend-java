/**
 * The type Spawn event.
 */
public class SpawnEvent extends DelayEvent {
    private String slicerType;
    private int slicerCount;
    private boolean addSlicer;
    private static final int BUFFER_TIME = 3;
    private static final int SLICER_TYPE = 2;
    private static final int SLICER_COUNT = 1;


    /**
     * Instantiates a new Spawn event.
     *
     * @param info the info
     */
    public SpawnEvent(String[] info) {
        super(info);
        this.buffer = Double.parseDouble(info[BUFFER_TIME]) / FACTOR;
        this.frameCount = Integer.MAX_VALUE;
        this.slicerType = info[SLICER_TYPE];
        this.slicerCount = Integer.parseInt(info[SLICER_COUNT]);
        this.addSlicer = false;
    }

    /**
     * Is add slicer boolean.
     *
     * @return the boolean
     */
    public boolean isAddSlicer() {
        return addSlicer;
    }

    /**
     * Run through the event
     */
    @Override
    public void updateEvent() {
        // If slicer just has been added wait for delay
        if(addSlicer) {
            addSlicer = false;
        }
        // spawn slicers as required
        if(frameCount / ShadowDefend.FPS >= buffer && slicerCount > 0){
            // Add a slicer to the current wave according to its type
            // If slicer is regular kind
            addSlicer = true;
            frameCount = 0;
            slicerCount -= 1;
        }
        // If all slicers of the event has been generated
        if(slicerCount == 0) {
            status = false;
        }
        // Increase frame count
        frameCount += ShadowDefend.getTimescale();
    }

    /**
     * Return the slicer type specified by the file.
     *
     * @return the string
     */
    public String generateSlicer() {
        return slicerType;
    }
}
