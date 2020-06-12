import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

/**
 * Sprite Abstract Class implementation taken from Project-1 Solution
 * I acknowledge the implementation of this class is not mine.
 */
public abstract class Sprite {

    protected Image image;
    protected Rectangle rect;
    protected double angle;

    /**
     * Creates a new Sprite (game entity)
     *
     * @param point    The starting point for the entity
     * @param imageSrc The image which will be rendered at the entity's point
     */
    public Sprite(Point point, String imageSrc) {
        this.image = new Image(imageSrc);
        this.rect = image.getBoundingBoxAt(point);
        this.angle = 0;
    }

    /**
     * Gets bounding box of the entity.
     *
     * @return the rect of class Rectangle.
     */
    public Rectangle getRect() { return new Rectangle(rect); }

    /**
     * Move the game entity by dx amount
     *
     * @param dx - the amount by which the entity needs to be moves
     */
    public void move(Vector2 dx) { rect.moveTo(rect.topLeft().asVector().add(dx).asPoint()); }

    /**
     * Gets center of the game entity
     *
     * @return the center as a Point object
     */
    public Point getCenter() {return getRect().centre(); }

    /**
     * Sets angle of the entity
     *
     * @param angle - the angle which will be set
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Updates the Sprite. Default behaviour is to render the Sprite at its current position.
     */
    public void render() { image.draw(getCenter().x, getCenter().y, new DrawOptions().setRotation(angle)); }

}
