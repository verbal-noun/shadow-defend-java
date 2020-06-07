import bagel.Input;
import bagel.map.TiledMap;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.List;


public class Wave {
    //An attribute to determine when the wave has started and finished.
    private boolean waveStarted = false;
    private int spawnedSlicers = 0;

    //The slicers and the path of the wave
    private final List<Slicer> slicers;
    private List<Point> polyline;

    public int getSpawnedSlicers() {
        return spawnedSlicers;
    }

    public int getSlicerCount() {
        return slicers.size();
    }

    //Constructor
    public Wave(List<Point> polyline) {
        // Load polyline points from the map to the wave
        this.polyline = polyline;
        this.slicers = new ArrayList<>();
    }

    /* A method to initialise the wave of 5 slicers */
    public void spawnSlicer() {
        slicers.add(new Slicer(polyline));
        spawnedSlicers += 1;
    }

    /* A method to start the wave when 'S' is pressed */
    public void startWave() {
        // Condition to ensure the wave activates only once
        if(!this.waveStarted) {
            this.waveStarted = true;
        }
    }

    public boolean waveStatus() { return waveStarted; }

    /* A method to update the wave's current status */
    public void updateWave(Input input) {
        /* Set wave is estimated to finish at the end of update. This will be changed if any of
         * the slicers are still active.
         */
        for (int i = slicers.size() - 1; i >= 0; i--) {
            Slicer s = slicers.get(i);
            s.update(input);
            if (s.isFinished()) {
                slicers.remove(i);
            }
        }
    }
}
