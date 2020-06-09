import bagel.util.Point;

import java.util.List;

public class MegaSlicer extends Slicer {
    /**
     * Creates a new Slicer
     *
     * @param polyline   The polyline that the slicer must traverse (must have at least 1 point)
     * @param slicerType
     */
    public MegaSlicer(List<Point> polyline, String slicerType) {
        super(polyline, slicerType);
    }
}
