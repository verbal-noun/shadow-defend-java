import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

public class StatusPanel {

    private static final int DEFAULT_LIVES = 25;
    private static final int DEFAULT_SPEED = 1;
    private static final String BG_IMAGE = "res/images/buypanel.png";
    private static final String FONT_FILE = "res/fonts/DejaVuSans-Bold.ttf";
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

    // Constructor
    public StatusPanel() {
        this.waveNo = 1;
        this.playerLives = DEFAULT_LIVES;
        this.timeScale = ShadowDefend.getTimescale();
        this.gameStatus = WAIT;
    }

    // Render the status panel
    public void renderPanel() {
        // Draw background image
        background.draw(512, 738);
        // Draw status panel items
        Font font = new Font(FONT_FILE, 14);
        font.drawString("Wave: " + waveNo, 10, 706);
        // Draw timescale
        DrawOptions color = new DrawOptions();
        color.setBlendColour((ShadowDefend.getTimescale() > DEFAULT_SPEED) ? Colour.GREEN : Colour.WHITE);
        font.drawString("Timescale: " + timeScale, 150, 706, color);
        font.drawString("Status: " + gameStatus, 400, 706);
        font.drawString("Lives: " + playerLives, 934, 706);
    }

    // Update game status
    public void setGameStatus(int code) {
        // Signal game has won
        if(code == 0) {
            gameStatus = WIN;
        }
        // Signal player is placing tower
        if(code == 1) {
            gameStatus = PLACING;
        }
        // Signal a wave is in progress
        if(code == 2) {
            gameStatus = WAVE;
        }
        // Signal waiting for player action
        if(code == 3) {
            gameStatus = WAIT;
        }
    }

    // update player lives
    public void setPlayerLives(int hitPoints) {
        playerLives -= hitPoints;
    }

    // update wave no
    public void increaseWave() {
        waveNo += 1;
    }

    public void setTimeScale() {
        this.timeScale = ShadowDefend.getTimescale();
    }
}
