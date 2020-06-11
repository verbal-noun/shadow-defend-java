import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.List;

/**
 * The type Slicer.
 */
public class Slicer extends Sprite{

    /**
     * The constant SPEED.
     */
    protected static final double SPEED = 2;
    /**
     * The constant DEF_HEALTH.
     */
    protected static final int DEF_HEALTH = 1;
    /**
     * The constant REWARD.
     */
    protected static final int REWARD = 2;
    /**
     * The constant PENALTY.
     */
    protected static final int PENALTY = 1;
    /**
     * The Health.
     */
    protected int health;
    /**
     * The Speed.
     */
    protected double speed;
    /**
     * The Polyline.
     */
    protected final List<Point> polyline;
    /**
     * The Target point index.
     */
    protected int targetPointIndex;
    /**
     * The Finished.
     */
    protected boolean finished;
    /**
     * The Killed.
     */
    protected boolean killed;
    /**
     * The Reward.
     */
    protected int reward;
    /**
     * The Penalty.
     */
    protected int penalty;
    /**
     * The Has children.
     */
    protected boolean hasChildren;
    /**
     * The Child no.
     */
    protected int childNo;

    /**
     * Creates a new Slicer
     *
     * @param polyline   The polyline that the slicer must traverse (must have at least 1 point)
     * @param slicerType the slicer type
     */
    public Slicer(List<Point> polyline, String slicerType) {
        super(polyline.get(0), slicerType);
        this.polyline = polyline;
        this.targetPointIndex = 1;
        this.finished = false;
        this.health = DEF_HEALTH;
        this.speed = SPEED;
        this.reward = REWARD;
        this.killed = false;
        this.penalty = PENALTY;
        this.hasChildren = false;
        this.childNo = 0;
    }

    /**
     * Gets health.
     *
     * @return the health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Update.
     *
     * @param input the input
     */
    public void update(Input input) {
        if(finished) {
            return;
        }

        // Obtain where we are and our target
        Point currentPoint = getCenter();
        Point targetPoint = polyline.get(targetPointIndex);
        // Convert them to vectors to perform calculations
        Vector2 target = targetPoint.asVector();
        Vector2 current = currentPoint.asVector();
        Vector2 distance = target.sub(current);
        // Pixel distance from current and target position
        double magnitude = distance.length();

        // Check if we are close to target than our step size
        if(magnitude < speed * ShadowDefend.getTimescale()) {
            // Check if we have reached the end
            if(targetPointIndex == polyline.size() - 1) {
                finished = true;
                return;
            } else {
                // Make the next point out target
                targetPointIndex += 1;
            }
        }

        // Move towards the target point
        // We do this by getting a unit vector in the direction of our target, and multiplying it
        // by the speed of the slicer (accounting for the timescale)
        super.move(distance.normalised().mul(speed * ShadowDefend.getTimescale()));
        // update current rotation angle to face target point
        setAngle(Math.atan2(targetPoint.y - currentPoint.y, targetPoint.x - currentPoint.x));
        //super.update(input);
        render();
    }

    /**
     * Is finished boolean.
     *
     * @return the boolean
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Reduce health.
     *
     * @param hitPoints the hit points
     */
    public void reduceHealth(int hitPoints) {
        health -= hitPoints;
        if(health <= 0) {
            killed = true;
            finished = true;
        }
    }

    /**
     * Is killed boolean.
     *
     * @return the boolean
     */
// Method to determine whether the slicer was killed or not
    public boolean isKilled() { return  killed; }

    /**
     * Give reward int.
     *
     * @return the int
     */
    public int giveReward() { return reward; }

    /**
     * Gets penalty.
     *
     * @return the penalty
     */
//Get the damage dealt by the slicer
    public int getPenalty() { return penalty;}

    /**
     * Gets child num.
     *
     * @return the child num
     */
    public int getChildNum() { return childNo; }

    /**
     * Sets target index.
     *
     * @param index the index
     */
    public void setTargetIndex(int index) {
        this.targetPointIndex = index;
    }

    /**
     * Gets target index.
     *
     * @return the target index
     */
    public int getTargetIndex() {
        return targetPointIndex;
    }

    /**
     * Sets position.
     *
     * @param position the position
     */
    public void setPosition(Point position) {
        rect = image.getBoundingBoxAt(position);
    }
}

