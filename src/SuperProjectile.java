import bagel.Image;
import bagel.util.Point;

/**
 * The type Super projectile.
 *
 * @param <T> the type parameter for enemy.
 */
public class SuperProjectile<T extends Slicer> extends Projectile {
    private static final String IMAGE = "res/images/supertank_projectile.png";
    private static final int SUPERTANK_DAMAGE = 3;

    /**
     * Creates a new Sprite (game entity)
     *
     * @param point  The starting point for the entity
     * @param target the target
     */
    public SuperProjectile(Point point, T target) {
        super(point, target);
        this.image = new Image(IMAGE);
        this.rect = image.getBoundingBoxAt(point);
        this.damage = SUPERTANK_DAMAGE;
    }
}
