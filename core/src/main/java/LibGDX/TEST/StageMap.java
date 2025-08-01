package LibGDX.TEST;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.List;

public class StageMap {
    private final ShapeRenderer shapeRenderer;
    private boolean visible = false;

    private float width = 500;
    private float height = 200;

    private float scale = 1.0f;
    private final float minScale = 0.3f;
    private final float maxScale = 3.0f;

    private final List<Stage> stages;

    public StageMap(List<Stage> stages) {
        this.shapeRenderer = new ShapeRenderer();
        this.stages = stages;

        // 스크롤 이벤트 감지를 위한 InputAdapter 등록
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (visible) {
                    scale -= amountY * 0.1f;
                    if (scale < minScale) scale = minScale;
                    if (scale > maxScale) scale = maxScale;
                }
                return false;
            }
        });
    }

    public void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            visible = !visible;
        }
    }

    public void render(int currentStageIndex) {
        update();
        if (!visible) return;

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0, 0, 0, 0.5f);
        shapeRenderer.rect((Gdx.graphics.getWidth() - width) / 2, (Gdx.graphics.getHeight() - height) / 2, width, height);

        float totalWidth = 0;
        float totalHeight = 0;
        for (Stage stage : stages) {
            totalWidth = Math.max(totalWidth, stage.x + stage.sizeVector.x);
            totalHeight = Math.max(totalHeight, stage.y + stage.sizeVector.y);
        }

        float baseX = (Gdx.graphics.getWidth() - totalWidth * scale) / 2f;
        float baseY = (Gdx.graphics.getHeight() - totalHeight * scale) / 2f;

        for (int i = 0; i < stages.size(); i++) {
            Stage stage = stages.get(i);
            if (i == currentStageIndex) {
                shapeRenderer.setColor(new Color(1, 1, 0, 0.9f));
            } else {
                shapeRenderer.setColor(new Color(1, 1, 1, 0.4f));
            }

            shapeRenderer.rect(
                baseX + stage.x * scale,
                baseY + stage.y * scale,
                stage.sizeVector.x * scale,
                stage.sizeVector.y * scale
            );
        }

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
