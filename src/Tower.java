import bagel.Image;
import bagel.util.Point;

/**
 * The type Tower.
 */
public class Tower extends Sprite {
    /**
     * The Cost.
     */
    protected final int cost;
    /**
     * The Radius.
     */
    protected int radius;
    private boolean enemyDetected;
    private boolean weaponsHot;
    /**
     * The Cooldown.
     */
    protected double cooldown;
    private int frameCount;
    // Constructor detail for towers

    /**
     * Instantiates a new Tower.
     *
     * @param point the point
     * @param image the image
     * @param cost  the cost
     */
    public Tower(Point point, String image, int cost) {
        super(point, image);
        this.cost = cost;
        this.radius = 0;
        this.enemyDetected = false;
        this.weaponsHot = true;
        frameCount = 0;
    }

    /**
     * Gets image.
     *
     * @return the image
     */
// Return the current image for current tower
    public Image getImage() { return this.image; }

    /**
     * Gets cost.
     *
     * @return the cost
     */
// Get the purchase cost of tower
    public int getCost() { return cost; }

    /**
     * Gets radius.
     *
     * @return the radius
     */
// Get the effect radius of the tower
    public int getRadius() {
        return radius;
    }

    /**
     * Is weapons hot boolean.
     *
     * @return the boolean
     */
    public boolean isWeaponsHot() { return weaponsHot; }

    /**
     * Is enemy detected boolean.
     *
     * @return the boolean
     */
    public boolean isEnemyDetected() { return enemyDetected; }

    /**
     * Sets enemy detected.
     *
     * @param signal the signal
     */
    public void setEnemyDetected(boolean signal) {
        this.enemyDetected = signal;
    }

    /**
     * Start cooldown.
     */
    public void startCooldown() {
        weaponsHot = false;
    }

    /**
     * Update.
     */
    public void update() {
        if(!weaponsHot) {
            frameCount += ShadowDefend.getTimescale();
            if(frameCount / ShadowDefend.FPS >= cooldown) {
                weaponsHot = true;
                frameCount = 0;
            }
        }
    }
}
