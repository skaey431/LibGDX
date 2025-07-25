package LibGDX.TEST;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MapOverlay {
    private boolean visible = false;
    private final Texture background;
    private final ShapeRenderer shapeRenderer;

    private final int mapWidth = 300;
    private final int mapHeight = 200;
    private final float scale;

    private final OrthographicCamera camera;

    public MapOverlay(Texture background, float fullWidth, float fullHeight, OrthographicCamera camera) {
        this.background = background;
        this.scale = mapWidth / fullWidth;
        this.shapeRenderer = new ShapeRenderer();
        this.camera = camera;
    }

    public void update() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            visible = !visible;
        }
    }

    public void render(SpriteBatch batch, float playerX, float playerY) {
        if (!visible) return;

        float camX = camera.position.x - camera.viewportWidth / 2;
        float camY = camera.position.y - camera.viewportHeight / 2;

        float x = camX + camera.viewportWidth - mapWidth - 20; // 오른쪽 위 여백 20
        float y = camY + camera.viewportHeight - mapHeight - 20;

        // 배경 그림자 맵
        batch.begin();
        batch.setColor(1, 1, 1, 0.8f);
        batch.draw(background, x, y, mapWidth, mapHeight);
        batch.setColor(1, 1, 1, 1);
        batch.end();

        // 플레이어 위치 점
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 1);

        float px = x + playerX * scale;
        float py = y + playerY * scale;
        shapeRenderer.circle(px, py, 5);
        shapeRenderer.end();
    }

    public boolean isVisible() {
        return visible;
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}
