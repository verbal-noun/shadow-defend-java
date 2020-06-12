import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * The type Status panel.
 */
public class StatusPanel {

    private static final int DEFAULT_LIVES = 25;
    private static final int DEFAULT_SPEED = 1;
    private static final String BG_IMAGE = "res/images/buypanel.png";
    private static final String FONT_FILE = "res/fonts/DejaVuSans-Bold.ttf";
    private static final int FONT_SIZE = 14;
    // Attributes to work as status codes
    private static final Integer STATUS_WIN = 0;
    private static final Integer STATUS_PLACING = 1;
    private static final Integer STATUS_WAVE = 2;
    private static final Integer STATUS_WAIT = 3;
    // Coordinates of display items
    private static final int x_COOR = 512;
    private static final int Y_COOR = 738;
    private static final int STATUS_Y = 706;
    private static final int TIME_X = 150;
    private static final int STATUS_X = 400;
    private static final int LIVE_X = 934;
    private static final int WAVE_X = 10;
    // Status messages
    private static final String WIN = "Winner!";
    private static final String PLACING = "Placing";
    private static final String WAVE = "Wave In Progress";
    private static final String WAIT = "Awaiting Start";
    //Attributes
    private int waveNo;
    private String gameStatus;
    private double timeScale;
    private int playerLives;
    // Background image of the panel
    private static final Image background = new Image(BG_IMAGE);
    private static Rectangle canvas = background.getBoundingBoxAt(new Point(0, 0));;

    /**
     * Instantiates a new Status panel.
     */
    public StatusPanel() {
        this.waveNo = 1;
        this.playerLives = DEFAULT_LIVES;
        this.timeScale = ShadowDefend.getTimescale();
        // Initiate the game with the lowest status
        this.gameStatus = WAIT;
    }

    /**
     * Render panel.
     */
    public void renderPanel() {
        // Draw background image
        background.draw(x_COOR, Y_COOR);
        // Draw status panel items
        Font font = new Font(FONT_FILE, FONT_SIZE);
        font.drawString("Wave: " + waveNo, WAVE_X, STATUS_Y);
        // Draw timescale
        DrawOptions color = new DrawOptions();
        color.setBlendColour((ShadowDefend.getTimescale() > DEFAULT_SPEED) ? Colour.GREEN : Colour.WHITE);
        font.drawString("Timescale: " + timeScale, TIME_X, STATUS_Y, color);
        font.drawString("Status: " + gameStatus, STATUS_X, STATUS_Y);
        font.drawString("Lives: " + playerLives, LIVE_X, STATUS_Y);
    }

    /**
     * Sets game status.
     *
     * @param code - Signals the current status of the game
     */
    public void setGameStatus(int code) {
        // Signal game has won
        if(code == STATUS_WIN) {
            gameStatus = WIN;
        }
        // Signal player is placing tower
        else if(code == STATUS_PLACING) {
            gameStatus = PLACING;
        }
        // Signal a wave is in progress
        else if(code == STATUS_WAVE) {
            gameStatus = WAVE;
        }
        // Signal waiting for player action
        else if(code == STATUS_WAIT) {
            gameStatus = WAIT;
        }
        else {
            System.out.println("Invalid code");
        }
    }

    /**
     * Sets player lives.
     *
     * @param lives the lives
     */
    public void setPlayerLives(int lives) {
        playerLives = lives;
    }

    /**
     * Increase wave of the game.
     */
    public void increaseWave() {
        waveNo += 1;
    }

    /**
     * Sets time scale.
     */
    public void setTimeScale() {
        this.timeScale = ShadowDefend.getTimescale();
    }
}
