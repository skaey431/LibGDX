package LibGDX.TEST.entity.AI;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class MovementComponent {
    private Vector2 position;
    private Vector2 velocity = new Vector2();
    private float width, height;

    private float moveSpeed = 100f;
    private float gravity = -600f;
    private boolean onGround = false;

    public MovementComponent(Vector2 startPosition, float width, float height) {
        this.position = startPosition.cpy();
        this.width = width;
        this.height = height;
    }

    public void update(float delta, Array<Rectangle> walls) {
        velocity.y += gravity * delta;
        position.y += velocity.y * delta;

        onGround = false;
        Rectangle hitbox = new Rectangle(position.x, position.y, width, height);

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
                hitbox.setPosition(position.x, position.y);
            }
        }

        position.x += velocity.x * delta;
        hitbox.setPosition(position.x, position.y);

        for (Rectangle wall : walls) {
            if (hitbox.overlaps(wall)) {
                position.x -= velocity.x * delta;
                break;
            }
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setVelocityX(float vx) {
        velocity.x = vx;
    }

    public void stopX() {
        velocity.x = 0;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public boolean isOnGround() {
        return onGround;
    }

    // 새로 추가한 getter
    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    public void jump() {
        if (onGround) {
            velocity.y = 400f; // 기존 jumpPower 값
            onGround = false;
        }
    }

    public float getVelocityX() {
        return velocity.x;
    }

}
