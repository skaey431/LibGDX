package LibGDX.TEST.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class BaseEntity {


    protected Vector2 position;
    protected Rectangle hitbox;
    protected float width, height;

    public BaseEntity(float x, float y, float width, float height) {
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.hitbox = new Rectangle(x, y, width, height);
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    protected void updateHitbox() {
        hitbox.setPosition(position.x, position.y);
    }

    public abstract void update(float delta);

    public abstract void render(SpriteBatch batch);

    public abstract void dispose();
    public abstract void check();
}
