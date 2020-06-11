import bagel.util.Point;

import java.util.List;

public class ApexSlicer extends MegaSlicer {
    private static final int APEX_CHILD_NUM = 4;
    private static final int FACTOR = 2;
    private static final int HEALTH_FACTOR = 25;
    private static final int APEX_REWARD = 150;
    /**
     * Creates a new Slicer
     *
     * @param polyline   The polyline that the slicer must traverse (must have at least 1 point)
     * @param slicerType
     */
    public ApexSlicer(List<Point> polyline, String slicerType) {
        super(polyline, slicerType);
        this.health = HEALTH_FACTOR * DEF_HEALTH;
        this.reward = APEX_REWARD;
        this.speed = super.getSpeed() / FACTOR;
        this.penalty = super.getPenalty() * APEX_CHILD_NUM;
        this.childNo = APEX_CHILD_NUM;
    }

}
