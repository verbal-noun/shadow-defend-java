import bagel.Image;
import bagel.util.Point;

public class Tower extends Sprite {
    private final int cost;

    // Constructor detail for towers
    public Tower(Point point, String image, int cost) {
        super(point, image);
        this.cost = cost;
    }

    public Image getImage() { return this.image; }

    public int getCost() { return cost; }
}
