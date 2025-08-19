package LibGDX.TEST.entity.AI;

import LibGDX.TEST.entity.BaseEntity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class PatrolAI extends BaseEntity {
    private PatrolAIController aiController;

    public PatrolAI(float x, float y, Array<Rectangle> walls, Vector2 playerPos) {
        super(x, y, 32, 48); // 너가 사용하는 텍스처 크기에 맞게 조절 필요

        MovementComponent movement = new MovementComponent(position, width, height);
        AnimationComponent animation = new AnimationComponent(
            "resources/chatgpt_character_walking_0001.png",
            "resources/chatgpt_character_walking_0002.png");

        aiController = new PatrolAIController(movement, animation, walls, playerPos);
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
    public void check() {
        System.out.println("checked");
    }

    public boolean isStopped() {
        return true;
    }
}
