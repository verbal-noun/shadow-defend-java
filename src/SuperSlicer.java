import bagel.util.Point;

import java.util.List;

/**
 * The type Super slicer.
 */
public class SuperSlicer extends Slicer {

   private static final double FACTOR = 3.0 / 4.0;
   private static final int SUPER_REWARD = 15;
   private static final int CHILD_NUM = 2;

    /**
     * Creates a new Super Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     * @param imageSrc the image src
     */
    public SuperSlicer(List<Point> polyline, String imageSrc) {
        super(polyline, imageSrc);
        this.reward = SUPER_REWARD;
        this.speed = SPEED * FACTOR;
        this.penalty = PENALTY * CHILD_NUM;
        this.hasChildren = true;
        this.childNo = CHILD_NUM;
    }


}
