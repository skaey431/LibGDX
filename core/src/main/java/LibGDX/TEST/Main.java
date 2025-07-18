package LibGDX.TEST;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private Texture frame1;
    private Texture frame2;

    private float x = 100, y = 100;
    private final float speed = 100f;

    private Rectangle playerRect;
    private List<Rectangle> walls;

    private float animationTimer = 0f;
    private final float frameDuration = 0.25f; // 프레임 교체 간격
    private boolean showFrame1 = true;
    private boolean lookingLeft;
    private TextureRegion    frame1Region;
    private TextureRegion frame2Region;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        frame1 = new Texture(Gdx.files.internal("resources/chatgpt_character_walking_0001.png"));
        frame2 = new Texture(Gdx.files.internal("resources/chatgpt_character_walking_0002.png"));
        frame1Region = new TextureRegion(frame1);
        frame2Region = new TextureRegion(frame2);

        playerRect = new Rectangle(x, y, frame1.getWidth(), frame1.getHeight());

        walls = new ArrayList<>();
        walls.add(new Rectangle(500, 100, 50, 300)); // 세로 벽
        walls.add(new Rectangle(500, 300, 200, 50)); // 가로 벽
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handleInput(delta);

        animationTimer += delta;
        if (animationTimer >= frameDuration) {
            animationTimer -= frameDuration;
            showFrame1 = !showFrame1;
        }

        batch.begin();
        TextureRegion currentFrame = showFrame1 ? frame1Region : frame2Region;
        if (!lookingLeft && !currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        } else if (lookingLeft && currentFrame.isFlipX()) {
            currentFrame.flip(true, false);
        }
        batch.draw(currentFrame, x, y);
        batch.end();

        // 벽 그리기
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 1); // 빨간색 벽
        for (Rectangle wall : walls) {
            shapeRenderer.rect(wall.x, wall.y, wall.width, wall.height);
        }
        shapeRenderer.end();
    }

    private void handleInput(float delta) {
        float oldX = x;
        float oldY = y;

        boolean moved = false;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= speed * delta;
            moved = true;
            lookingLeft = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += speed * delta;
            moved = true;
            lookingLeft = false;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            y += speed * delta;
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            y -= speed * delta;
            moved = true;
        }

        playerRect.setPosition(x, y);

        // 벽과 충돌 체크
        for (Rectangle wall : walls) {
            if (playerRect.overlaps(wall)) {
                x = oldX;
                y = oldY;
                playerRect.setPosition(x, y);
                break;
            }
        }

        if (!moved) {
            animationTimer = 0f;
            showFrame1 = true;
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        frame1.dispose();
        frame2.dispose();
    }
}
