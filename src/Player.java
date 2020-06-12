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
     * Gets money of current player.
     *
     * @return the money
     */
    public int getMoney() {
        return money;
    }

    /**
     * Reduce money of current player by given amount.
     *
     * @param amount - the amount money will be reduced
     */
    public void reduceMoney(int amount) { money -= amount; }

    /**
     * Add money to the exiting player amount
     *
     * @param amount - the amount that will be added.
     */
    public void addMoney(int amount) { money += amount; }

    /**
     * Gets lives remaining.
     *
     * @return the lives of player
     */
    public int getLives() {
        return lives;
    }

    /**
     * Reduce lives by a certain amount
     *
     * @param amount - the amount which will de deducted
     */
    public void reduceLives(int amount) { lives -= amount; }
}
