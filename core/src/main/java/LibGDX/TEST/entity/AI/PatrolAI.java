package LibGDX.TEST.entity.AI;

import LibGDX.TEST.entity.abstracClass.BaseCharacter;
import LibGDX.TEST.entity.abstracClass.BaseEntity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class PatrolAI extends BaseCharacter {
    private final PatrolAIController aiController;

    public PatrolAI(float x, float y) {
        super(x, y, 64, 64); // 너가 사용하는 텍스처 크기에 맞게 조절 필요

        MovementComponent movement = new MovementComponent(position, width, height);
        AnimationComponent animation = new AnimationComponent(
            "resources/chatgpt_character_walking_0001.png",
            "resources/chatgpt_character_walking_0002.png");

        aiController = new PatrolAIController(movement, animation);
    }
    @Override
    public void updateMap(Array<Rectangle> walls){
        aiController.updateMap(walls);
    }
    @Override
    public void update(float delta) {
        aiController.update(delta);
        this.position.set(aiController.movement.getPosition());  // 위치 동기화
        updateHitbox();
    }

    @Override
    public void render(SpriteBatch batch) {
        try {
            aiController.render(batch);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


    @Override
    public void dispose() {
        aiController.dispose();
    }

    @Override
    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    @Override
    public void setCurrentStage(int stage) {
        this.currentStage = stage;
    }

    @Override
    public int getCurrentStage() {
        return currentStage;
    }

    public boolean isStopped() {
        return true;
    }
}
