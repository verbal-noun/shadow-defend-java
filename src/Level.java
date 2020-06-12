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


/**
 * The level class
 * This class acts as a hub for all the game entities to interact
 * Level drives the game and controls the state of game entities
 */
public class Level {
    // Dimensions
    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    private static final int TOP = 0;
    private static final int BUY_PANEL_BORDER = 100;
    private static final int STATUS_PANEL_BORDER = 675;
    // Attributes to indicate tower
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
    private static final String WAVE_FILE = "res/levels/waves.txt";
    private final List<Wave> waves = new ArrayList<>();
    // Player for the level
    private final Player player;
    // Panels
    private final BuyPanel buyPanel;
    private final StatusPanel statusPanel;
    private boolean finished;
    // Attributes related to purchase
    private boolean itemSelected;
    // Attributes for level's defense
    private final List<Tower> purchaseItems;
    private int selectedItem;
    private final List<Tank> tanks;
    private final List<SuperTank> superTanks;
    private final List<AirSupport> airSupport;
    private int planeCount;
    private final List<Explosive> explosives;
    private final List<Projectile> tankProjectiles;
    private final List<SuperProjectile> superProjectiles;
    private int waveNo;
    private boolean playerKilled;

    /**
     * Instantiates a new Level.
     *
     * @param currentLevel the current level
     */
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

    /**
     * Render all the elements of the game
     */
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

