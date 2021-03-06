import bagel.AbstractGame;
import bagel.Input;
import bagel.Keys;
import bagel.Window;

/**
 * ShadowDefend, a tower defence game.
 */
public class ShadowDefend extends AbstractGame {

    // Dimensions of the game window
    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    // Change to suit system specifications. This could be
    // dynamically determined but that is out of scope.
    public static final double FPS = 60;
    // The spawn delay (in seconds) to spawn slicers
    private static final int INITIAL_TIMESCALE = 1;
    private static final int MAX_TIMESCALE = 5;
    private static final int MAX_LEVEL = 2;
    // Timescale is made static because it is a universal property of the game and the specification
    // says everything in the game is affected by this
    private static int timescale = INITIAL_TIMESCALE;
    private Level level;
    private int levelNo;

    /**
     * Creates a new instance of the ShadowDefend game
     */
    public ShadowDefend() {
        super(WIDTH, HEIGHT, "ShadowDefend");
        this.levelNo = 1;
        this.level = new Level(levelNo);
    }

    /**
     * The entry-point for the game
     *
     * @param args Optional command-line arguments
     */
    public static void main(String[] args) {
        new ShadowDefend().run();
    }

    /**
     * Gets timescale.
     *
     * @return the timescale
     */
    public static int getTimescale() {
        return timescale;
    }

    /**
     * Increases the timescale
     */
    private void increaseTimescale() {
        if(timescale < MAX_TIMESCALE) {
            timescale++;
        }
    }

    /**
     * Decreases the timescale but doesn't go below the base timescale
     */
    private void decreaseTimescale() {
        if (timescale > INITIAL_TIMESCALE) {
            timescale--;
        }
    }

    /**
     * Update to new level when current level is finished
     */
    private void increaseLevel() {
        if(levelNo < MAX_LEVEL) {
            levelNo += 1;
            this.level = new Level(levelNo);
        }
    }

    /**
     * Update the state of the game, potentially reading from input
     *
     * @param input The current mouse/keyboard state
     */
    @Override
    protected void update(Input input) {
        // Update level when current level is finished
        if(!level.isFinished() && level.isPlayerKilled()) {
            Window.close();
        } else {
            if(level.isFinished()){
                increaseLevel();
            }
        }
        // Update the game state of current level
        level.updateLevel(input);

        // Handle key presses
        if (input.wasPressed(Keys.S)) {
            level.startLevel();
        }

        if (input.wasPressed(Keys.L)) {
            increaseTimescale();
        }

        if (input.wasPressed(Keys.K)) {
            decreaseTimescale();
        }

    }
}
