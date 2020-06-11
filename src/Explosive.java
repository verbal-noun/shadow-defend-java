import bagel.util.Point;

/**
 * The type Explosive.
 */
public class Explosive extends Sprite {
    private static final String IMAGE = "res/images/explosive.png";
    private static final double DETONATE_PERIOD = 2;
    private static final int RAIDUS = 200;
    private static final int DAMAGE = 500;
    private int frameCount;
    private boolean status;

    /**
     * Creates a new Sprite (game entity)
     *
     * @param point The starting point for the entity
     */
    public Explosive(Point point) {
        super(point, IMAGE);
        this.frameCount = 0;
        this.status = false;
    }

    /**
     * Update.
     */
    public void update() {
        frameCount += ShadowDefend.getTimescale();
        // If countdown has finished then detonate the explosive
        if(frameCount / ShadowDefend.FPS >= DETONATE_PERIOD) {
            detonate();
            frameCount = 0;
        }
    }
    private void detonate() {
        status = true;
    }

    /**
     * Is active boolean.
     *
     * @return the boolean
     */
    public boolean isActive() {return status;}

    /**
     * Gets radius.
     *
     * @return the radius
     */
    public int getRadius() {return RAIDUS;}

    /**
     * Deal damage int.
     *
     * @return the int
     */
    public int dealDamage() { return DAMAGE; }
}
