package LibGDX.TEST.map;

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

    private float width = Gdx.graphics.getWidth() - 30;
    private float height = Gdx.graphics.getHeight() - 30;

    private float scale = 1.0f;
    private final float minScale = 0.3f;
    private final float maxScale = 3.0f;

    private float offsetX = 0;
    private float offsetY = 0;

    private float dragStartX = 0;
    private float dragStartY = 0;
    private boolean dragging = false;

    private final List<Stage> stages;

    public StageMap(List<Stage> stages) {
        this.shapeRenderer = new ShapeRenderer();
        this.stages = stages;

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

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (!visible) return false;
                dragging = true;
                dragStartX = screenX;
                dragStartY = screenY;
                return true;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (!visible || !dragging) return false;
                float dx = screenX - dragStartX;
                float dy = dragStartY - screenY; // 상하 반전
                offsetX += dx;
                offsetY += dy;
                dragStartX = screenX;
                dragStartY = screenY;
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                dragging = false;
                return true;
            }
        });
    }

    public void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            visible = !visible;

            // 맵 켤 때 초기화
            if (visible) {
                scale = 1.0f;
                offsetX = 0f;
                offsetY = 0f;
            }
        }

        if (!visible) return;

        // 단축키 확대/축소
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS) || Gdx.input.isKeyPressed(Input.Keys.EQUALS)) {
            scale += 0.02f;
            if (scale > maxScale) scale = maxScale;
        } else if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            scale -= 0.02f;
            if (scale < minScale) scale = minScale;
        }

        // 위치 초기화
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            offsetX = 0f;
            offsetY = 0f;
            scale = 1.0f;
        }
    }

    public void render(int currentStageIndex) {
        update();
        if (!visible) return;

        int screenW = Gdx.graphics.getWidth();
        int screenH = Gdx.graphics.getHeight();

        // 맵 배경 위치 계산
        float bgX = (screenW - width) / 2f;
        float bgY = (screenH - height) / 2f;

        // Scissor Box 설정
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
        Gdx.gl.glScissor((int) bgX, (int) bgY, (int) width, (int) height);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // 반투명 배경
        shapeRenderer.setColor(0, 0, 0, 0.5f);
        shapeRenderer.rect(bgX, bgY, width, height);

        // 전체 스테이지 너비/높이 계산
        float totalWidth = 0;
        float totalHeight = 0;
        for (Stage stage : stages) {
            totalWidth = Math.max(totalWidth, stage.x + stage.sizeVector.x);
            totalHeight = Math.max(totalHeight, stage.y + stage.sizeVector.y);
        }

        float baseX = (screenW - totalWidth * scale) / 2f + offsetX;
        float baseY = (screenH - totalHeight * scale) / 2f + offsetY;

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
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST); // 클리핑 해제
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
