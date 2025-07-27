package LibGDX.TEST;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class StageMap {
    private final int stageCount;
    private final ShapeRenderer shapeRenderer;

    private boolean visible = false;

    // 전체 맵 크기
    private final float size = 60f;
    private final float spacing = 20f;
    private float width = 500;
    private float height = 200;
    List<Stage> stages;

    public StageMap(List<Stage> stages) {
        this.stageCount = stages.size();
        this.shapeRenderer = new ShapeRenderer();
        this.stages = stages;
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


        // 팝업 배경
        shapeRenderer.setColor(0, 0, 0, 0.5f);
        shapeRenderer.rect((Gdx.graphics.getWidth()-width)/2, (Gdx.graphics.getHeight()-height)/2,  width, height);

        float totalWidth = stageCount * size + (stageCount - 1) * spacing;
        float baseX = (Gdx.graphics.getWidth() - totalWidth) / 2f;
        float baseY = (Gdx.graphics.getHeight() - size) / 2f;

        for (int i = 0; i < stageCount; i++) {
            float x = baseX + i * (size + spacing);
            float y = baseY;

            if (i == currentStageIndex) {
                shapeRenderer.setColor(new Color(1, 1, 0, 0.9f)); // 현재 스테이지: 노란색
            } else {
                shapeRenderer.setColor(new Color(1, 1, 1, 0.4f)); // 나머지 스테이지
            }

            shapeRenderer.rect(x, y, size, size);
        }
        for (Stage stage : stages){
            shapeRenderer.rect(0, 0, stage.width/16, size);
        }

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
