// Player.java
package LibGDX.TEST;

import LibGDX.TEST.abstractClass.PhysicsObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Player extends PhysicsObject {
    private Texture frame1, frame2;
    private boolean showFrame1 = true;
    private float animationTimer = 0f;
    private final float frameDuration = 0.25f;
    private boolean lookingLeft = true;

    public Player(float x, float y) {
        super(x, y);
        frame1 = new Texture(Gdx.files.internal("resources/doctor_character_walking_0001.png"));
        frame2 = new Texture(Gdx.files.internal("resources/doctor_character_walking_0002.png"));
        this.width = frame1.getWidth();
        this.height = frame1.getHeight();
        this.hitbox.setSize(width, height);
    }

    public void update(float delta, Array<Rectangle> walls) {
        handleInput(delta);
        super.update(delta, walls);

        if (velocity.x != 0) {
            animationTimer += delta;
            if (animationTimer >= frameDuration) {
                animationTimer -= frameDuration;
                showFrame1 = !showFrame1;
            }
        } else {
            animationTimer = 0;
            showFrame1 = true;
        }
    }

    private void handleInput(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveLeft();
            lookingLeft = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveRight();
            lookingLeft = false;
        } else {
            stopMoving();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            jump();
        }
    }

    public void render(SpriteBatch batch) {
        Texture frame = showFrame1 ? frame1 : frame2;
        int drawX = Math.round(position.x);
        int drawY = Math.round(position.y);

        if (lookingLeft) {
            batch.draw(frame, drawX, drawY);
        } else {
            batch.draw(frame, drawX + width, drawY, -width, height);
        }
    }

    public void dispose() {
        frame1.dispose();
        frame2.dispose();
    }
}
