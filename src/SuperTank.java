import bagel.util.Point;

/**
 * Super tank class.
 */
public class SuperTank extends Tower {
    private static final int COST = 600;
    private static final String IMAGE = "res/images/supertank.png";
    private static final int RADIUS_ST = 150;
    private static final double SUPER_COOLDOWN = 0.5;

    /**
     * Instantiates a new Super tank.
     *
     * @param point - the point where super tank will be placed
     */
    public SuperTank(Point point) {
        super(point, IMAGE, COST);
        this.radius = RADIUS_ST;
        this.cooldown = SUPER_COOLDOWN;
    }
}
