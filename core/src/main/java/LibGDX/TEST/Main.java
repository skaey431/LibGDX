package LibGDX.TEST;

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
    ArrayList<PatrolAI> patrolAIs;  // 여러 AI 관리용 리스트
    Map<String, PatrolAI> aiMap;    // AI ID별 빠른 조회용 맵
    Array<Rectangle> walls;
    Texture brickTexture;

    float worldWidth = 800;
    float worldHeight = 480;

    TestPopup popup;

    List<Stage> stages;
    int currentStageIndex;
    Stage currentStage;

    MiniMap miniMap;       // 미니맵 객체
    StageMap stageMap;     // 스테이지맵 객체

    boolean isStageMapOpen = false;  // 스테이지맵 열림 상태

    @Override
    public void create() {
        batch = new SpriteBatch();
        brickTexture = new Texture(Gdx.files.internal("resources/bricks.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, worldWidth, worldHeight);

        stages = new ArrayList<>();

        // 스테이지 생성 - AIInfo 리스트 사용
        stages.add(new Stage(
            "room1",
            "resources/room1.png",
            new Array<Rectangle>() {{
                add(new Rectangle(0, 40, 800, 0));
            }},
            new ArrayList<AIInfo>() {{
                add(new AIInfo("patrol1", "Patrol", new Vector2(400, 40)));
                add(new AIInfo("patrol2", "Patrol", new Vector2(200, 40)));
            }}
        ));

        stages.add(new Stage(
            "hallway1-2",
            "resources/hallway.png",
            new Array<Rectangle>() {{
                add(new Rectangle(0, 40, 1000, 0));
            }},
            new ArrayList<AIInfo>() {{
                add(new AIInfo("patrol3", "Patrol", new Vector2(600, 40)));
            }}
        ));

        stages.add(new Stage(
            "room2",
            "resources/room2.png",
            new Array<Rectangle>() {{
                add(new Rectangle(0, 40, 800, 0));
            }},
            new ArrayList<AIInfo>() {{
                add(new AIInfo("patrol4", "Patrol", new Vector2(300, 40)));
            }}
        ));

        currentStageIndex = 0;
        currentStage = stages.get(currentStageIndex);

        player = new Player(100, 40);

        walls = new Array<>();
        walls.addAll(currentStage.walls);

        // AI 객체들 생성 및 관리
        patrolAIs = new ArrayList<>();
        aiMap = new HashMap<>();

        for (AIInfo info : currentStage.getAiInfos()) {
            PatrolAI ai = null;
            if (info.type.equals("Patrol")) {
                ai = new PatrolAI(info.startPosition.x, info.startPosition.y);
                patrolAIs.add(ai);
                aiMap.put(info.id, ai);
            }
            // 필요하면 타입별 분기 추가 가능
        }

        popup = new TestPopup(200, 150);

        miniMap = new MiniMap(camera, currentStage.backgroundTexture.getHeight(), currentStage.backgroundTexture.getWidth());  // 미니맵 생성
        stageMap = new StageMap(stages);        // 스테이지맵 생성
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // M 키 눌림 감지해서 스테이지맵 토글
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            isStageMapOpen = !isStageMapOpen;
        }

        // 팝업과 AI 상태에 따른 입력 처리
        boolean anyAIStopped = patrolAIs.stream().anyMatch(PatrolAI::isStopped);
        if (anyAIStopped && !popup.isVisible() && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            popup.setVisible(true);
        }

        if (!popup.isVisible()) {
            player.update(delta, walls);
        } else {
            player.stopMoving();
            player.update(delta, walls);
        }

        // 모든 AI 업데이트 (플레이어 위치 전달)
        for (PatrolAI ai : patrolAIs) {
            ai.update(delta, walls, player.getPosition().x, player.getPosition().y);
        }

        // 스테이지 이동 체크
        if (player.getPosition().x > currentStage.width) {
            moveToNextStage(true);
        } else if (player.getPosition().x < 0) {
            moveToNextStage(false);
        }

        // 카메라 위치 조정
        camera.position.x = player.getPosition().x + worldWidth / 8;
        camera.position.y = player.getPosition().y + worldHeight / 4;

        camera.position.x = Math.max(camera.position.x, worldWidth / 2);
        camera.position.x = Math.min(camera.position.x, currentStage.width - worldWidth / 2);
        camera.position.y = Math.max(camera.position.y, worldHeight / 2);

        camera.update();

        // 화면 클리어
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // 배경 그리기
        batch.draw(currentStage.getBackgroundTexture(), 0, 0, currentStage.width, currentStage.height);

        // 벽 그리기
        for (Rectangle wall : walls) {
            batch.draw(brickTexture, wall.x, wall.y, wall.width, wall.height);
        }

        // AI 렌더링
        for (PatrolAI ai : patrolAIs) {
            ai.render(batch);
        }

        // 플레이어, 팝업 텍스트 렌더링
        player.render(batch);
        popup.renderText(batch);

        // 미니맵은 스테이지맵이 열리지 않았을 때만 그린다
        if (!isStageMapOpen) {
            miniMap.render(batch,player.getPosition().x, player.getPosition().y);
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

        // 벽 갱신
        walls.clear();
        walls.addAll(currentStage.walls);

        // AI 리스트 갱신 (이전 AI 객체들 dispose 필요 시 추가)
        for (PatrolAI ai : patrolAIs) ai.dispose();
        patrolAIs.clear();
        aiMap.clear();

        for (AIInfo info : currentStage.getAiInfos()) {
            PatrolAI ai = null;
            if (info.type.equals("Patrol")) {
                ai = new PatrolAI(info.startPosition.x, info.startPosition.y);
                patrolAIs.add(ai);
                aiMap.put(info.id, ai);
            }
        }

        // 플레이어 위치 재설정
        if (toRight) {
            player.setPosition(0, 40);
        } else {
            player.setPosition(currentStage.width - 10, 40);
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        brickTexture.dispose();

        for (Stage s : stages) s.dispose();
        for (PatrolAI ai : patrolAIs) ai.dispose();

        player.dispose();
        popup.dispose();
    }
}
