package LibGDX.TEST;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class PatrolAI {
    private Texture frame1;
    private Texture frame2;
    private float x, y;
    private float direction = 1;
    private float speed = 50f;
    private float patrolRange = 100f;
    private float startX;
    private Rectangle bounds;

    private float animationTimer = 0f;
    private final float frameDuration = 0.25f;
    private boolean showFrame1 = true;

    // 바닥 Y 좌표
    private float groundY;

    public PatrolAI(String frame1Path, String frame2Path, float x, float y) {
        this.frame1 = new Texture(frame1Path);
        this.frame2 = new Texture(frame2Path);
        this.x = x; // 사용자 지정 x 좌표
        this.y = y; // 사용자 지정 y 좌표
        this.startX = x;
        this.groundY = y;
        this.bounds = new Rectangle(x, y, frame1.getWidth(), frame1.getHeight());
    }

    public void update(float delta, java.util.List<Rectangle> walls) {
        // 이동
        x += direction * speed * delta;

        // 좌우 배회
        if (x > startX + patrolRange) {
            direction = -1;
        } else if (x < startX - patrolRange) {
            direction = 1;
        }

        // 벽과 충돌 감지
        for (Rectangle wall : walls) {
            if (bounds.overlaps(wall)) {
                // 벽에 부딪히면 반대 방향으로 이동
                direction = -direction;
                // 벽과 충돌하면 AI의 위치를 벽과 충돌하지 않게 수정
                if (direction < 0) {
                    x = wall.x - bounds.width;// 벽 끝으로 밀기
                } else {
                    x = wall.x + wall.width;// 벽 끝으로 밀기
                }
                break;
            }
        }

        // 애니메이션 타이머
        animationTimer += delta;
        if (animationTimer >= frameDuration) {
            animationTimer -= frameDuration;
            showFrame1 = !showFrame1;
        }

        bounds.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        Texture currentFrame = showFrame1 ? frame1 : frame2;

        // 방향에 따라 좌우 반전
        if (direction < 0) {
            batch.draw(currentFrame, x, y);
        } else {
            batch.draw(currentFrame, x + currentFrame.getWidth(), y, -currentFrame.getWidth(), currentFrame.getHeight());
        }
    }

    public void dispose() {
        frame1.dispose();
        frame2.dispose();
    }

    public Rectangle getBounds() {
        return bounds;
    }
}
