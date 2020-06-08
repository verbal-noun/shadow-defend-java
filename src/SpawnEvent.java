public class SpawnEvent extends Event {
    private static final String SLICER = "slicer";
    private static final String SUPER_SLICER = "superslicer";
    private static final String MEGA_SLICER = "megaslicer";
    private static final String APEX_SLICER = "apexslicer";
    private String slicerType;
    private int slicerCount;

    public SpawnEvent(String[] info) {
        super(info);
        this.buffer = Double.parseDouble(info[3]) / FACTOR;
        this.frameCount = Integer.MAX_VALUE;
        this.slicerType = info[2];
        this.slicerCount = Integer.parseInt(info[1]);
    }

    @Override
    public void updateEvent() {
        // spawn slicers as required
        if(frameCount / FPS >= buffer && slicerCount > 0){
            // Add a slicer to the current wave according to its type
            // If slicer is regular kind
            if(slicerType.equals(SLICER)) {

            }
            // Add a super slicer
            if(slicerType.equals(SUPER_SLICER)) {

            }
            // Add a mega slicer
            if(slicerType.equals(MEGA_SLICER)) {

            }
            // Add an apex slicer
            if(slicerType.equals(APEX_SLICER)) {

            }
        }

        if(slicerCount == 0) {
            status = false;
        }
    }
}
