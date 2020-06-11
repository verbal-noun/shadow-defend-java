import bagel.util.Point;

public class Projectile extends Sprite {
    private static final String IMAGE = "res/images/tank_projectile.png";
    private boolean targetHit;
    /**
     * Creates a new Sprite (game entity)
     *
     * @param point    The starting point for the entity
     */
    public Projectile(Point point) {
        super(point, IMAGE);
        this.targetHit = false;
    }

    public void update() {

    }

    public boolean isTargetHit() { return targetHit; }
}
