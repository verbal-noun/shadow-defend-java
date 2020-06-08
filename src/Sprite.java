import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

/**
 *  Sprite Abstract Class implementation taken from Project-1 Solution
 *  I acknowledge the implementation of this class is not mine.
 */
public abstract class Sprite {

    private final Image image;
    private final Rectangle rect;
    private double angle;

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

    public Rectangle getRect() { return new Rectangle(rect); }

    public void move(Vector2 dx) { rect.moveTo(rect.topLeft().asVector().add(dx).asPoint()); }

    public Point getCenter() {return getRect().centre(); }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Updates the Sprite. Default behaviour is to render the Sprite at its current position.
     */
    public void render() { image.draw(getCenter().x, getCenter().y, new DrawOptions().setRotation(angle)); }

}