    /**
     * Update level.
     *
     * @param input the input
     */
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
        if(waves.size() > 0) {
            attackEnemy();
        }
        // Control player interaction
        playerInteraction(input);
        // Check if player has died or not
        if(player.getLives() <= 0) {
            playerKilled = true;
        }
    }

    /**
     * Method to return if the game is finished or not
     *
     * @return the boolean signifying game's condition
     */
    public boolean isFinished() { return finished;}

    /**
     * Is player killed boolean.
     *
     * @return the boolean to indicate if player is killed.
     */
    public boolean isPlayerKilled() { return playerKilled; }

    //------------------------------------------ Panel related methods -----------------------------------------------//

    /**
     * Update the buy and status panels of the game
     */
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

    /**
     * Method to facilitate tower purchase and placement
     * Tracks input from the player and updates accordingly
     *
     * @param input - The input coming from the player
     */
    private void playerInteraction(Input input) {
        // Point to track current position of the cursor
        Point currPos = new Point(input.getMouseX(), input.getMouseY());
        // Check if left click was pressed or not
        // If mouse left is pressed but no item has been selected from before
        if(input.wasPressed(MouseButtons.LEFT) && !itemSelected) {
            // If item hasn't been selected, check where mouse was pressed
            for(int i = 0; i < purchaseItems.size(); i++) {
                Tower T = purchaseItems.get(i);
                if(T.getRect().intersects(new Point(input.getMouseX(), input.getMouseY()))) {
                    selectedItem = i;
                    // If player has enough money to buy it, select item
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
         // If mouse right was pressed then clear selections if any
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

    /**
     * A method to check if the current position is a valid area for placing a new tower
     * This method notifies whether an item should be drawn under cursor and place when
     * selected.
     *
     * @param currPos - the position on which the mouse is hovering
     * @param input - Player input
     * @return - returns whether currPos is on a valid position or not
     */
    private boolean checkPosValidity(Point currPos, Input input) {
        // Check if current cursor location is outside game window
        boolean invalidPos = input.getMouseX() < 0 || input.getMouseX() > Window.getWidth() ||
                input.getMouseY() < 0 || input.getMouseY() > Window.getHeight();

        // If mouse cursor is not outside the game window
        if(!invalidPos) {

            // Check if position is over a valid item or not
            // Check if is over an invalid map item
            if (map.hasProperty((int) currPos.x, (int) currPos.y, "blocked")) {
                return false;
            }
            // Check if center intersects with any panel
            if (currPos.y <= BUY_PANEL_BORDER || currPos.y >= STATUS_PANEL_BORDER) {
                return false;
            }
            // Check if new tower intersects with any existing towers
            for (Tank T : tanks) {
                if (T.getRect().intersects(currPos)) {
                    return false;
                }
            }
            for (SuperTank T : superTanks) {
                if (T.getRect().intersects(currPos)) {
                    return false;
                }
            }
        }
        // If all the conditions have been met, return true
        return true;
    }

    //----------------------------------------- Wave related methods -------------------------------------------------//

    /**
     * A method to load the waves from the waves.txt file.
     * This creates the appropriate number of waves as well as
     * allocates the events to corresponding waves.
     */
    private void loadWave() {
        BufferedReader reader;
        // Read lines from waves.txt file
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

    /**
     * Start level.
     */
    public void startLevel() {
        // Condition to ensure another wave isn't started while a wave is active
        if(!waves.get(TOP).waveStatus()) {
            waves.get(TOP).startWave();
        }
    }

    /**
     * Load the next wave in the list
     * Allocates proper reward to player for passing the wave too.
     */
    private void loadNextWave() {
        waves.remove(TOP);
        player.addMoney(100 * waveNo + 150);
        waveNo += 1;
        statusPanel.increaseWave();
    }

    //----------------------------------------- Tower related methods ------------------------------------------------//

    /**
     * Update the defense of the level
     * This includes passive and active towers as well as any projectiles or explosives
     */
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

    /**
     * Render towers player has purchased so far.
     */
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

    /**
     * A method to update the game state of any active tower player has purchased.
     *
     * @param towers - a list of towers
     * @param <T> - The class type of the towers
     */
    private <T extends  Tower> void updateTanks(List<T> towers) {
        for(int i = towers.size() - 1; i >= 0; i--) {
            T tower = towers.get(i);
            tower.update();
        }
    }

    /**
     * Update the different type of projectiles active in the map
     *
     * @param projectiles - A list of projectiles
     * @param <T> - The class type of projectiles.
     */
    private <T extends Projectile<Slicer>> void updateProjectiles(List<T> projectiles) {
        for(int i = projectiles.size() - 1; i >= 0; i--) {
            T projectile = projectiles.get(i);
            projectile.update();
            // If projectile has hit its target remove it
            if(projectile.isTargetHit()) {
                projectiles.remove(i);
            }
        }
    }

    /**
     * Update the state of airsupport player has purchased
     */
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

    /**
     * Update the game state of explosives
     */
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

    /**
     * Method to deal damage to the enemy
     *
     * @param enemies - List of enemies to searched
     * @param bomb - The bomb
     * @param <T> - The class of enemy
     */
    private <T extends Slicer> void explodeEnemy(List<T> enemies, Explosive bomb) {
        for(T enemy : enemies) {
            Vector2 distance = enemy.getCenter().asVector().sub(bomb.getCenter().asVector());
            if(distance.length() <= bomb.getRadius()) {
                enemy.reduceHealth(bomb.dealDamage());
            }
        }
    }

    /**
     * Method to facilitate active towers of enemies
     *
     */
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

    /**
     * Detect enemy for a given active tower
     * If enemy is in range, facilitate attacking of tower
     *
     * @param tank - The tower which will look for enemies
     * @param <T> - The class type of tower
     */
    private <T extends Tower> void detectEnemy(T tank) {
        tank.setEnemyDetected(false);
        // Load all the apex slicers of the wave if any
        searchEnemy(waves.get(TOP).getApexSlicers(), tank);
        // load all the mega slicers of the wave if any
        if(!tank.isEnemyDetected()) {
            searchEnemy(waves.get(TOP).getMegaSlicers(), tank);
        }
        // Load all the super slicers of the wave if any
        if(!tank.isEnemyDetected()) {
            searchEnemy(waves.get(TOP).getSuperSlicers(), tank);
        }
        // Load all the regular slicers if any
        if(!tank.isEnemyDetected()) {
            searchEnemy(waves.get(TOP).getSlicers(), tank);
        }

    }

    /**
     * Method to detect enemy in range and shoot the enemy
     *
     * @param enemyList - List of active slicers in the map
     * @param tower - The tower which will look for enemies
     * @param <E> - The type of slicer
     * @param <T> - The type of tower
     */
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
                // Target locked
                break;
            }

        }
    }

    /**
     * A method to shoot the enemy which is in range
     * This method create appropriate projectiles and adds them to the game
     *
     * @param enemy - The enemy which is in target
     * @param tower - The tower which has detected the enemy
     * @param <E> - The Type of slicer
     * @param <T> - The type of tower
     */
    private <E extends Slicer, T extends Tower> void shootEnemy(E enemy, T tower) {
        // Determine type of tower and launch projectile accordingly
        if(tower.getClass() == Tank.class) {
            tankProjectiles.add(new Projectile<>(tower.getCenter(), enemy));
            tower.startCooldown();
        }
        if(tower.getClass() == SuperTank.class) {
            superProjectiles.add(new SuperProjectile<>(tower.getCenter(), enemy));
            tower.startCooldown();
        }
    }
}
