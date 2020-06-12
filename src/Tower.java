import bagel.Image;
import bagel.util.Point;

/**
 * The base Tower class.
 * Contains basic functionalities needed by game's defense.
 */
public class Tower extends Sprite {

    protected final int cost;
    protected int radius;
    private boolean enemyDetected;
    private boolean weaponsHot;
    protected double cooldown;
    private int frameCount;

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
     * Gets image of the tower.
     *
     * @return the image pf current tower
     */

    public Image getImage() { return this.image; }

    /**
     * Gets cost.
     *
     * @return the cost of current tower
     */
    public int getCost() { return cost; }

    /**
     * Gets the effect radius of the tower.
     *
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Method which signals if the tower is ready to fire
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
     * Sets whether a tower has detected an enemy of not
     *
     * @param signal - A boolean which indicates enemy has been seen or not
     */
    public void setEnemyDetected(boolean signal) {
        this.enemyDetected = signal;
    }

    /**
     * Start cooldown for the tower.
     */
    public void startCooldown() {
        weaponsHot = false;
    }

    /**
     * Update.
     */
    public void update() {
        // If shot has been fired start cooldown
        if(!weaponsHot) {
            frameCount += ShadowDefend.getTimescale();
            // When cooldown time reached, ready weapons
            if(frameCount / ShadowDefend.FPS >= cooldown) {
                weaponsHot = true;
                frameCount = 0;
            }
        }
    }
}
