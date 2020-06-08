import bagel.util.Point;

public class Tower extends Sprite {
    private final int cost;

    // Constructor detail for towers
    public Tower(Point point, String image, int cost) {
        super(point, image);
        this.cost = cost;
    }

}
