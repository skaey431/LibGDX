package LibGDX.TEST;

import LibGDX.TEST.controller.CameraController;
import LibGDX.TEST.controller.EntityManager;
import LibGDX.TEST.controller.StageManager;
import LibGDX.TEST.controller.UIManager;
import LibGDX.TEST.entity.AI.PatrolAI;
import LibGDX.TEST.entity.BaseEntity;
import LibGDX.TEST.entity.Player;
import LibGDX.TEST.map.MiniMap;
import LibGDX.TEST.map.Stage;
import LibGDX.TEST.map.StageMap;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {

    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 480;
    private static final float PLAYER_START_Y = 40;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private CameraController cameraController;

    private Player player;
    private Texture wallTexture;

    private StageManager stageManager;
    private EntityManager entityManager;

    private MiniMap miniMap;
    private StageMap stageMap;
    private UIManager uiManager;

    @Override
    public void create() {
        batch = new SpriteBatch();

        // 카메라 설정
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        cameraController = new CameraController(camera, WORLD_WIDTH, WORLD_HEIGHT);

        uiCamera = new OrthographicCamera();
        uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        wallTexture = new Texture("resources/bricks.png");

        // 스테이지 생성
        List<Stage> stages = new ArrayList<>();
        stages.add(new Stage("room1", "resources/room1.png",
            new Array<Rectangle>() {{ add(new Rectangle(0, 40, 800, 20)); }},
            0, 0, 50, true));
        stages.add(new Stage("hallway1-2", "resources/hallway.png",
            new Array<Rectangle>() {{ add(new Rectangle(0, 40, 1000, 20)); }},
            50, 10, 30, true));
        stages.add(new Stage("room2", "resources/room2.png",
            new Array<Rectangle>() {{ add(new Rectangle(0, 40, 800, 20)); }},
            50 + (float)4096 / 16, 0, 50, true));

        stageManager = new StageManager(stages);

        // 플레이어 생성
        player = new Player(100, PLAYER_START_Y);

        // 엔티티 생성
        entityManager = new EntityManager();
        entityManager.addEntity(new PatrolAI(50, 50));

        // 미니맵, 스테이지맵, UI
        miniMap = new MiniMap(camera, stageManager.getCurrentStage().getHeight(), stageManager.getCurrentStage().getWidth());
        stageMap = new StageMap(stages);
        uiManager = new UIManager(uiCamera, stageMap, miniMap);
    }

    @Override
    public void render() {
        try {
            float delta = Gdx.graphics.getDeltaTime();

            // 스테이지맵 토글
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) uiManager.toggleStageMap();

            // 플레이어 물리 업데이트
            player.update(delta, stageManager.getCurrentStage().getWalls());

            // 엔티티 업데이트
            entityManager.updateEntities(delta, stageManager.getCurrentStage());

            // 스테이지 이동 체크
            checkStageTransition();

            // 카메라 업데이트
            cameraController.update(player.getPosition(), stageManager.getCurrentStage());

            // 화면 클리어
            Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // 월드 렌더
            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            // 배경
            batch.draw(stageManager.getCurrentStage().getBackgroundTexture(), 0, 0,
                stageManager.getCurrentStage().getWidth(),
                stageManager.getCurrentStage().getHeight());

            // 벽
            for (Rectangle wall : stageManager.getCurrentStage().getWalls()) {
                batch.draw(wallTexture, wall.x, wall.y, wall.width, wall.height);
            }

            // 플레이어
            player.render(batch);

            // 엔티티 렌더
            entityManager.renderEntities(batch, stageManager.getCurrentStage());

            batch.end();

            // UI 렌더
            uiManager.render(batch, uiCamera, player,
                stageManager.getCurrentStage().getEntities(),
                stageManager.getCurrentIndex());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkStageTransition() {
        if (player.getPosition().x > stageManager.getCurrentStage().getWidth()) {
            stageManager.moveToNextStage(true);
        } else if (player.getPosition().x < 0) {
            stageManager.moveToNextStage(false);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        wallTexture.dispose();
        player.dispose();
        for (BaseEntity entity : stageManager.getCurrentStage().getEntities()) entity.dispose();
        entityManager.disposeAll();
    }
}
