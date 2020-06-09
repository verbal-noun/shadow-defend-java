import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.List;

public class SuperSlicer extends Slicer {

   private static final String IMAGE_FILE = "res/images/slicer.png";
   private static final int DEF_REWARD = 15;
   private static final int CHILD_NUM = 2;
   protected int reward;
   protected int penaly;
   private double speed;

    /**
     * Creates a new Super Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public SuperSlicer(List<Point> polyline, String imageSrc) {
        super(polyline, imageSrc);
        this.reward = DEF_REWARD;
        this.speed = SPEED * 3 / 4;
        this.penaly = PENALTY * CHILD_NUM;
        System.out.println(speed);
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

    public int getPenaly() {
        return penaly;
    }
}
