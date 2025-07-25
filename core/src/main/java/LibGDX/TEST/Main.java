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
    private MapOverlay mapOverlay;


    float worldWidth = 800;
    float worldHeight = 480;

    TestPopup popup;

    @Override
    public void create() {
        batch = new SpriteBatch();
        brickTexture = new Texture(Gdx.files.internal("resources/bricks.png"));

        backgroundTexture = new Texture(Gdx.files.internal("resources/복도1.png"));
        backgroundWidth = backgroundTexture.getWidth();
        backgroundHeight = backgroundTexture.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, worldWidth, worldHeight);
        mapOverlay = new MapOverlay(backgroundTexture, backgroundWidth, backgroundHeight, camera);

        walls = new Array<>();
        walls.add(new Rectangle(0, 40, backgroundWidth, 0));  // 바닥

        player = new Player(100, 40);
        ai = new PatrolAI(400, 40);

        popup = new TestPopup(200, 150);  // 고정 위치, 크기 자유롭게 설정
    }

    @Override
    public void render() {

        float delta = Gdx.graphics.getDeltaTime();

        // 팝업 상태에 따른 입력 처리
        if (ai.isStopped() && !popup.isVisible() && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            popup.setVisible(true);
        }

        if (!popup.isVisible()) {
            player.update(delta, walls);
        } else {
            player.stopMoving();
            player.update(delta, walls); // 위치 고정
        }

        ai.update(delta, walls, player.getPosition().x, player.getPosition().y);

        // 카메라 이동 및 제한
        camera.position.x = player.getPosition().x + worldWidth / 8;
        camera.position.y = player.getPosition().y + worldHeight / 4;
        camera.position.x = Math.max(camera.position.x, worldWidth / 2);
        camera.position.x = Math.min(camera.position.x, backgroundWidth - worldWidth / 2);
        camera.position.y = Math.max(camera.position.y, worldHeight / 2);
        camera.update();

        // 화면 지우기
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 카메라 기준 그리는 부분
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(backgroundTexture, 0, 0, backgroundWidth, backgroundHeight);

        for (Rectangle wall : walls) {
            batch.draw(brickTexture, wall.x, wall.y, wall.width, wall.height);
        }

        player.render(batch);
        ai.render(batch);

        // 팝업 텍스트도 배치 안 깨지도록 같이 batch 내에서 렌더링
        popup.renderText(batch);


        batch.end();

        try {
            mapOverlay.render(batch, player.getPosition().x, player.getPosition().y);
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 실제 예외 출력
        }
        // 팝업 박스는 shapeRenderer로 따로 렌더링 (카메라 영향 받지 않음)
        popup.renderShape();

        // 팝업 클릭 처리
        if (Gdx.input.justTouched()) {
            int screenX = Gdx.input.getX();
            int screenY = Gdx.input.getY();
            popup.handleClick(screenX, Gdx.graphics.getHeight() - screenY);
        }

        mapOverlay.update();

    }


    @Override
    public void dispose() {
        batch.dispose();
        brickTexture.dispose();
        backgroundTexture.dispose();
        player.dispose();
        ai.dispose();
        popup.dispose();
    }
}
