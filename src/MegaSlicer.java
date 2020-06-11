import bagel.util.Point;

import java.util.List;

/**
 * The type Mega slicer.
 */
public class MegaSlicer extends SuperSlicer {
    private static final int CHILD_NUM = 2;
    private static final int FACTOR = 2;
    private static final int MEGA_REWARD = 10;

    /**
     * Creates a new Slicer
     *
     * @param polyline   The polyline that the slicer must traverse (must have at least 1 point)
     * @param slicerType the slicer type
     */
    public MegaSlicer(List<Point> polyline, String slicerType) {
        super(polyline, slicerType);
        this.health = super.getHealth() * FACTOR;
        this.reward = MEGA_REWARD;
        this.penalty = super.getPenalty() * CHILD_NUM;
    }

    /**
     * Gets speed.
     *
     * @return the speed
     */
    public double getSpeed() {
        return speed;
    }
}
