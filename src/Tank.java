import bagel.util.Point;

/**
 * The type Tank.
 */
public class Tank extends Tower {

    private static final int TANK_COST = 250;
    private static final String TANK_IMAGE = "res/images/tank.png";
    private static final int RADIUS = 100;
    private static final double TANK_COOLDOWN = 1;

    /**
     * Instantiates a new Tank.
     *
     * @param point the point
     */
    public Tank(Point point) {
        super(point, TANK_IMAGE, TANK_COST);
        this.radius = RADIUS;
        this.cooldown = TANK_COOLDOWN;
    }
}
