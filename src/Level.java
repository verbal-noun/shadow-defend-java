import bagel.Image;
import bagel.Input;
import bagel.MouseButtons;
import bagel.Window;
import bagel.map.TiledMap;
import bagel.util.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Level {
    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    private static final int TOP = 0;
    private static final int BUY_PANEL_BORDER = 100;
    private static final int STATUS_PANEL_BORDER = 675;
    private static final int TANK = 0;
    private static final int SUPER_TANK = 1;
    private static final int AIR_SUPPORT = 2;
    // Status messages
    private static final Integer WIN = 0;
    private static final Integer PLACING = 1;
    private static final Integer WAVE = 2;
    private static final Integer WAIT = 3;
    // Map attribute for the level
    private final TiledMap map;
    private final List<Point> polyline;
    // File containing wave information
    private static final String WAVE_FILE = "res/levels/waves.txt";
    private List<Wave> waves = new ArrayList<>();
    // Player for the level
    private Player player;
    // Panels
    private BuyPanel buyPanel;
    private StatusPanel statusPanel;
    private boolean isFinished;
    // Attributes related to purchase
    private boolean itemSelected;
    private List<Tower> purchaseItems;
    private int selectedItem;
    private List<Tank> tanks;
    private List<SuperTank> superTanks;
    private List<AirSupport> airSupport;
    private int planeCount;
    private List<Explosive> explosives;

    // Constructor
    public Level(int currentLevel) {
        String MAP_FILE = String.format("res/levels/%s.tmx", currentLevel);
        this.map = new TiledMap(MAP_FILE);
        this.polyline = map.getAllPolylines().get(0);
        this.player = new Player();
        this.buyPanel = new BuyPanel(500);
        this.purchaseItems = buyPanel.getPurchaseItems();
        this.statusPanel = new StatusPanel();
        this.isFinished = false;
        // Load waves
        loadWave();
        this.itemSelected = false;
        // Load level defence
        tanks = new ArrayList<>();
        superTanks = new ArrayList<>();
        airSupport = new ArrayList<>();
        this.planeCount = 0;
        this.explosives = new ArrayList<>();
    }

    private void loadWave() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(WAVE_FILE));
            String line = reader.readLine();
            while(line != null) {
                // Process the wave line
                // Extract wave number
                int index = line.indexOf(",");
                int waveNo = Integer.parseInt(line.substring(0, index));
                // Check if wave exists in the list
                if(waveNo > waves.size()) {
                    waves.add(new Wave(polyline));
                }
                // Pass event information into the wave
                waves.get(waveNo-1).addEvent(line.substring(index+1));
                // Read the next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render() {
        map.draw(0, 0, 0, 0, WIDTH, HEIGHT);
        // Configure status panel appropriately
        updatePanels();
        // Draw the panels
        buyPanel.renderPanel();
        statusPanel.renderPanel();
        // Draw the towers
        renderTowers();
    }

    public void updateLevel(Input input) {
        // Render level
        render();
        // Update current wave
        if(waves.size() > 0) {
            if (waves.get(TOP).isFinished()) {
                // Load next wave
                loadNextWave();
            } else {
                // Update the current wave
                waves.get(TOP).updateWave(input);
            }
            // Signal end of wave if all waves are finished
            if (waves.size() == 0) {
                isFinished = true;
                System.out.println("Level finished");
                statusPanel.setGameStatus(WIN);
            }
        }
        // Update airplane position if any
        flyAirplanes();
        //check for live explosives
        updateExplosive();
        // Control player interaction
        playerInteraction(input);
    }
    //----------------------------------------- Panel related methods ------------------------------------------------//
    // Update the status panel with proper information
    private void updatePanels() {
        // Status panel related configurations
        statusPanel.setTimeScale();
        // Set the game status
        if(itemSelected) {
            statusPanel.setGameStatus(PLACING);
        } else if (waves.get(TOP).waveStatus()) {
            statusPanel.setGameStatus(WAVE);
        } else if(!isFinished) {
            statusPanel.setGameStatus(WAIT);
        }
        // Update player lives
        statusPanel.setPlayerLives(player.getLives());
        // Buy panel related configuration
        buyPanel.setPlayerMoney(player.getMoney());
    }
    // Method to enable tower purchase and placement
    private void playerInteraction(Input input) {
        // Point to track current position of the cursor
        Point currPos = new Point(input.getMouseX(), input.getMouseY());
        // Check if left click was pressed or not
        if(input.wasPressed(MouseButtons.LEFT) && !itemSelected) {
            // If item hasn't been selected, check where mouse was pressed
            for(int i = 0; i < purchaseItems.size(); i++) {
                Tower T = purchaseItems.get(i);
                if(T.getRect().intersects(new Point(input.getMouseX(), input.getMouseY()))) {
                    selectedItem = i;
                    if(player.getMoney() >= purchaseItems.get(selectedItem).getCost()) {
                        itemSelected = true;
                    }
                }
            }
            // If item was selected previously, try to place it
        } else if (input.wasPressed(MouseButtons.LEFT) && itemSelected){
            // Place item if current position is valid
            if(selectedItem == TANK) {
                // Purchase a tank
                Tank newItem = new Tank(currPos);
                tanks.add(newItem);
                // Deduct money from player
                player.setMoney(newItem.getCost());
            }
            if(selectedItem == SUPER_TANK) {
                // Purchase a tank
                SuperTank newItem = new SuperTank(currPos);
                superTanks.add(newItem);
                // Deduct money from player
                player.setMoney(newItem.getCost());
            }
            if(selectedItem == AIR_SUPPORT) {
                // Purchase a plane
                planeCount += 1;
                AirSupport newItem = new AirSupport(currPos, planeCount);
                airSupport.add(newItem);
                // Deduct money from player
                player.setMoney(newItem.getCost());
            }
            // Clear selection
            itemSelected = false;
        } else if (input.wasPressed(MouseButtons.RIGHT) && itemSelected) {
            // Clear selection
            itemSelected = false;
        }

        // If item has been selected then draw it at the mouse point
        if(itemSelected) {
            Image image = purchaseItems.get(selectedItem).getImage();
            boolean invalidPos = input.getMouseX() < 0 || input.getMouseX() > Window.getWidth() ||
                    input.getMouseY() < 0 || input.getMouseY() > Window.getHeight();
            // If mouse cursor is not outside the game window
            if(!invalidPos) {
                boolean validPos = true;
                // Check if position is over a valid item or not
                // Check if is over an invalid map item
                if (map.hasProperty((int) currPos.x, (int) currPos.y, "blocked")) {
                    validPos = false;
                }
                // Check if center intersects with any panel
                if (currPos.y <= BUY_PANEL_BORDER || currPos.y >= STATUS_PANEL_BORDER) {
                    validPos = false;
                }
                // Check if new tower intersects with any existing towers
                for(Tank T : tanks) {
                   if(T.getRect().intersects(currPos)) {
                       validPos = false;
                       break;
                   }
                }
                for(SuperTank T : superTanks) {
                    if(T.getRect().intersects(currPos)) {
                        validPos = false;
                        break;
                    }
                }
                // If the cursor is on top of a valid position draw image
                if (validPos) {
                    image.draw(currPos.x, currPos.y);
                }
            }
        }
    }
    //----------------------------------------- Wave related methods -------------------------------------------------//
    public void startLevel() {
        if(!waves.get(TOP).waveStatus()) {
            waves.get(TOP).startWave();
        }
    }

    private void loadNextWave() {
        waves.remove(TOP);
        statusPanel.increaseWave();
        System.out.println(isFinished);
    }

    //----------------------------------------- Tower related methods ------------------------------------------------//
    private void renderTowers() {
        // Tanks
        for(Tank T : tanks) {
            T.render();
        }
        // Super tanks
        for(SuperTank T : superTanks) {
            T.render();
        }
        // Render Airplane
        for(AirSupport T : airSupport) {
            T.render();
        }
        // Render existing explosives
        for(Explosive E: explosives) {
            E.render();
        }
    }

    private void flyAirplanes() {
        for(int i = airSupport.size() - 1; i >= 0; i--) {
            AirSupport plane = airSupport.get(i);
            plane.update();
            if(!plane.getStatus()) {
                airSupport.remove(i);
            }
            // Check if plane is dropping explosives or not
            if(plane.getLaunchStatus()) {
                // Add explosive in the current location of the plane
                explosives.add(new Explosive(plane.getCenter()));
            }
        }
    }

    private void updateExplosive() {
        for(int i = explosives.size()-1; i >= 0; i--) {
            Explosive E = explosives.get(i);
            E.update();
            if(E.isActive()) {
                // Deal damage

                // remove explosive
                explosives.remove(i);
            }
        }
    }
}
