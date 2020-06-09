import bagel.util.Point;

import java.util.List;

public class SpawnEvent extends Event {
    private static final String SLICER = "slicer";
    private static final String SUPER_SLICER = "superslicer";
    private static final String MEGA_SLICER = "megaslicer";
    private static final String APEX_SLICER = "apexslicer";
    private static final String IMAGE_FILE = "res/images/%s.png";

    private String slicerType;
    private int slicerCount;

    private boolean addSlicer;

    public SpawnEvent(String[] info) {
        super(info);
        this.buffer = Double.parseDouble(info[3]) / FACTOR;
        this.frameCount = Integer.MAX_VALUE;
        this.slicerType = info[2];
        this.slicerCount = Integer.parseInt(info[1]);
        this.addSlicer = false;
    }

    public boolean isAddSlicer() {
        return addSlicer;
    }

    @Override
    public void updateEvent() {
        // If slicer just has been added wait for delay
        if(addSlicer) {
            addSlicer = false;
        }
        // spawn slicers as required
        if(frameCount / FPS >= buffer && slicerCount > 0){
            // Add a slicer to the current wave according to its type
            // If slicer is regular kind
            addSlicer = true;
            frameCount = 0;
            slicerCount -= 1;
        }

        if(slicerCount == 0) {
            status = false;
        }
        // Increase frame count
        frameCount += ShadowDefend.getTimescale();
    }

    public Slicer generateSlicer(List<Point> polyline) {
        String type = String.format(IMAGE_FILE, slicerType);
        if(slicerType.equals(SLICER)) {
            return new Slicer(polyline, type);
        }
        // Add a super slicer
        else if(slicerType.equals(SUPER_SLICER)) {
            return new Slicer(polyline, type);
        }
        // Add a mega slicer
        else if(slicerType.equals(MEGA_SLICER)) {
            return new Slicer(polyline, type);
        }
        // Add an apex slicer
        else  {
            return new Slicer(polyline, type);
        }
    }
}
