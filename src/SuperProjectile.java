import bagel.Image;
import bagel.util.Point;

public class SuperProjectile extends Projectile {
    private static final String IMAGE = "res/images/supertank_projectile.png";
    /**
     * Creates a new Sprite (game entity)
     *
     * @param point The starting point for the entity
     */
    public SuperProjectile(Point point) {
        super(point);
        this.image = new Image(IMAGE);
        this.rect = image.getBoundingBoxAt(point);
    }
}
