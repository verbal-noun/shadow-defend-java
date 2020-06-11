import bagel.Image;
import bagel.util.Point;

public class Tower extends Sprite {
    protected final int cost;
    protected int radius;
    private boolean enemyDetected;
    private boolean weaponsHot;
    protected double cooldown;
    private int frameCount;
    // Constructor detail for towers

    public Tower(Point point, String image, int cost) {
        super(point, image);
        this.cost = cost;
        this.radius = 0;
        this.enemyDetected = false;
        this.weaponsHot = true;
        frameCount = 0;
    }
    // Return the current image for current tower
    public Image getImage() { return this.image; }
   // Get the purchase cost of tower
    public int getCost() { return cost; }
    // Get the effect radius of the tower
    public int getRadius() {
        return radius;
    }

    public boolean isWeaponsHot() { return weaponsHot; }

    public boolean isEnemyDetected() { return enemyDetected; }

    public void setEnemyDetected(boolean signal) {
        this.enemyDetected = signal;
    }

    public void startCooldown() {
        weaponsHot = false;
    }

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
