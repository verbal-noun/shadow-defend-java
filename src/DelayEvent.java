/**
 * The type Event.
 */
public class DelayEvent {

    protected static final double FACTOR = 1000;
    protected static final int EVENT_TYPE = 0;
    private static final int DELAY_TIME = 1;
    protected double buffer;
    protected boolean status;
    protected int frameCount;

    /**
     * Instantiates a new Event.
     *
     * @param info - the information from the waves.txt file
     */
    public DelayEvent(String[] info){
        this.status = true;
        this.frameCount = 0;
        this.buffer = Double.parseDouble(info[DELAY_TIME]) / FACTOR;
    }

    /**
     * Update event.
     */
    public void updateEvent() {
        frameCount += ShadowDefend.getTimescale();
        // Update buffer time
        if(frameCount == ShadowDefend.FPS) {
            // Decrease delay period
            buffer -= 1;
            frameCount = 0;
        }
        // If delay has been finished
        if(buffer <= 0) {
            status = false;
        }
    }

    /**
     * Event status boolean.
     *
     * @return the boolean
     */
    public boolean eventStatus() {
        return !status;
    }
}
