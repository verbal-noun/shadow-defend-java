import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.Random;

/**
 *  Class for airplanes
 *  Passive tower hence extends from tower
 */
public class AirSupport extends Tower {
    private static final int PRICE = 500;
    private static final String IMAGE = "res/images/airsupport.png";
    private static final String FLYING_HORIZONTAL = "Horizontal";
    private static final String FLYING_VERTICAL = "Vertical";
    private static final double SPEED = 5;
    // Attributes to determine direction and orientation
    private static final int OFFSET = 25;
    private static final int HORIZONTAL = 90;
    private static final int VERTICAL = 180;
    private static final int UPPERBOUND = 4;
    private static final int FPS = 60;
    private String flyDirection;
    private final Point currPos;
    private boolean status;
    private int dropTime;
    private static final Random GENERATOR = new Random();
    // Attribute to determine whether bomb is launched or not
    private boolean launchStatus;
    private int frameCount;

    /**
     * Instantiates a new airplane
     *
     * @param point   The point where the place was placed
     * @param planeNo Determines the direction the plane will fly
     */
    public AirSupport(Point point, int planeNo) {
        super(point, IMAGE, PRICE);
        // Set the direction of the plane
        currPos = setDirection(point, planeNo);
        // Get new an image and bounding box at new location
        this.rect = image.getBoundingBoxAt(currPos);
        this.status = true;
        this.dropTime = GENERATOR.nextInt(UPPERBOUND);
        this.launchStatus = false;
        frameCount = 0;
    }

    /**
     * Sets the direction of the plane
     * Helps plane to position at the proper end of the window
     *
     * @param point  The point where the place was placed
     * @param planeNo Determines the direction the plane will fly
     */
    private Point setDirection(Point point, int planeNo) {
        // Determining on number of plane set the direction
        Point pos;
        if(planeNo % 2 == 0) {
            // Fly vertically
            pos = new Point(point.x, -OFFSET);
            flyDirection = FLYING_VERTICAL;
            this.angle = Math.toRadians(VERTICAL);
        } else {
            // Fly horizontally
            pos = new Point(-OFFSET, point.y);
            flyDirection = FLYING_HORIZONTAL;
            this.angle = Math.toRadians(HORIZONTAL);
        }
        return pos;
    }

    /**
     * Update the status of the air plane
     */
    @Override
    public void update() {
        frameCount += ShadowDefend.getTimescale();
        // Move its position according to its direction
        Point targetPoint;
        // Set the target point according to its fly direction
        if(flyDirection.equals(FLYING_HORIZONTAL)) {
            targetPoint = new Point(getCenter().x + SPEED, getCenter().y);

        } else {
            targetPoint = new Point(getCenter().x, getCenter().y + SPEED);
        }
        // Convert them to vectors to perform calculations
        Vector2 target = targetPoint.asVector();
        Vector2 current = currPos.asVector();
        Vector2 distance = target.sub(current);
        // Pixel distance from current and target position
        if(status) {
            super.move(distance.normalised().mul(SPEED * ShadowDefend.getTimescale()));
        }
        // Check if plane has reached its destination
        if(getCenter().x >= Window.getWidth() || getCenter().y >= Window.getHeight()) {
            status = false;
        }
        // If bomb has been dropped, change launch status
        if(launchStatus) {
            launchStatus = false;
        }
        // Drop bomb when drop time is reached
        if(frameCount / FPS >= dropTime) {
            dropExplosive();
            frameCount = 0;
        }
    }

    /**
     * Gets the status whether the plane has completed its journey or not.
     *
     * @return the status
     */
    public boolean getStatus() { return status; }

    /**
     * Gets whether the plane is ready to launch explosives.
     *
     * @return the launch status.
     */
    public boolean getLaunchStatus() {  return launchStatus; }

    /**
     * Drop explosive by signalling plane is ready .
     */
    public void dropExplosive() {
        // Check if drop time is reached or not
        launchStatus = true;
        // Generate new random cooldown period
        dropTime = GENERATOR.nextInt(UPPERBOUND);
    }
}
