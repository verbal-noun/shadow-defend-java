import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.List;

public class MegaSlicer extends SuperSlicer {
    private static final int CHILD_NUM = 2;
    /**
     * Creates a new Slicer
     *
     * @param polyline   The polyline that the slicer must traverse (must have at least 1 point)
     * @param slicerType
     */
    public MegaSlicer(List<Point> polyline, String slicerType) {
        super(polyline, slicerType);
        this.health = super.getHealth()*2;
        this.reward = 10;
        this.penaly = super.getPenaly() * CHILD_NUM;
    }

    public double getSpeed() {
        return speed;
    }
}
