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
import LibGDX.TEST.StageMap;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    SpriteBatch batch;
    OrthographicCamera camera;
    Player player;
    PatrolAI ai;
    Array<Rectangle> walls;
    Texture brickTexture;

    float worldWidth = 800;
    float worldHeight = 480;

    TestPopup popup;

    List<Stage> stages;
    int currentStageIndex;
    Stage currentStage;

    StageMap stageMap;

    @Override
    public void create() {
        batch = new SpriteBatch();
        brickTexture = new Texture(Gdx.files.internal("resources/bricks.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, worldWidth, worldHeight);

        stages = new ArrayList<>();

        // Stage 1 - 방1
        stages.add(new Stage(
            "resources/room1.png",
            new Array<Rectangle>() {{
                add(new Rectangle(0, 40, 800, 0));
            }},
            400, 40
        ));

        // Stage 2 - 복도
        stages.add(new Stage(
            "resources/hallway.png",
            new Array<Rectangle>() {{
                add(new Rectangle(0, 40, 1000, 0));
            }},
            600, 40
        ));

        // Stage 3 - 방2
        stages.add(new Stage(
            "resources/room2.png",
            new Array<Rectangle>() {{
                add(new Rectangle(0, 40, 800, 0));
            }},
            300, 40
        ));

        currentStageIndex = 0;
        currentStage = stages.get(currentStageIndex);

        player = new Player(100, 40);
        ai = new PatrolAI(currentStage.aiStartX, currentStage.aiStartY);
        popup = new TestPopup(200, 150);

        walls = new Array<>();
        walls.addAll(currentStage.walls);

        stageMap = new StageMap(stages); // 전체 맵 생성
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (ai.isStopped() && !popup.isVisible() && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            popup.setVisible(true);
        }

        if (!popup.isVisible()) {
            player.update(delta, walls);
        } else {
            player.stopMoving();
            player.update(delta, walls);
        }

        ai.update(delta, walls, player.getPosition().x, player.getPosition().y);

        if (player.getPosition().x > currentStage.width) {
            moveToNextStage(true);
        } else if (player.getPosition().x < 0) {
            moveToNextStage(false);
        }

        camera.position.x = player.getPosition().x + worldWidth / 8;
        camera.position.y = player.getPosition().y + worldHeight / 4;

        camera.position.x = Math.max(camera.position.x, worldWidth / 2);
        camera.position.x = Math.min(camera.position.x, currentStage.width - worldWidth / 2);
        camera.position.y = Math.max(camera.position.y, worldHeight / 2);

        camera.update();

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(currentStage.backgroundTexture, 0, 0, currentStage.width, currentStage.height);

        for (Rectangle wall : walls) {
            batch.draw(brickTexture, wall.x, wall.y, wall.width, wall.height);
        }

        ai.render(batch);
        player.render(batch);
        popup.renderText(batch);

        batch.end();

        popup.renderShape();

        if (Gdx.input.justTouched()) {
            int screenX = Gdx.input.getX();
            int screenY = Gdx.input.getY();
            popup.handleClick(screenX, Gdx.graphics.getHeight() - screenY);
        }

        // 전체 맵 렌더링
        stageMap.render(currentStageIndex);
    }

    private void moveToNextStage(boolean toRight) {
        int nextIndex = currentStageIndex + (toRight ? 1 : -1);
        if (nextIndex < 0 || nextIndex >= stages.size()) return;

        currentStageIndex = nextIndex;
        currentStage = stages.get(currentStageIndex);

        ai.setPosition(currentStage.aiStartX, currentStage.aiStartY);

        walls.clear();
        walls.addAll(currentStage.walls);

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
        player.dispose();
        ai.dispose();
        popup.dispose();
        stageMap.dispose();
    }
}
