import bagel.AbstractGame;
import bagel.Input;
import bagel.Keys;
import bagel.Window;
import bagel.map.TiledMap;
import bagel.util.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * ShadowDefend, a tower defence game.
 */
public class ShadowDefend extends AbstractGame {

    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    private static final String MAP_FILE = "res/levels/1.tmx";
    // Change to suit system specifications. This could be
    // dynamically determined but that is out of scope.
    public static final double FPS = 60;
    // The spawn delay (in seconds) to spawn slicers
    private static final int SPAWN_DELAY = 5;
    private static final int INITIAL_TIMESCALE = 1;
    private static final int MAX_SLICERS = 5;
    // Timescale is made static because it is a universal property of the game and the specification
    // says everything in the game is affected by this
    private static int timescale = INITIAL_TIMESCALE;
    private final TiledMap map;
    private final List<Point> polyline;
    private double frameCount;
    private Wave wave;
    private Level level;
    private int levelNo;

    /**
     * Creates a new instance of the ShadowDefend game
     */
    public ShadowDefend() {
        super(WIDTH, HEIGHT, "ShadowDefend");
        this.map = new TiledMap(MAP_FILE);
        this.polyline = map.getAllPolylines().get(0);
        this.frameCount = Integer.MAX_VALUE;
        this.wave = new Wave(polyline);
        this.levelNo = 1;
        this.level = new Level(levelNo);


        // Temporary fix for the weird slicer map glitch (might have to do with caching textures)
        // This fix is entirely optional
        new Slicer(polyline);
    }

    /**
     * The entry-point for the game
     *
     * @param args Optional command-line arguments
     */
    public static void main(String[] args) {
        new ShadowDefend().run();
    }

    public static int getTimescale() {
        return timescale;
    }

    /**
     * Increases the timescale
     */
    private void increaseTimescale() {
        timescale++;
        // Update status panel
        level.updateTime();
    }

    /**
     * Decreases the timescale but doesn't go below the base timescale
     */
    private void decreaseTimescale() {
        if (timescale > INITIAL_TIMESCALE) {
            timescale--;
        }
        // Update status panel
        level.updateTime();
    }

    /**
     * Update to new level when current level is finished
     */
    private void increaseLevel() {
        levelNo += 1;
        this.level = new Level(levelNo);
    }

    /**
     * Update the state of the game, potentially reading from input
     *
     * @param input The current mouse/keyboard state
     */
    @Override
    protected void update(Input input) {
        // Increase the frame counter by the current timescale
        frameCount += getTimescale();

        // Draw map from the top left of the window
        level.render();
        //System.out.println(level.waves.size());

        // Handle key presses
        if (input.wasPressed(Keys.S)) {
            wave.startWave();
        }

        if (input.wasPressed(Keys.L)) {
            increaseTimescale();
        }

        if (input.wasPressed(Keys.K)) {
            decreaseTimescale();
        }

        // Check if it is time to spawn a new slicer (and we have some left to spawn)
        if (wave.waveStatus() && frameCount / FPS >= SPAWN_DELAY && wave.getSpawnedSlicers() != MAX_SLICERS) {
            // Make a new slicer active in the wave
            wave.spawnSlicer();
            // Reset frame counter
            frameCount = 0;
        }

        // Close game if all slicers have finished traversing the polyline
        if (wave.getSpawnedSlicers() == MAX_SLICERS && wave.getSlicerCount() == 0) {
            Window.close();
        }

        // Update all sprites, and remove them if they've finished
        wave.updateWave(input);
    }
}
