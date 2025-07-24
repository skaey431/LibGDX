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

        walls = new Array<>();
        walls.add(new Rectangle(0, 40, backgroundWidth, 0));  // 바닥

        player = new Player(100, 40);
        ai = new PatrolAI(400, 40);

        popup = new TestPopup(200, 150);  // 고정 위치, 크기 자유롭게 설정
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // AI가 멈춰있고 팝업이 안 켜져 있으면 F 눌렀을 때 팝업 켜기
        if (ai.isStopped() && !popup.isVisible() && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            popup.setVisible(true);
        }

        // 팝업이 켜져 있으면 플레이어 움직임 봉인
        if (!popup.isVisible()) {
            player.update(delta, walls);
        } else {
            // 팝업 켜져 있을 때는 플레이어 움직임 멈춤
            player.stopMoving();
            player.update(delta, walls);  // 위치 고정시키기 위해 update 호출 유지
        }

        ai.update(delta, walls, player.getPosition().x, player.getPosition().y);

        // 카메라가 플레이어를 따라가도록 이동
        camera.position.x = player.getPosition().x + worldWidth / 8;
        camera.position.y = player.getPosition().y + worldHeight / 4;

        // 카메라 경계 제한
        camera.position.x = Math.max(camera.position.x, worldWidth / 2);
        camera.position.x = Math.min(camera.position.x, backgroundWidth - worldWidth / 2);
        camera.position.y = Math.max(camera.position.y, worldHeight / 2);

        camera.update();

        // 화면 지우기
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // 배경 그리기
        batch.draw(backgroundTexture, 0, 0, backgroundWidth, backgroundHeight);

        // 벽 그리기
        for (Rectangle wall : walls) {
            batch.draw(brickTexture, wall.x, wall.y, wall.width, wall.height);
        }

        // 플레이어와 AI 그리기
        player.render(batch);
        ai.render(batch);

        batch.end();

        // 팝업 렌더링 (ShapeRenderer와 BitmapFont 내부 포함)
        popup.renderShape();

        batch.begin();
        popup.renderText(batch);
        batch.end();

        // 팝업 클릭 처리
        if (Gdx.input.justTouched()) {
            // 마우스 클릭 좌표를 화면 좌표에서 월드 좌표로 변환
            int screenX = Gdx.input.getX();
            int screenY = Gdx.input.getY();
            // 팝업 버튼 클릭 체크는 스크린 좌표 기준
            popup.handleClick(screenX, Gdx.graphics.getHeight() - screenY);
        }
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
