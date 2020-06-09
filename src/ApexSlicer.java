import bagel.util.Point;

import java.util.List;

public class ApexSlicer extends Slicer {
    /**
     * Creates a new Slicer
     *
     * @param polyline   The polyline that the slicer must traverse (must have at least 1 point)
     * @param slicerType
     */
    public ApexSlicer(List<Point> polyline, String slicerType) {
        super(polyline, slicerType);
    }
}
