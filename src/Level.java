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

    // Map attribute for the level
    private final TiledMap map;
    private final List<Point> polyline;
    // File containing wave information
    private static final String WAVE_FILE = "res/levels/waves.txt";
    public List<Wave> waves = new ArrayList<>();
    // Player for the level
    private Player player;

    // Panels
    private BuyPanel buyPanel;
    private StatusPanel statusPanel;

    public Level(int currentLevel) {
        String MAP_FILE = String.format("res/levels/%s.tmx", currentLevel);
        this.map = new TiledMap(MAP_FILE);
        this.polyline = map.getAllPolylines().get(0);
        this.player = new Player();
        this.buyPanel = new BuyPanel(500);
        this.statusPanel = new StatusPanel();
        // Load waves
        loadWave();
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
                waves.get(waveNo-1).addEvent(new Event(line.substring(index+1).split(",")));
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
        // Draw the panels
        buyPanel.renderPanel();
        statusPanel.renderPanel();
    }

    //----------------------------------------- Panel related methods ------------------------------------------------//
    public void updateTime() {
        statusPanel.setTimeScale();
    }

    //----------------------------------------- Wave related methods -------------------------------------------------//
    public void startLevel() {
        waves.get(0).startWave();
    }
}
