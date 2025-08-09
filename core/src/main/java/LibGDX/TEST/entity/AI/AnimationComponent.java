package LibGDX.TEST.entity.AI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AnimationComponent {
    private Texture frame1;
    private Texture frame2;
    private boolean showFrame1 = true;
    private float animationTimer = 0f;
    private final float frameDuration = 0.25f;

    public AnimationComponent(String frame1Path, String frame2Path) {
        frame1 = new Texture(Gdx.files.internal(frame1Path));
        frame2 = new Texture(Gdx.files.internal(frame2Path));
    }

    public void update(float delta, boolean isMoving) {
        if (!isMoving) return;

        animationTimer += delta;
        if (animationTimer >= frameDuration) {
            animationTimer -= frameDuration;
            showFrame1 = !showFrame1;
        }
    }

    public void render(SpriteBatch batch, float x, float y, float width, float height, boolean movingLeft) {
        Texture frame = showFrame1 ? frame1 : frame2;

        if (movingLeft) {
            batch.draw(frame, x, y);
        } else {
            batch.draw(frame, x + width, y, -width, height);
        }
    }

    public void dispose() {
        frame1.dispose();
        frame2.dispose();
    }

    public float getFrameWidth() {
        return frame1.getWidth();
    }

    public float getFrameHeight() {
        return frame1.getHeight();
    }

}
