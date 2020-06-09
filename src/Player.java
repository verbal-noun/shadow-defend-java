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

    public void setMoney(int money) {
        this.money = money;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) { this.lives = lives; }
}
