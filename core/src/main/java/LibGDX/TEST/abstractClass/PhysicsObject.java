package LibGDX.TEST.abstractClass;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Vector2;

public abstract class PhysicsObject {
    protected Vector2 position;
    protected Vector2 velocity;
    protected float width, height;

    protected float gravity = -600f;
    protected float moveSpeed = 100f;
    protected float jumpPower = 400f;
    protected boolean onGround = false;

    protected Rectangle hitbox;

    public PhysicsObject(float x, float y) {
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0, 0);
        this.hitbox = new Rectangle(x, y, width, height);
    }

    public void update(float delta, Array<Rectangle> walls) {
        // 중력
        velocity.y += gravity * delta;
        position.y += velocity.y * delta;
        updateHitbox();

        onGround = false;
        for (Rectangle wall : walls) {
            if (hitbox.overlaps(wall)) {
                if (velocity.y < 0) {
                    position.y = wall.y + wall.height;
                    velocity.y = 0;
                    onGround = true;
                } else if (velocity.y > 0) {
                    position.y = wall.y - height;
                    velocity.y = 0;
                }
                updateHitbox();
            }
        }

        // 좌우 이동
        position.x += velocity.x * delta;
        updateHitbox();

        for (Rectangle wall : walls) {
            if (hitbox.overlaps(wall)) {
                // 이동 전 위치로 복구
                position.x -= velocity.x * delta;
                updateHitbox();
                break;
            }
        }
    }

    public void moveLeft() {
        velocity.x = -moveSpeed;
    }

    public void moveRight() {
        velocity.x = moveSpeed;
    }

    public void stopMoving() {
        velocity.x = 0;
    }

    public void jump() {
        if (onGround) {
            velocity.y = jumpPower;
            onGround = false;
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    private void updateHitbox() {
        hitbox.setPosition(position.x, position.y);
    }
}
