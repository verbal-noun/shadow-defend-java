import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Slicer extends Sprite{

    protected static final double SPEED = 2;
    protected static final int DEF_HEALTH = 1;
    protected static final int REWARD = 2;
    protected static final int PENALTY = 1;
    protected int health;
    protected double speed;
    protected final List<Point> polyline;
    protected int targetPointIndex;
    protected boolean finished;
    protected boolean killed;
    protected int reward;
    protected int penalty;
    protected boolean hasChildren;
    protected int childNo;
    /**
     * Creates a new Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
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

    public int getHealth() {
        return health;
    }

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

    public boolean isFinished() {
        return finished;
    }

    public void reduceHealth(int hitPoints) {
        health -= hitPoints;
        if(health <= 0) {
            killed = true;
            finished = true;
        }
    }

    // Method to determine whether the slicer was killed or not
    public boolean isKilled() { return  killed; }

    public int giveReward() { return reward; }
    //Get the damage dealt by the slicer
    public int getPenalty() { return penalty;}

    public int getChildNum() { return childNo; }

    public void setTargetIndex(int index) {
        this.targetPointIndex = index;
    }

    public int getTargetIndex() {
        return targetPointIndex;
    }

    public void setPosition(Point position) {
        rect = image.getBoundingBoxAt(position);
    }
}

