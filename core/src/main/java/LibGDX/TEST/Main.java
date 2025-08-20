package LibGDX.TEST;

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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends ApplicationAdapter {
    SpriteBatch batch;
    OrthographicCamera camera;
    Player player;

    Array<Rectangle> walls;
    Texture brickTexture;

    float worldWidth = 800;
    float worldHeight = 480;

    TestPopup popup;

    List<Stage> stages;
    List<BaseEntity> entities;
    int currentStageIndex;
    Stage currentStage;

    MiniMap miniMap;       // 미니맵 객체
    StageMap stageMap;     // 스테이지맵 객체

    boolean isStageMapOpen = false;  // 스테이지맵 열림 상태

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, worldWidth, worldHeight);

        stages = new ArrayList<>();
        entities = new ArrayList<>();

        //임시 db 넣기
        stages.add(new Stage(
            "room1",
            "resources/room1.png",
            new Array<Rectangle>() {{
                add(new Rectangle(0, 40, 800, 0));
            }},
            0,
            0,
            50,
            true
        ));

        stages.add(new Stage(
            "hallway1-2",
            "resources/hallway.png",
            new Array<Rectangle>() {{
                add(new Rectangle(0, 40, 1000, 0));
            }},
            50,
            10,
            30,
            true
        ));

        stages.add(new Stage(
            "room2",
            "resources/room2.png",
            new Array<Rectangle>() {{
                add(new Rectangle(0, 40, 800, 0));
            }},
            50+4096/16,
            0,
            50,
            true
        ));
        stages.add(new Stage(
            "hallway1-2",
            "resources/hallway.png",
            new Array<Rectangle>() {{
                add(new Rectangle(0, 40, 0, 0));
            }},
            10,
            50,
            30,
            false
        ));
        entities.add(
            new PatrolAI(
                50,50
            )
        );


        //임시 데이터 끗
        currentStageIndex = 0;
        currentStage = stages.get(currentStageIndex);

        player = new Player(100, 40);

        walls = new Array<>();
        walls.addAll(currentStage.getWalls());

        popup = new TestPopup(200, 150);

        miniMap = new MiniMap(camera, currentStage.getBackgroundTexture().getHeight(), currentStage.getBackgroundTexture().getWidth());  // 미니맵 생성
        stageMap = new StageMap(stages);        // 스테이지맵 생성
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // M 키 눌림 감지해서 스테이지맵 토글
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            isStageMapOpen = !isStageMapOpen;
        }



        if (!popup.isVisible()) {
            player.update(delta, walls);
        } else {
            player.stopMoving();
            player.update(delta, walls);
        }


        // 스테이지 이동 체크
        if (player.getPosition().x > currentStage.getWidth()) {
            moveToNextStage(true);
        } else if (player.getPosition().x < 0) {
            moveToNextStage(false);
        }

        // 카메라 위치 조정
        camera.position.x = player.getPosition().x + worldWidth / 8;
        camera.position.y = player.getPosition().y + worldHeight / 4;

        camera.position.x = Math.max(camera.position.x, worldWidth / 2);
        camera.position.x = Math.min(camera.position.x, currentStage.getWidth() - worldWidth / 2);
        camera.position.y = Math.max(camera.position.y, worldHeight / 2);

        camera.update();

        // 화면 클리어
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // 배경 그리기
        batch.draw(currentStage.getBackgroundTexture(), 0, 0, currentStage.getWidth(), currentStage.getHeight());

        // 벽 그리기
        for (Rectangle wall : walls) {
            batch.draw(brickTexture, wall.x, wall.y, wall.width, wall.height);
        }



        // 플레이어, 팝업 텍스트 렌더링
        player.render(batch);
        popup.renderText(batch);

        // 미니맵은 스테이지맵이 열리지 않았을 때만 그린다
        if (!isStageMapOpen) {
            miniMap.render(batch,player.getPosition());
        }
        for (BaseEntity entity : currentStage.getEntities()) {
            entity.update(delta);
            entity.render(batch);
        }

        batch.end();


        // 팝업 도형 렌더링
        popup.renderShape();



        // 스테이지맵은 열려 있을 때만 그린다
        if (isStageMapOpen) {
            stageMap.render(currentStageIndex);
        }

        if (Gdx.input.justTouched()) {
            int screenX = Gdx.input.getX();
            int screenY = Gdx.graphics.getHeight() - Gdx.input.getY();
            popup.handleClick(screenX, screenY);
        }
    }

    private void moveToNextStage(boolean toRight) {
        int nextIndex = currentStageIndex + (toRight ? 1 : -1);
        if (nextIndex < 0 || nextIndex >= stages.size()) return;

        currentStageIndex = nextIndex;
        currentStage = stages.get(currentStageIndex);

        miniMap.update(currentStage.getWidth(), currentStage.getHeight());
        System.out.println("stage moved");


        // 벽 갱신
        walls.clear();
        walls.addAll(currentStage.getWalls());

        for (BaseEntity entity : entities) {
            try {
                entity.updateMap(currentStage.getWalls(),player.getPosition());
                currentStage.getEntities().add(entity);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }



        // 플레이어 위치 재설정
        if (toRight) {
            player.setPosition(0, 40);
        } else {
            player.setPosition(currentStage.getWidth() - 10, 40);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        brickTexture.dispose();

        for (Stage s : stages) s.dispose();
        for (BaseEntity entity : currentStage.getEntities()) {
            entity.dispose();
        }

        player.dispose();
        popup.dispose();
    }
}
