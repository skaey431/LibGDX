package LibGDX.TEST.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

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

    public void update(float backgroundWidth, float backgroundHeight) {
        this.backgroundWidth = backgroundWidth;
        this.backgroundHeight = backgroundHeight;
    }
    public void entity() {

    }

    public void render(SpriteBatch batch, Vector2 playerPos) {
        batch.end();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.setProjectionMatrix(camera.combined);

        float mapWidth = 150;   // 미니맵 화면 크기
        float mapHeight = 100;
        float mapX = camera.position.x + 230; // 미니맵 화면 좌표
        float mapY = camera.position.y + 130;

        // 1️⃣ 배경 고정
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.3f, 0.3f, 0.3f, 0.5f);
        shapeRenderer.rect(mapX, mapY, mapWidth, mapHeight);
        shapeRenderer.end();

        // 2️⃣ 전체 맵 비율 계산
        float scaledX = (playerPos.x / 800) * mapWidth;
        float scaledY = (playerPos.y / backgroundHeight) * mapHeight;

        float dotX, offsetX = 0;

        // 3️⃣ 가로 스크롤 처리
        if (scaledX < mapWidth / 2f) {
            // 좌측 끝: 플레이어 점 이동
            dotX = mapX + scaledX;
            offsetX = 0;
        } else if ( scaledX - mapWidth <(backgroundWidth/800 - 1) * mapWidth/ 2f - mapWidth / 2f   )  {
            dotX = mapX + mapWidth / 2f;

        } else {
            // 우측 끝: 플레이어 점 이동
            dotX = mapX + scaledX;
            offsetX = ((backgroundWidth/800 - 1) * mapWidth/ 2f);
        }

        float dotY = mapY + scaledY; // 세로는 단순 비율

        // 4️⃣ 플레이어 점 표시
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 1);
        shapeRenderer.circle(dotX - offsetX, dotY, 3);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
        batch.begin();
    }



    public void dispose() {
        shapeRenderer.dispose();
    }
}
