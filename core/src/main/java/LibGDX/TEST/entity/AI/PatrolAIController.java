package LibGDX.TEST.entity.AI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class PatrolAIController implements AIController {
    MovementComponent movement;
    private AnimationComponent animation;

    private boolean movingLeft = true;
    private boolean stopped = false;

    private Array<Rectangle> walls = new Array<Rectangle>();
    private Vector2 playerPosition = new Vector2(0,0);

    public PatrolAIController(MovementComponent movement, AnimationComponent animation) {
        this.movement = movement;
        this.animation = animation;
        movement.setVelocityX(-100f);
    }

    public void updateMap(Array<Rectangle> walls){
        for (Rectangle wall : walls) {
            this.walls.add(wall);
        }
    }

    @Override
    public void update(float delta) {
        Vector2 oldPos = movement.getPosition().cpy();
        movement.update(delta, walls);

        // 충돌 판단 (좌우 충돌 시 방향 전환)
        boolean collided = false;
        for (Rectangle wall : walls) {
            if (movement.getPosition().dst(oldPos) > 0) {
                Rectangle hitbox = new Rectangle(movement.getPosition().x, movement.getPosition().y,
                    movement.getWidth(), movement.getHeight());
                if (hitbox.overlaps(wall)) {
                    collided = true;
                    break;
                }
            }
        }

        if (collided) {
            // 위치 복구 및 방향 전환
            movement.getPosition().set(oldPos);
            movement.setVelocityX(movingLeft ? movement.getMoveSpeed() : -movement.getMoveSpeed());
            movingLeft = !movingLeft;
        }

        animation.update(delta, !stopped);
    }

    public void render(SpriteBatch batch) {
        Vector2 pos = movement.getPosition();
        animation.render(batch, pos.x, pos.y, movement.getWidth(), movement.getHeight(), movingLeft);
    }

    public void dispose() {
        animation.dispose();
    }
}
