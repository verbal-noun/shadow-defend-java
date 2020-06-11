import bagel.Image;
import bagel.Input;
import bagel.MouseButtons;
import bagel.Window;
import bagel.map.TiledMap;
import bagel.util.Point;
import bagel.util.Vector2;

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
    private static final int HORIZONTAL = 90;
    // Status messages
    private static final Integer WIN = 0;
    private static final Integer PLACING = 1;
    private static final Integer WAVE = 2;
    private static final Integer WAIT = 3;
    // Map attribute for the level
    private final TiledMap map;
    private final List<Point> polyline;
    // File containing wave information
    private static final String WAVE_FILE = "res/levels/test-waves.txt";
    private List<Wave> waves = new ArrayList<>();
    // Player for the level
    private Player player;
    // Panels
    private BuyPanel buyPanel;
    private StatusPanel statusPanel;
    private boolean finished;
    // Attributes related to purchase
    private boolean itemSelected;
    private List<Tower> purchaseItems;
    private int selectedItem;
    private List<Tank> tanks;
    private List<SuperTank> superTanks;
    private List<AirSupport> airSupport;
    private int planeCount;
    private List<Explosive> explosives;
    private List<Projectile> tankProjectiles;
    private List<SuperProjectile> superProjectiles;
    private Wave currWave;
    private int waveNo;
    private boolean playerKilled;

    // Constructor
    public Level(int currentLevel) {
        // Load level map
        String MAP_FILE = String.format("res/levels/%s.tmx", currentLevel);
        this.map = new TiledMap(MAP_FILE);
        this.polyline = map.getAllPolylines().get(0);
        // Instantiate the player
        this.player = new Player();
        this.playerKilled = false;
        // Instantiate panels
        this.buyPanel = new BuyPanel(500);
        this.purchaseItems = buyPanel.getPurchaseItems();
        this.statusPanel = new StatusPanel();
        this.finished = false;
        // Load waves
        loadWave();
        this.itemSelected = false;
        this.waveNo = 1;
        // Load level defence
        tanks = new ArrayList<>();
        superTanks = new ArrayList<>();
        airSupport = new ArrayList<>();
        this.planeCount = 0;
        this.explosives = new ArrayList<>();
        this.superProjectiles = new ArrayList<>();
        this.tankProjectiles = new ArrayList<>();
    }

    //------------------------------------------- Level methods ------------------------------------------------------//
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
        }
        // Signal end of wave if all waves are finished
        if (waves.size() == 0) {
            finished = true;
            statusPanel.setGameStatus(WIN);
            return;
        }
        // Update state towers and ammunition of the level
        updateDefense();
        // Attack enemies when in range and waves remaining
        if(waves.size() > 0) { attackEnemy(); }
        // Control player interaction
        playerInteraction(input);
        // Check if player has died or not
        if(player.getLives() <= 0) {
            playerKilled = true;
        }
    }

    public boolean isFinished() { return finished;}

    public boolean isPlayerKilled() { return playerKilled; }

    //------------------------------------------ Panel related methods -----------------------------------------------//

    // Update the status panel with proper information
    private void updatePanels() {
        // Status panel related configurations
        statusPanel.setTimeScale();
        // Set the game status
        if(itemSelected) {
            statusPanel.setGameStatus(PLACING);
        } else if (waves.size() > 0 && waves.get(TOP).waveStatus()) {
            statusPanel.setGameStatus(WAVE);
        } else if(!finished) {
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
            // Check if the current position is valid or not
            boolean validPos = checkPosValidity(currPos, input);
            // Place item if current position is valid
            if(validPos) {
                if(selectedItem == TANK) {
                    // Purchase a tank
                    Tank newItem = new Tank(currPos);
                    tanks.add(newItem);
                    // Deduct money from player
                    player.reduceMoney(newItem.getCost());
                }
                if(selectedItem == SUPER_TANK) {
                    // Purchase a tank
                    SuperTank newItem = new SuperTank(currPos);
                    superTanks.add(newItem);
                    // Deduct money from player
                    player.reduceMoney(newItem.getCost());
                }
                if(selectedItem == AIR_SUPPORT) {
                    // Purchase a plane
                    planeCount += 1;
                    AirSupport newItem = new AirSupport(currPos, planeCount);
                    airSupport.add(newItem);
                    // Deduct money from player
                    player.reduceMoney(newItem.getCost());
                }
                // Clear selection
                itemSelected = false;
            }
        } else if (input.wasPressed(MouseButtons.RIGHT) && itemSelected) {
            // Clear selection
            itemSelected = false;
        }

        // If item has been selected then draw it at the mouse point
        if(itemSelected) {
            boolean validPos = checkPosValidity(currPos, input);
            // If the cursor is on top of a valid position draw image
            if (validPos) {
                Image image = purchaseItems.get(selectedItem).getImage();
                image.draw(currPos.x, currPos.y);
            }
        }

    }

    private boolean checkPosValidity(Point currPos, Input input) {
        // Check if current cursor location is outside game window
        boolean invalidPos = input.getMouseX() < 0 || input.getMouseX() > Window.getWidth() ||
                input.getMouseY() < 0 || input.getMouseY() > Window.getHeight();

        boolean validPos = true;
        // If mouse cursor is not outside the game window
        if(!invalidPos) {

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
            for (Tank T : tanks) {
                if (T.getRect().intersects(currPos)) {
                    validPos = false;
                    break;
                }
            }
            for (SuperTank T : superTanks) {
                if (T.getRect().intersects(currPos)) {
                    validPos = false;
                    break;
                }
            }
        }
        return validPos;
    }
    //----------------------------------------- Wave related methods -------------------------------------------------//

    // Load waves from waves file and fill events
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
                    waves.add(new Wave(polyline, player));
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
    // Initiate the game level
    public void startLevel() {
        if(!waves.get(TOP).waveStatus()) {
            waves.get(TOP).startWave();
        }
    }
    // Load next wave of the level if any
    private void loadNextWave() {
        waves.remove(TOP);
        player.addMoney(100 * waveNo + 150);
        waveNo += 1;
        statusPanel.increaseWave();
    }

    //----------------------------------------- Tower related methods ------------------------------------------------//

    // Draw the towers of the level
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
    // Update the state of towers in the game
    private void updateDefense() {
        // Update the active towers
        updateTanks(tanks);
        updateTanks(superTanks);
        // Update any projectiles or explosives
        updateExplosive();
        updateProjectiles(tankProjectiles);
        updateProjectiles(superProjectiles);
        // Update passive towers
        flyAirplanes();
    }
    // Update the state of active towers
    private <T extends  Tower> void updateTanks(List<T> towers) {
        for(int i = towers.size() - 1; i >= 0; i--) {
            T tower = towers.get(i);
            tower.update();
        }
    }

    // Update projectiles launched by active towers
    private <T extends Projectile<Slicer>> void updateProjectiles(List<T> projectiles) {
        for(int i = projectiles.size() - 1; i >= 0; i--) {
            T projectile = projectiles.get(i);
            projectile.update();
            if(projectile.isTargetHit()) {
                projectiles.remove(i);
            }
        }
    }

    // Fly the planes purchased by the player
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

    //------------------------------------------ Projectile related methods ------------------------------------------//

    // Update the status of level's explosives
    private void updateExplosive() {
        for(int i = explosives.size()-1; i >= 0; i--) {
            Explosive E = explosives.get(i);
            E.update();
            if(E.isActive()) {
                // Deal damage to all slicers in the area
                explodeEnemy(waves.get(TOP).getSlicers(), E);
                explodeEnemy(waves.get(TOP).getSuperSlicers(), E);
                explodeEnemy(waves.get(TOP).getMegaSlicers(), E);
                explodeEnemy(waves.get(TOP).getApexSlicers(), E);
                // remove explosive
                explosives.remove(i);
            }
        }
    }
    // Method to deal damage to slicers by explosives
    private <T extends Slicer> void explodeEnemy(List<T> enemies, Explosive bomb) {
        for(T enemy : enemies) {
            Vector2 distance = enemy.getCenter().asVector().sub(bomb.getCenter().asVector());
            if(distance.length() <= bomb.getRadius()) {
                enemy.reduceHealth(bomb.dealDamage());
            }
        }
    }
    // Method to fire at enemies when in range by active towers
    private void attackEnemy() {
        // Position and fire tanks
        for(Tank tank : tanks) {
            // Position tank towards enemy in range
            detectEnemy(tank);
        }
        // Position and fire tanks
        for(SuperTank supTank : superTanks) {
            // Position tank towards enemy in range
            detectEnemy(supTank);
        }
    }

    private <T extends Tower> void detectEnemy(T tank) {
        tank.setEnemyDetected(false);
        // Load all the apex slicers of the wave if any
        searchEnemy(waves.get(TOP).getApexSlicers(), tank);
        // load all the mega slicers of the wave if any
        if(!tank.isEnemyDetected()) {
            searchEnemy(waves.get(TOP).getSuperSlicers(), tank);
        }
        searchEnemy(waves.get(TOP).getMegaSlicers(), tank);
        // Load all the super slicers of the wave if any
        if(!tank.isEnemyDetected()) {
            searchEnemy(waves.get(TOP).getSuperSlicers(), tank);
        }
        // Load all the regular slicers if any
        if(!tank.isEnemyDetected()) {
            searchEnemy(waves.get(TOP).getSlicers(), tank);
        }

    }

    // Method to detect enemy in range
    private <E extends Slicer, T extends Tower> void searchEnemy(List<E> enemyList, T tower) {
        // Determine if any enemy exists within range of tower
        for(E enemy : enemyList) {
            Vector2 distance = enemy.getCenter().asVector().sub(tower.getCenter().asVector());
            if(distance.length() <= tower.getRadius()) {
                // Signal enemy is detected
                tower.setEnemyDetected(true);
                // Turn tower towards enemy
                double angle = Math.atan2(enemy.getCenter().y - tower.getCenter().y,
                        enemy.getCenter().x - tower.getCenter().x);
                tower.setAngle(angle + HORIZONTAL);
                // If cooldown period is over then shoot
                if(tower.isWeaponsHot()) {
                    shootEnemy(enemy, tower);
                }
            }
            // Target locked
            break;
        }
    }

    // A method to facilitate shooting of enemy slicers
    private <E extends Slicer, T extends Tower> void shootEnemy(E enemy, T tower) {
        // Determine type of tower and launch projectile accordingly
        if(tower.getClass() == Tank.class) {
            tankProjectiles.add(new Projectile<E>(tower.getCenter(), enemy));
            tower.startCooldown();
        }
        if(tower.getClass() == SuperTank.class) {
            superProjectiles.add(new SuperProjectile<E>(tower.getCenter(), enemy));
            tower.startCooldown();
        }
    }
}
