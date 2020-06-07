public class Player {
    private int money;
    private static int lives;


    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public static int getLives() {
        return lives;
    }

    public static void setLives(int lives) {
        Player.lives = lives;
    }

    // Constructor
    public Player() {
        this.money = 500;
        lives = 25;
    }
}
