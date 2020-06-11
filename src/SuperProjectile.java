import bagel.Image;
import bagel.util.Point;

public class SuperProjectile<T extends Slicer> extends Projectile {
    private static final String IMAGE = "res/images/supertank_projectile.png";
    private static final int SUPERTANK_DAMAGE = 1;
    /**
     * Creates a new Sprite (game entity)
     *
     * @param point The starting point for the entity
     */
    public SuperProjectile(Point point, T target) {
        super(point, target);
        this.image = new Image(IMAGE);
        this.rect = image.getBoundingBoxAt(point);
        this.damage = SUPERTANK_DAMAGE;
    }
}
