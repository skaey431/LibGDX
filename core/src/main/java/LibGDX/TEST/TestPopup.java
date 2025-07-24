package LibGDX.TEST;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.GL20;

public class TestPopup {

    private float x, y, width, height;
    private Rectangle buttonBounds;
    private boolean visible = false;

    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;

    public TestPopup(float width, float height) {
        this.width = width;
        this.height = height;

        // 팝업 위치를 화면 정중앙에 고정
        this.x = (Gdx.graphics.getWidth() - width) / 2f;
        this.y = (Gdx.graphics.getHeight() - height) / 2f;

        float btnWidth = 100;
        float btnHeight = 40;
        float btnX = x + width / 2 - btnWidth / 2;
        float btnY = y + 20;

        buttonBounds = new Rectangle(btnX, btnY, btnWidth, btnHeight);
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
    }

    public void renderShape() {
        if (!visible) return;

        Gdx.gl.glEnable(GL20.GL_BLEND);
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
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void renderText(SpriteBatch batch) {
        if (!visible) return;

        // "Test Popup" 텍스트 위치 (팝업 좌측 상단 약간 여백 둠)
        font.draw(batch, "Test Popup", x + 20, y + height - 30);

        // "Close" 버튼 텍스트 위치 버튼 중앙 정렬
        String closeText = "Close";
        float textWidth = font.getRegion().getRegionWidth() * closeText.length() * 0.6f; // 폰트 너비 대략 계산
        float textX = buttonBounds.x + (buttonBounds.width - textWidth) / 2f;
        float textY = buttonBounds.y + buttonBounds.height / 2 + font.getCapHeight() / 2;

        font.draw(batch, closeText, textX, textY);
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
