import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.Random;

public class AirSupport extends Tower {
    private static final int PRICE = 500;
    private static final String IMAGE = "res/images/airsupport.png";
    private static final String FLYING_HORIZONTAL = "Horizontal";
    private static final String FLYING_VERTICAL = "Vertical";
    private static final double SPEED = 5;
    private static final int OFFSET = 25;
    private static final int HORIZONTAL = 90;
    private static final int VERTICAL = 180;
    private static final int UPPERBOUND = 4;
    private static final int FPS = 60;
    private String flyDirection;
    private Point currPos;
    private boolean status;
    private int dropTime;
    private static final Random GENERATOR = new Random();
    private boolean launchStatus;
    private int frameCount;

    // Constructor
    public AirSupport(Point point, int planeNo) {
        super(point, IMAGE, PRICE);
        // Set the direction of the plane
        currPos = setDirection(point, planeNo);
        this.rect = image.getBoundingBoxAt(currPos);
        this.status = true;
        this.dropTime = GENERATOR.nextInt(UPPERBOUND);
        this.launchStatus = false;
        frameCount = 0;
    }

    private Point setDirection(Point point, int planeNo) {
        // Determining on number of plane set the direction
        Point pos;
        if(planeNo % 2 == 0) {
            // Fly vertically
            pos = new Point(point.x, 0);
            flyDirection = FLYING_VERTICAL;
            this.angle = Math.toRadians(VERTICAL);
        } else {
            // Fly horizontally
            pos = new Point(0, point.y);
            flyDirection = FLYING_HORIZONTAL;
            this.angle = Math.toRadians(HORIZONTAL);
        }
        return pos;
    }

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

        if(launchStatus) {
            launchStatus = false;
        }
        // Drop bomb when drop time is reached
        if(frameCount / FPS >= dropTime) {
            dropExplosive();
            frameCount = 0;
        }
    }

    // Method to determine if plane is still active in the map or not
    public boolean getStatus() { return status; }
    // Method to signal if plane is dropping explosives or not
    public boolean getLaunchStatus() {  return launchStatus; }
    // Method to delegate dropping explosives onto the map
    public void dropExplosive() {
        // Check if drop time is reached or not
        launchStatus = true;
        dropTime = GENERATOR.nextInt(UPPERBOUND);
    }
}
