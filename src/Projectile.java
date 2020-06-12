import bagel.util.Point;
import bagel.util.Vector2;

/**
 * The Projectile class.
 *
 * @param <T> - Type of enemy which it is targeting
 */
public class Projectile<T extends Slicer> extends Sprite {
    private static final String IMAGE = "res/images/tank_projectile.png";
    private static final Double SPEED = 10.0;
    private static final int TANK_DAMAGE = 1;
    private boolean targetHit;
    private final T target;
    protected int damage;

    /**
     * Creates a new Sprite (game entity)
     *
     * @param point  - The starting point for the entity
     * @param target - the target which it will attack
     */
    public Projectile(Point point, T target) {
        super(point, IMAGE);
        this.targetHit = false;
        this.target = target;
        this.damage = TANK_DAMAGE;
    }

    /**
     * Update the current state of the projectile
     */
    public void update() {
        // Obtain where we are and our target
        Point currentPoint = getCenter();
        Point enemyPoint = target.getCenter();
        // Convert them to vectors to perform calculations
        Vector2 enemy = enemyPoint.asVector();
        Vector2 current = currentPoint.asVector();
        Vector2 distance = enemy.sub(current);
        // Check if we have reached the end
        super.move(distance.normalised().mul(SPEED * ShadowDefend.getTimescale()));
        // Check if we are close to target than our step size
        if(target.getRect().intersects(getCenter())) {
            // Deal damage to the enemy
            targetHit = true;
            // Reduce the health of the enemy
            target.reduceHealth(damage);
        }

        // Render the projectile
        render();

    }

    /**
     * Is target hit boolean.
     *
     * @return the boolean
     */
    public boolean isTargetHit() { return targetHit; }
}
