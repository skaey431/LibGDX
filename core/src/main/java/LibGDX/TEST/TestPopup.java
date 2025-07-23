package LibGDX.TEST;

import LibGDX.TEST.abstractClass.Popup;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class TestPopup extends Popup {
    private Rectangle buttonBounds;
    private ShapeRenderer shapeRenderer;

    public TestPopup(float x, float y, float width, float height) {
        super(x, y, width, height);
        shapeRenderer = new ShapeRenderer();

        // 버튼 위치 (팝업 하단 중앙에 100x40 크기)
        float btnWidth = 100;
        float btnHeight = 40;
        buttonBounds = new Rectangle(
            x + (width - btnWidth) / 2f,
            y + 20,
            btnWidth,
            btnHeight
        );
    }

    @Override
    public Rectangle getButtonBounds() {
        return buttonBounds;
    }

    @Override
    public boolean handleClick(float clickX, float clickY) {
        if (!visible) return false;
        if (buttonBounds.contains(clickX, clickY)) {
            // 버튼 클릭 시 팝업 닫기
            setVisible(false);
            return true;
        }
        // 팝업 바깥 클릭 시에도 팝업 닫기
        Rectangle popupRect = new Rectangle(x, y, width, height);
        if (!popupRect.contains(clickX, clickY)) {
            setVisible(false);
            return true;
        }
        return false;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (!visible) return;

        batch.end();

        // 반투명 배경
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.6f);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // 팝업 배경
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(x, y, width, height);
        // 버튼
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.rect(buttonBounds.x, buttonBounds.y, buttonBounds.width, buttonBounds.height);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        batch.begin();

        // 텍스트 출력 (간단히, 폰트는 Main에서 넘겨받거나 여기서 생성 가능)
        // 예: "Close" 버튼 텍스트
        // 배치용 폰트가 필요하면 Main에서 폰트 넘겨주는게 좋음
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
