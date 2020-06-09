import bagel.util.Point;

public class AirSupport extends Tower {
    private static final int PRICE = 500;
    private static final String IMAGE = "res/images/airsupport.png";

    public AirSupport(Point point) {
        super(point, IMAGE, PRICE);
    }
}
