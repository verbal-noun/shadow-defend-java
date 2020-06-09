import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;
import bagel.util.Rectangle;

import java.util.ArrayList;
import java.util.List;


public class BuyPanel {
    // Background image of the panel and purchase items
    private static final String BG_IMAGE = "res/images/buypanel.png";
    private static final String FONT_FILE = "res/fonts/DejaVuSans-Bold.ttf";
    private static final String TANK = "res/images/tank.png";
    private static final String SUPER_TANK = "res/images/supertank.png";
    private static final String AIR_SUPPORT = "res/images/airsupport.png";
    // Key binds
    private static final String START = "S - Start Wave";
    private static final String SPEED_UP = "L - Increase Timescale";
    private static final String SPEED_DOWN = "K - Decrease Timescale";
    // Initialising the background image
    private static final Image background = new Image(BG_IMAGE);
    private static final Rectangle canvas = background.getBoundingBoxAt(new Point(0, 0));
    // Dimensions
    private static final double OFFSET_H = 40;
    private static final double OFFSET_W = 120;
    private static final double KEY_W = 450;
    // Item costs
    private static final int TANK_COST = 250;
    private static final int SUP_TANK_COST = 600;
    private static final int AIR_SUP_COST = 500;
    // Purchase items
    private final Tower tank;
    private final Tower superTank;
    private final Tower airSupport;
    private List<Tower> purchaseItems;

    public List<Tower> getPurchaseItems() {
        return purchaseItems;
    }

    // Attribute to keep track of player's current money
    private int playerMoney;

    // Constructor
    public BuyPanel(int money) {
        // Initialise purchase items
        this.tank = new Tower(new Point(64, OFFSET_H), TANK, 250);
        this.superTank = new Tower(new Point(tank.getCenter().x + OFFSET_W, OFFSET_H), SUPER_TANK, 600);
        this.airSupport = new Tower(new Point(superTank.getCenter().x + OFFSET_W, OFFSET_H), AIR_SUPPORT, 500);
        this.playerMoney = money;
        // Load items into list
        loadItems();
    }

    public void renderPanel() {
        // Draw the background
        background.drawFromTopLeft(0, 0);
        // Render purchase items
        tank.render();
        superTank.render();
        airSupport.render();
        // Display prices with appropriate colour
        renderPrices();
        // Display player's current money
        renderMoney();
        // Display game controls
        renderKeys();
    }

    // Function to display the prices of purchase items
    private void renderPrices() {
        Font font = new Font(FONT_FILE, 20);
        DrawOptions color = new DrawOptions();
        // Display the price of purchase items with appropriate colour
        color.setBlendColour((playerMoney >= TANK_COST) ? Colour.GREEN : Colour.RED);
        font.drawString("$" + TANK_COST, 40, 90, color);

        color.setBlendColour((playerMoney >= SUP_TANK_COST) ? Colour.GREEN : Colour.RED);
        font.drawString("$" + SUP_TANK_COST, 155, 90, color);

        color.setBlendColour((playerMoney >= AIR_SUP_COST) ? Colour.GREEN : Colour.RED);
        font.drawString("$" + AIR_SUP_COST, 278, 90, color);
    }

    // Display the current money of the player
    private void renderMoney() {
        Font font = new Font(FONT_FILE, 50);
        font.drawString("$" + playerMoney, 824, 65);
    }

    // Display the game controls
    private void renderKeys() {
        Font font = new Font(FONT_FILE, 14);
        font.drawString("Key binds: ", KEY_W, 20);
        font.drawString(START, KEY_W, 45);
        font.drawString(SPEED_UP, KEY_W, 60);
        font.drawString(SPEED_DOWN, KEY_W, 75);
    }

    private void loadItems() {
        purchaseItems = new ArrayList<>();
        purchaseItems.add(tank);
        purchaseItems.add(superTank);
        purchaseItems.add(airSupport);
    }

    public void setPlayerMoney(int money) {
        playerMoney = money;
    }
    public Rectangle getCanvas() {
        return canvas;
    }
}
