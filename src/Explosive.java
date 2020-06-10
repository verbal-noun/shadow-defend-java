import bagel.util.Point;

public class Explosive extends Sprite {
    private static final String IMAGE = "res/images/explosive.png";
    private static final double DETONATE_PERIOD = 2;
    private int frameCount;
    private boolean status;

    /**
     * Creates a new Sprite (game entity)
     *
     * @param point    The starting point for the entity
     */
    public Explosive(Point point) {
        super(point, IMAGE);
        this.frameCount = 0;
        this.status = false;
    }
    public void update() {
        frameCount += ShadowDefend.getTimescale();

        if(frameCount / ShadowDefend.FPS >= DETONATE_PERIOD) {
            detonate();
            frameCount = 0;
        }
    }
    private void detonate() {
        status = true;
    }

    public boolean isActive() {return status;}
}
