// abstract/Popup.java
package LibGDX.TEST.abstractClass;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public abstract class Popup {
    protected float x, y, width, height;
    protected boolean visible = false;
    protected ShapeRenderer shapeRenderer;
    protected Rectangle buttonBounds;

    public Popup(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.shapeRenderer = new ShapeRenderer();

        // Close 버튼 영역 예시
        this.buttonBounds = new Rectangle(x + width - 90, y + 20, 70, 30);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void handleClick(float clickX, float clickY) {
        if (!visible) return;

        if (buttonBounds.contains(clickX, clickY)) {
            onClose();
        }
    }

    protected abstract void onClose();

    public void render(SpriteBatch batch) {
        if (!visible) return;

        // SpriteBatch 먼저 종료
        batch.end();

        // ShapeRenderer와 SpriteBatch의 projectionMatrix 일치시키기
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // 반투명 배경과 팝업창 그리기
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0, 0, 0, 0.6f); // 반투명 배경
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        shapeRenderer.setColor(Color.DARK_GRAY); // 팝업 배경
        shapeRenderer.rect(x, y, width, height);

        shapeRenderer.setColor(Color.LIGHT_GRAY); // 버튼
        shapeRenderer.rect(buttonBounds.x, buttonBounds.y, buttonBounds.width, buttonBounds.height);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        // 다시 SpriteBatch 시작
        batch.begin();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
