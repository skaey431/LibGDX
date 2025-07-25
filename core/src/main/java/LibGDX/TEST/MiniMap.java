package LibGDX.TEST;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MiniMap {
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private float backgroundWidth;
    private float backgroundHeight;

    public MiniMap(OrthographicCamera camera, float backgroundHeight, float backgroundWidth) {
        this.camera = camera;
        this.shapeRenderer = new ShapeRenderer();
        this.backgroundHeight = backgroundHeight;
        this.backgroundWidth = backgroundWidth;
    }

    public void update() {
        // 필요시 확장 가능
    }

    public void render(SpriteBatch batch, float playerX, float playerY) {
        batch.end();

        Gdx.gl.glEnable(GL20.GL_BLEND); // 💡 투명도 적용을 위해 블렌딩 활성화

        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float mapX = camera.position.x + 300;
        float mapY = camera.position.y + 180;
        float mapWidth = 150;
        float mapHeight = 100;

        // 🔴 더 투명하게 (알파 0.25)
        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0.5f);
        shapeRenderer.rect(mapX, mapY, mapWidth, mapHeight);

        float dotX = mapX + (playerX / backgroundWidth) * mapWidth;
        float dotY = mapY + (playerY / backgroundHeight) * mapHeight;

        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.circle(dotX, dotY, 3);

        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND); // 💡 사용 후 비활성화

        batch.begin();
    }


    public void dispose() {
        shapeRenderer.dispose();
    }
}
