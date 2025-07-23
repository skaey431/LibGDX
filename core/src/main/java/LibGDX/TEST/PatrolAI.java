package LibGDX.TEST;

import LibGDX.TEST.abstractClass.PhysicsObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class PatrolAI extends PhysicsObject {
    private Texture frame1, frame2;
    private boolean showFrame1 = true;
    private float animationTimer = 0f;
    private final float frameDuration = 0.25f;
    private boolean movingLeft = true;
    private final float gravity = -1000f;
    private float detectionRange = 100f;
    private boolean isNearPlayer = false;

    private BitmapFont font;

    public PatrolAI(float x, float y) {
        super(x, y);
        frame1 = new Texture(Gdx.files.internal("resources/chatgpt_character_walking_0001.png"));
        frame2 = new Texture(Gdx.files.internal("resources/chatgpt_character_walking_0002.png"));
        this.width = frame1.getWidth();
        this.height = frame1.getHeight();
        this.hitbox.setSize(width, height);
        velocity.x = -moveSpeed;

        font = new BitmapFont(); // 기본 폰트
    }

    public void update(float delta, Array<Rectangle> walls, float playerX, float playerY) {
        // 플레이어와 거리 계산
        float dx = playerX - position.x;
        float dy = playerY - position.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        isNearPlayer = distance < detectionRange;

        // 플레이어 가까우면 멈춤, 아니면 이동
        velocity.x = isNearPlayer ? 0 : (movingLeft ? -moveSpeed : moveSpeed);

        // 수평 이동 시도
        position.x += velocity.x * delta;
        hitbox.setPosition(position.x, position.y);

        // 수평 충돌 검사 및 처리
        boolean collided = false;
        for (Rectangle wall : walls) {
            if (hitbox.overlaps(wall)) {
                collided = true;
                break;
            }
        }

        if (collided) {
            position.x -= velocity.x * delta; // 되돌리기
            hitbox.setPosition(position.x, position.y);
            movingLeft = !movingLeft;
            velocity.x = movingLeft ? -moveSpeed : moveSpeed;
        }

        // 중력 및 수직 이동
        velocity.y += gravity * delta;
        position.y += velocity.y * delta;
        hitbox.setPosition(position.x, position.y);

        // 수직 충돌 검사 및 처리
        for (Rectangle wall : walls) {
            if (hitbox.overlaps(wall)) {
                if (velocity.y < 0) {
                    position.y = wall.y + wall.height;
                    velocity.y = 0;
                } else if (velocity.y > 0) {
                    position.y = wall.y - height;
                    velocity.y = 0;
                }
                hitbox.setPosition(position.x, position.y);
            }
        }

        // 애니메이션 타이머 - 근처면 애니메이션 멈춤
        if (!isNearPlayer) {
            animationTimer += delta;
            if (animationTimer >= frameDuration) {
                animationTimer -= frameDuration;
                showFrame1 = !showFrame1;
            }
        } else {
            animationTimer = 0f;
            showFrame1 = true;
        }
    }

    public void render(SpriteBatch batch) {
        Texture frame = showFrame1 ? frame1 : frame2;
        if (movingLeft) {
            batch.draw(frame, position.x, position.y);
        } else {
            batch.draw(frame, position.x + width, position.y, -width, height);
        }

        // 플레이어 근처라서 멈춰있으면 'F' 문자 출력
        if (isNearPlayer) {
            font.draw(batch, "F", position.x + width / 2f, position.y + height + 20);
        }
    }

    public boolean isStopped() {
        return velocity.x == 0 && velocity.y == 0;
    }


    public void dispose() {
        frame1.dispose();
        frame2.dispose();
        font.dispose();
    }
}
