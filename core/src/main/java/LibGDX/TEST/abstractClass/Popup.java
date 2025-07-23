package LibGDX.TEST.abstractClass;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Popup {
    protected float x, y, width, height;
    protected boolean visible = false;

    public Popup(float x, float y, float width, float height) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
    }

    // 팝업 표시여부 설정
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    // 버튼 같은 UI 요소 위치 및 크기 지정용 메서드
    public abstract Rectangle getButtonBounds();

    // 버튼 클릭 처리 (좌표 입력)
    public abstract boolean handleClick(float clickX, float clickY);

    // 팝업 그리기
    public abstract void render(SpriteBatch batch);

    // 팝업 업데이트 (필요하면 오버라이드)
    public void update(float delta) {}
}
