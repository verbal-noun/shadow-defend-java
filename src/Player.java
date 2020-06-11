public class Player {
    private static final int MONEY = 500;
    private static final int LIVES = 25;
    private int money;
    private int lives;

    // Constructor
    public Player() {
        this.money = MONEY;
        this.lives = LIVES;
    }

    public int getMoney() {
        return money;
    }

    public void reduceMoney(int amount) { money -= amount; }

    public void addMoney(int amount) { money += amount; }
    // Returns the lives left by the player
    public int getLives() {
        return lives;
    }
    // Method to reduce lives of player
    public void reduceLives(int amount) { lives -= amount; }
}
