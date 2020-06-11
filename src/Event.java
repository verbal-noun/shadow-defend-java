/**
 * The type Event.
 */
public class Event {
    /**
     * The constant FACTOR.
     */
    protected static final double FACTOR = 1000;
    /**
     * The constant FPS.
     */
    protected static final double FPS = 60;

    /**
     * The Buffer.
     */
    protected double buffer;
    /**
     * The Status.
     */
    protected boolean status;
    /**
     * The Frame count.
     */
    protected int frameCount;
    private String eventType;

    /**
     * Instantiates a new Event.
     *
     * @param info the info
     */
    public Event(String[] info){
        this.eventType = info[0];
        this.status = true;
        this.frameCount = 0;
        this.buffer = Double.parseDouble(info[1]) / FACTOR;
    }

    /**
     * Update event.
     */
    public void updateEvent() {
        frameCount += ShadowDefend.getTimescale();
        //System.out.println(frameCount);
        // Update buffer time
        if(frameCount == FPS) {
            buffer -= 1;
            frameCount = 0;
        }

        if(buffer <= 0) {
            status = false;
        }
    }

    /**
     * Gets event type.
     *
     * @return the event type
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Event status boolean.
     *
     * @return the boolean
     */
    public boolean eventStatus() {
        return status;
    }
}
