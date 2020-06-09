import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.List;

public class ApexSlicer extends MegaSlicer {
    private static final int CHILD_NUM = 4;
    private static final double FACTOR = 3 / 8;
    private static final int APEX_REWARD = 150;
    private double speed;
    /**
     * Creates a new Slicer
     *
     * @param polyline   The polyline that the slicer must traverse (must have at least 1 point)
     * @param slicerType
     */
    public ApexSlicer(List<Point> polyline, String slicerType) {
        super(polyline, slicerType);
        this.health = 25 * DEF_HEALTH;
        this.reward = APEX_REWARD;
        this.speed = SPEED * FACTOR;
        this.penaly = super.getPenaly() * CHILD_NUM;
    }

    @Override
    public void update(Input input) {
        if(finished) {
            return;
        }

        // Obtain where we are and our target
        Point currentPoint = getCenter();
        Point targetPoint = polyline.get(targetPointIndex);
        // Convert them to vectors to perform calculations
        Vector2 target = targetPoint.asVector();
        Vector2 current = currentPoint.asVector();
        Vector2 distance = target.sub(current);
        // Pixel distance from current and target position
        double magnitude = distance.length();

        // Check if we are close to target than our step size
        if(magnitude < speed * ShadowDefend.getTimescale()) {
            // Check if we have reached the end
            if(targetPointIndex == polyline.size() - 1) {
                finished = true;
                return;
            } else {
                // Make the next point out target
                targetPointIndex += 1;
            }
        }

        // Move towards the target point
        // We do this by getting a unit vector in the direction of our target, and multiplying it
        // by the speed of the slicer (accounting for the timescale)
        super.move(distance.normalised().mul(speed * ShadowDefend.getTimescale()));
        // update current rotation angle to face target point
        setAngle(Math.atan2(targetPoint.y - currentPoint.y, targetPoint.x - currentPoint.x));
        //super.update(input);
        render();
    }
}
