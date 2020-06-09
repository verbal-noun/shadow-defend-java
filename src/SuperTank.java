import bagel.util.Point;

public class SuperTank extends Tower {
    private static final int COST = 600;
    private static final String IMAGE = "res/images/supertank.png";

    public SuperTank(Point point) {
        super(point, IMAGE, COST);
    }
}
