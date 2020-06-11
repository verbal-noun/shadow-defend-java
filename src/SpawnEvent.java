import bagel.util.Point;

import java.util.List;

/**
 * The type Spawn event.
 */
public class SpawnEvent extends Event {
    private String slicerType;
    private int slicerCount;

    private boolean addSlicer;

    /**
     * Instantiates a new Spawn event.
     *
     * @param info the info
     */
    public SpawnEvent(String[] info) {
        super(info);
        this.buffer = Double.parseDouble(info[3]) / FACTOR;
        this.frameCount = Integer.MAX_VALUE;
        this.slicerType = info[2];
        this.slicerCount = Integer.parseInt(info[1]);
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

    @Override
    public void updateEvent() {
        // If slicer just has been added wait for delay
        if(addSlicer) {
            addSlicer = false;
        }
        // spawn slicers as required
        if(frameCount / FPS >= buffer && slicerCount > 0){
            // Add a slicer to the current wave according to its type
            // If slicer is regular kind
            addSlicer = true;
            frameCount = 0;
            slicerCount -= 1;
        }

        if(slicerCount == 0) {
            status = false;
        }
        // Increase frame count
        frameCount += ShadowDefend.getTimescale();
    }

    /**
     * Generate slicer string.
     *
     * @return the string
     */
    public String generateSlicer() {
        return slicerType;
    }
}
