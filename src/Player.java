/**
 * The type Player.
 */
public class Player {
    private static final int MONEY = 500;
    private static final int LIVES = 25;
    private int money;
    private int lives;

    /**
     * Instantiates a new Player.
     */
    public Player() {
        this.money = MONEY;
        this.lives = LIVES;
    }

    /**
     * Gets money.
     *
     * @return the money
     */
    public int getMoney() {
        return money;
    }

    /**
     * Reduce money.
     *
     * @param amount the amount
     */
    public void reduceMoney(int amount) { money -= amount; }

    /**
     * Add money.
     *
     * @param amount the amount
     */
    public void addMoney(int amount) { money += amount; }

    /**
     * Gets lives.
     *
     * @return the lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Reduce lives.
     *
     * @param amount the amount
     */
    public void reduceLives(int amount) { lives -= amount; }
}
