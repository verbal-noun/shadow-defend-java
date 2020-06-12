import bagel.util.Point;
import bagel.util.Vector2;
import java.util.List;

/**
 * The type Slicer.
 */
public class Slicer extends Sprite{

    protected static final double SPEED = 2;
    protected static final int DEF_HEALTH = 1;
    protected static final int REWARD = 2;
    protected static final int PENALTY = 1;
    private static final int CHILD_NUM = 0;
    // Attributes related to basic features
    protected int health;
    protected double speed;
    protected int reward;
    protected int penalty;
    protected int childNo;
    // The map and the target
    protected final List<Point> polyline;
    protected int targetPointIndex;
    // Attributes to signal the state of the slicer
    protected boolean finished;
    protected boolean killed;
    protected boolean hasChildren;

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
        this.childNo = CHILD_NUM;
    }

    /**
     * Gets remaining health of the slicer.
     *
     * @return the health
     */
    public int getHealth() {
        return health;
    }

    /**
     * Update status of the slicer
     *
     */
    public void update() {
        // If slicer has reached the end or has been killed
        if(finished) {
            return;
        }

        /**
         * NOTE: the movement logic has been implemented from Project 1 solution
         *      I acknowledge the following code is not mine.
         */
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
     * Checks if the slicer is finished or not
     *
     * @return the boolean
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Reduce health of the slicer by the given hitpoints.
     *
     * @param hitPoints - the amount health will be reduced by
     */
    public void reduceHealth(int hitPoints) {
        health -= hitPoints;
        if(health <= 0) {
            killed = true;
            finished = true;
        }
    }

    /**
     * Checks if the slicer is killed or not
     *
     * @return the boolean
     */
    public boolean isKilled() { return  killed; }

    /**
     * Give reward int.
     *
     * @return the int
     */
    public int giveReward() { return reward; }

    /**
     * Gets penalty associated with the slicer.
     *
     * @return the penalty
     */
    public int getPenalty() { return penalty;}

    /**
     * Gets children number current slicer
     *
     * @return childNo - the number of children of the slicer
     */
    public int getChildNum() { return childNo; }

    /**
     * Sets target index of the polyline the slicer will follow
     * Method used to spawn children in the same point as the parent
     *
     * @param index - the target index in the polyline
     */
    public void setTargetIndex(int index) {
        this.targetPointIndex = index;
    }

    /**
     * Gets target index of the current slicer
     *
     * @return the target index
     */
    public int getTargetIndex() {
        return targetPointIndex;
    }

    /**
     * Sets position to a given point
     *
     * @param position - the Point which be set at the slicer's current position
     */
    public void setPosition(Point position) {
        rect = image.getBoundingBoxAt(position);
    }
}

