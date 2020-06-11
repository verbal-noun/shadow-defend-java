import bagel.util.Point;

public class SuperTank extends Tower {
    private static final int COST = 600;
    private static final String IMAGE = "res/images/supertank.png";
    private static final int RADIUS_ST = 150;
    private static final double SUPER_COOLDOWN = 0.5;

    public SuperTank(Point point) {
        super(point, IMAGE, COST);
        this.radius = RADIUS_ST;
        this.cooldown = SUPER_COOLDOWN;
    }
}
