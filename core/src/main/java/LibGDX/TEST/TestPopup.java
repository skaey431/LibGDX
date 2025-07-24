package LibGDX.TEST;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class TestPopup {

    private float x, y, width, height;
    private Rectangle buttonBounds;
    private boolean visible = false;

    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;

    public TestPopup(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        float btnWidth = 100;
        float btnHeight = 40;
        float btnX = x + width / 2 - btnWidth / 2;
        float btnY = y + 20;

        buttonBounds = new Rectangle(btnX, btnY, btnWidth, btnHeight);
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont(); // 기본 폰트 사용
        font.setColor(Color.WHITE);
    }

    public void renderShape() {
        if (!visible) return;

        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // 배경 반투명 블랙
        shapeRenderer.setColor(0, 0, 0, 0.5f);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // 팝업 배경
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, width, height);

        // 버튼
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.rect(buttonBounds.x, buttonBounds.y, buttonBounds.width, buttonBounds.height);

        shapeRenderer.end();
        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);
    }

    public void renderText(SpriteBatch batch) {
        if (!visible) return;

        font.draw(batch, "Test Popup", x + 20, y + height - 20);
        font.draw(batch, "Close", buttonBounds.x + 20, buttonBounds.y + 25);
    }

    public void handleClick(float clickX, float clickY) {
        if (buttonBounds.contains(clickX, clickY)) {
            setVisible(false);
        }
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void dispose() {
        shapeRenderer.dispose();
        font.dispose();
    }
}
