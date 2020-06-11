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

    /**
     * The Image.
     */
    protected Image image;
    /**
     * The Rect.
     */
    protected Rectangle rect;
    /**
     * The Angle.
     */
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
     * Gets rect.
     *
     * @return the rect
     */
    public Rectangle getRect() { return new Rectangle(rect); }

    /**
     * Move.
     *
     * @param dx the dx
     */
    public void move(Vector2 dx) { rect.moveTo(rect.topLeft().asVector().add(dx).asPoint()); }

    /**
     * Gets center.
     *
     * @return the center
     */
    public Point getCenter() {return getRect().centre(); }

    /**
     * Sets angle.
     *
     * @param angle the angle
     */
    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Updates the Sprite. Default behaviour is to render the Sprite at its current position.
     */
    public void render() { image.draw(getCenter().x, getCenter().y, new DrawOptions().setRotation(angle)); }

}
