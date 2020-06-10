import bagel.Image;
import bagel.util.Point;

public class Tower extends Sprite {
    protected final int cost;
    protected int radius;
    private boolean enemyDetected;

    // Constructor detail for towers
    public Tower(Point point, String image, int cost) {
        super(point, image);
        this.cost = cost;
        this.radius = 0;
        this.enemyDetected = false;
    }
    public Image getImage() { return this.image; }

    public int getCost() { return cost; }

    public int getRadius() {
        return radius;
    }

    public boolean isEnemyDetected() {
        return enemyDetected;
    }

    public void setEnemyDetected(boolean signal) {
        this.enemyDetected = signal;
    }
}
