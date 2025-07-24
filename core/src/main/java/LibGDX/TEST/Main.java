package LibGDX.TEST;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Main extends ApplicationAdapter {
    SpriteBatch batch;
    OrthographicCamera camera;
    Player player;
    PatrolAI ai;
    Array<Rectangle> walls;
    Texture brickTexture;

    Texture backgroundTexture;
    float backgroundWidth, backgroundHeight;

    float worldWidth = 800;
    float worldHeight = 480;

    @Override
    public void create() {
        batch = new SpriteBatch();
        brickTexture = new Texture(Gdx.files.internal("resources/bricks.png"));

        backgroundTexture = new Texture(Gdx.files.internal("resources/img_19975_1.jpg"));
        backgroundWidth = backgroundTexture.getWidth();
        backgroundHeight = backgroundTexture.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, worldWidth, worldHeight);

        walls = new Array<>();
        walls.add(new Rectangle(0, 0, worldWidth, 40));      // 바닥
        walls.add(new Rectangle(0, 0, 20, worldHeight));     // 왼쪽 벽
        walls.add(new Rectangle(worldWidth - 20, 0, 20, worldHeight)); // 오른쪽 벽
        walls.add(new Rectangle(300, 40, 200, 40));          // 중간 벽

        player = new Player(100, 80);
        ai = new PatrolAI(400, 80);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // 카메라 업데이트
        camera.update();

        // 화면 지우기
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // 배경 그리기 (플레이어 위치를 기준으로 배경 이동)
        float offsetX = camera.position.x - worldWidth / 2;
        batch.draw(backgroundTexture, offsetX % backgroundWidth, 0, backgroundWidth, backgroundHeight);
        batch.draw(backgroundTexture, offsetX % backgroundWidth - backgroundWidth, 0, backgroundWidth, backgroundHeight);

        // 벽 그리기
        for (Rectangle wall : walls) {
            batch.draw(brickTexture, wall.x, wall.y, wall.width, wall.height);
        }

        // 플레이어와 AI 그리기
        player.render(batch);
        ai.render(batch);

        batch.end();

        // 플레이어와 AI 업데이트
        player.update(delta, walls);
        ai.update(delta, walls, player.getPosition().x, player.getPosition().y);

        // 카메라가 플레이어를 따라가도록 이동
        camera.position.x = player.getPosition().x + worldWidth / 4;
        camera.position.y = player.getPosition().y + worldHeight / 4;

        // 카메라 경계 제한
        camera.position.x = Math.max(camera.position.x, worldWidth / 2); // 왼쪽 경계
        camera.position.x = Math.min(camera.position.x, backgroundWidth - worldWidth / 2); // 오른쪽 경계
    }

    @Override
    public void dispose() {
        batch.dispose();
        brickTexture.dispose();
        backgroundTexture.dispose();
        player.dispose();
        ai.dispose();
    }
}
