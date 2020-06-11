import bagel.util.Point;

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
     * @param point    The starting point for the entity
     */
    public Explosive(Point point) {
        super(point, IMAGE);
        this.frameCount = 0;
        this.status = false;
    }
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
    // Method to check if explosive is active or not
    public boolean isActive() {return status;}
    // Get the effect area of the explosive
    public int getRadius() {return RAIDUS;}
    // Retrieve the damage down by the explosive
    public int dealDamage() { return DAMAGE; }
}
