package LibGDX.TEST;

import LibGDX.TEST.abstractClass.PhysicsObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class PatrolAI extends PhysicsObject {
    private Texture frame1, frame2;
    private boolean showFrame1 = true;
    private float animationTimer = 0f;
    private final float frameDuration = 0.25f;
    private boolean movingLeft = true;
    private boolean stopped = false;

    public PatrolAI(float x, float y) {
        super(x, y);
        frame1 = new Texture(Gdx.files.internal("resources/chatgpt_character_walking_0001.png"));
        frame2 = new Texture(Gdx.files.internal("resources/chatgpt_character_walking_0002.png"));
        this.width = frame1.getWidth();
        this.height = frame1.getHeight();
        this.hitbox.setSize(width, height);
        velocity.x = -moveSpeed;
    }

    public void update(float delta, Array<Rectangle> walls, float playerX, float playerY) {
        float dx = position.x - playerX;
        float dy = position.y - playerY;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist < 100) {
            stopMoving();
            stopped = true;
        } else {
            velocity.x = movingLeft ? -moveSpeed : moveSpeed;
            stopped = false;
        }

        float oldX = position.x;
        float oldY = position.y;

        super.update(delta, walls);

        boolean collided = false;
        for (Rectangle wall : walls) {
            if (hitbox.overlaps(wall)) {
                collided = true;
                break;
            }
        }

        if (collided) {
            position.x = oldX;
            position.y = oldY;
            hitbox.setPosition(position.x, position.y);

            movingLeft = !movingLeft;
            velocity.x = movingLeft ? -moveSpeed : moveSpeed;
        }

        // 애니메이션 타이머는 stopped 상태일 때 업데이트하지 않음
        if (!stopped) {
            animationTimer += delta;
            if (animationTimer >= frameDuration) {
                animationTimer -= frameDuration;
                showFrame1 = !showFrame1;
            }
        }
    }

    public void render(SpriteBatch batch) {
        Texture frame = showFrame1 ? frame1 : frame2;
        int drawX = Math.round(position.x);
        int drawY = Math.round(position.y);

        if (movingLeft) {
            batch.draw(frame, drawX, drawY);
        } else {
            batch.draw(frame, drawX + width, drawY, -width, height);
        }
    }

    public boolean isStopped() {
        return stopped;
    }

    public void dispose() {
        frame1.dispose();
        frame2.dispose();
    }
}
