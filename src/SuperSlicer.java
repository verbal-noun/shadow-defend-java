import bagel.Image;
import bagel.util.Point;
import java.util.List;

public class SuperSlicer extends Slicer {

   private static final String IMAGE_FILE = "res/images/slicer.png";
    /**
     * Creates a new Super Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public SuperSlicer(List<Point> polyline, String imageSrc) {
        super(polyline, imageSrc);
    }
}
