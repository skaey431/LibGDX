package LibGDX.TEST;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

// 스테이지 클래스 (배경, 크기, 벽, AI 위치 등)
//
public class Stage {
    Texture backgroundTexture;
    float width, height;
    Array<Rectangle> walls;
    float aiStartX, aiStartY;

    public Stage(String backgroundPath, Array<Rectangle> walls, float aiStartX, float aiStartY) {
        this.backgroundTexture = new Texture(Gdx.files.internal(backgroundPath));
        this.width = backgroundTexture.getWidth();
        this.height = backgroundTexture.getHeight();
        this.walls = walls;
        this.aiStartX = aiStartX;
        this.aiStartY = aiStartY;
        walls.add(new Rectangle(0,40,backgroundTexture.getWidth(),0));
    }

    public void dispose() {
        backgroundTexture.dispose();
    }
}
