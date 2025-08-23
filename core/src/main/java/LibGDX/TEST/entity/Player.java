package LibGDX.TEST.entity;

import LibGDX.TEST.entity.abstracClass.BaseCharacter;
import LibGDX.TEST.entity.abstracClass.BaseEntity;
import LibGDX.TEST.entity.AI.MovementComponent;
import LibGDX.TEST.entity.AI.AnimationComponent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class Player extends BaseCharacter {
    private MovementComponent movement;
    private AnimationComponent animation;

    private boolean lookingLeft = true;

    public Player(float x, float y) {
        super(x, y, 10,  10); // 텍스처 크기 맞게 조절 필요
        animation = new AnimationComponent(
            "resources/doctor_character_walking_0001.png",
            "resources/doctor_character_walking_0002.png");
        movement = new MovementComponent(position, width, height);
        this.width = animation.getFrameWidth();
        this.height = animation.getFrameHeight();
    }


    public void update(float delta, Array<Rectangle> walls) {
        handleInput();
        movement.update(delta, walls);
        animation.update(delta, movement.getVelocityX() != 0);
        System.out.println("movement: " + movement.getVelocityX());
        this.position.set(movement.getPosition());
        updateHitbox();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            movement.setVelocityX(-movement.getMoveSpeed());
            lookingLeft = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            movement.setVelocityX(movement.getMoveSpeed());
            lookingLeft = false;
        } else {
            movement.stopX();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            movement.jump();
        }
    }

    @Override
    public void updateMap(Array<Rectangle> walls) {}

    @Override
    public void update(float delta) {

    }

    public void render(SpriteBatch batch) {
        animation.render(batch, position.x, position.y, width, height, lookingLeft);
    }

    public void dispose() {
        animation.dispose();
    }

    public void stopMoving() {
        animation.setShowFrame1(true);
    }
    @Override
    public void setPosition(float x, float y) {
        position.set(x, y);
        movement.getPosition().set(position);
    }

    @Override
    public void setCurrentStage(int stage) {
        this.currentStage = stage;
    }

    @Override
    public int getCurrentStage() {
        return currentStage;
    }

    ;
}
