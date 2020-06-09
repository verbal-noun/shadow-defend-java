import bagel.util.Point;

public class Tank extends Tower {

    private static final int TANK_COST = 250;
    private static final String TANK_IMAGE = "res/images/tank.png";

    public Tank(Point point) {
        super(point, TANK_IMAGE, TANK_COST);
    }
}
