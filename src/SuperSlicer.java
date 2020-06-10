import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.List;

public class SuperSlicer extends Slicer {

   private static final String IMAGE_FILE = "res/images/slicer.png";
   private static final double FACTOR = (3 / 4);
   private static final int SUPER_REWARD = 15;
   private static final int CHILD_NUM = 2;
   protected int reward;
   protected int penaly;


    /**
     * Creates a new Super Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public SuperSlicer(List<Point> polyline, String imageSrc) {
        super(polyline, imageSrc);
        this.reward = SUPER_REWARD;
        this.speed = SPEED * FACTOR;
        this.penaly = PENALTY * CHILD_NUM;
    }

    public int getPenaly() {
        return penaly;
    }
}
