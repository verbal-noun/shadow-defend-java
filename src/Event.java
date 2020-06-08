public class Event {
    protected static final double FACTOR = 1000;
    protected static final double FPS = 60;

    protected double buffer;
    protected boolean status;
    protected int frameCount;
    private String eventType;

    public Event(String[] info){
        this.eventType = info[0];
        this.status = true;
        this.frameCount = 0;
        this.buffer = Double.parseDouble(info[1]) / FACTOR;
    }

    public void updateEvent() {
        frameCount += ShadowDefend.getTimescale();
        // Update buffer time
        if(frameCount == FPS) {
            buffer -= 1;
        }

        if(buffer <= 0) {
            status = false;
        }
    }

    public String getEventType() {
        return eventType;
    }

    public boolean eventStatus() {
        return status;
    }
}
