import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Slicer extends Sprite{

    private static final String IMAGE_FILE = "res/images/slicer.png";
    private static final double SPEED = 1;

    private final List<Point> polyline;
    private int targetPointIndex;
    private boolean finished;

    /**
     * Creates a new Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public Slicer(List<Point> polyline) {
        super(polyline.get(0), IMAGE_FILE);
        this.polyline = polyline;
        this.targetPointIndex = 1;
        this.finished = false;
    }

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
        if(magnitude < SPEED * ShadowDefend.getTimescale()) {
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
        super.move(distance.normalised().mul(SPEED * ShadowDefend.getTimescale()));
        // update current rotation angle to face target point
        setAngle(Math.atan2(targetPoint.y - currentPoint.y, targetPoint.x - currentPoint.x));
        //super.update(input);
        render();
    }

    public boolean isFinished() {
        return finished;
    }
}

