package LibGDX.TEST;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class StageMap {
    private final ShapeRenderer shapeRenderer;

    private boolean visible = false;

    // 전체 맵 크기
    private final float size = 60f;
    private float width = 500;
    private float height = 200;
    List<Stage> stages;

    public StageMap(List<Stage> stages) {
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

        float totalWidth = 0;
        float totalHeight = 0;
        for (Stage stage : stages){
            if (totalWidth<=stage.x+stage.sizeVector.x) {
                totalWidth = stage.x + stage.sizeVector.x;
            }
            if (totalHeight <= stage.y + stage.sizeVector.y){
                totalHeight = stage.y + stage.sizeVector.y;
            }
        }
        float baseX = (Gdx.graphics.getWidth() - totalWidth) / 2f;
        float baseY = (Gdx.graphics.getHeight() - totalHeight) / 2f;
        for (int i = 0; i < stages.size(); i++) {
            if (i == currentStageIndex) {
                shapeRenderer.setColor(new Color(1, 1, 0, 0.9f)); // 현재 스테이지: 노란색
            } else {
                shapeRenderer.setColor(new Color(1, 1, 1, 0.4f)); // 나머지 스테이지
            }
            Stage stage = stages.get(i);
            shapeRenderer.rect(
                baseX + stage.x,
                baseY + stage.y,
                stage.sizeVector.x,
                stage.sizeVector.y
            );
        }


        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
