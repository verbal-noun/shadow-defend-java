import bagel.map.TiledMap;

public class Level {
    private final TiledMap map;

    public Level(int currentLevel) {
        String MAP_FILE = String.format("res/levels/%s.tmx", currentLevel);
        this.map = new TiledMap(MAP_FILE);
    }
}
