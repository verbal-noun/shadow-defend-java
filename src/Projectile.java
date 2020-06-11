import bagel.util.Point;
import bagel.util.Vector2;

public class Projectile<T extends Slicer> extends Sprite {
    private static final String IMAGE = "res/images/tank_projectile.png";
    private static final Double SPEED = 10.0;
    private static final int TANK_DAMAGE = 1;
    private boolean targetHit;
    private T target;
    protected int damage;

    /**
     * Creates a new Sprite (game entity)
     *
     * @param point The starting point for the entity
     */
    public Projectile(Point point, T target) {
        super(point, IMAGE);
        this.targetHit = false;
        this.target = target;
        this.damage = TANK_DAMAGE;
    }

    public void update() {
        // Obtain where we are and our target
        Point currentPoint = getCenter();
        Point enemyPoint = target.getCenter();
        // Convert them to vectors to perform calculations
        Vector2 enemy = enemyPoint.asVector();
        Vector2 current = currentPoint.asVector();
        Vector2 distance = enemy.sub(current);
        // Pixel distance from current and target position
        double magnitude = distance.length();
        // Check if we have reached the end
        super.move(distance.normalised().mul(SPEED * ShadowDefend.getTimescale()));
        // Check if we are close to target than our step size
        if(target.getRect().intersects(getCenter())) {
            // Deal damage to the enemy
            targetHit = true;
            target.reduceHealth(damage);
        }

        // Render the projectile
        render();

    }

    public boolean isTargetHit() { return targetHit; }
}