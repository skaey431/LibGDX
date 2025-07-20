package LibGDX.TEST;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;

    private Texture frame1;
    private Texture frame2;
    private Texture brickTexture;
    private TextureRegion brickRegion;

    private float x = 100, y = 300;
    private float velocityY = 0f;
    private final float speed = 100f;
    private final float gravity = -600f;
    private final float jumpPower = 400f;
    private boolean onGround = false;

    private Rectangle playerRect;
    private List<Rectangle> walls;

    private float animationTimer = 0f;
    private final float frameDuration = 0.25f;
    private boolean showFrame1 = true;
    private boolean lookingLeft = false;

    private float stageLeftBound = 0f;
    private float stageRightBound = 800f;
    private float backgroundColor = 0f;

    private boolean showPopup = false;
    private Rectangle popupBounds;
    private Rectangle closeButton;

    private PatrolAI ai; // <-- 분리된 AI 인스턴스

    @Override
    public void create() {
        batch = new SpriteBatch();

        frame1 = new Texture(Gdx.files.internal("resources/chatgpt_character_walking_0001.png"));
        frame2 = new Texture(Gdx.files.internal("resources/chatgpt_character_walking_0002.png"));

        brickTexture = new Texture(Gdx.files.internal("resources/bricks.png"));
        brickTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        brickRegion = new TextureRegion(brickTexture);

        playerRect = new Rectangle(x, y, frame1.getWidth(), frame1.getHeight());

        walls = new ArrayList<>();
        walls.add(new Rectangle(0, 50, 800, 50));
        walls.add(new Rectangle(500, 100, 50, 300));

        popupBounds = new Rectangle(200, 200, 300, 150);
        closeButton = new Rectangle(200 + 300 - 30, 200 + 150 - 30, 20, 20);

        // 예시: AI를 화면 중앙에 배치
        ai = new PatrolAI(
            "resources/chatgpt_character_walking_0001.png",
            "resources/chatgpt_character_walking_0002.png",
            300, 100 // 원하는 위치 (x, y)
        );

    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(backgroundColor, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!showPopup) {
            handleInput(delta);

            velocityY += gravity * delta;
            y += velocityY * delta;
            playerRect.setPosition(x, y);
            onGround = false;

            for (Rectangle wall : walls) {
                if (playerRect.overlaps(wall)) {
                    if (velocityY < 0) {
                        y = wall.y + wall.height;
                        onGround = true;
                        velocityY = 0;
                    } else if (velocityY > 0) {
                        y = wall.y - playerRect.height;
                        velocityY = 0;
                    }
                    playerRect.setPosition(x, y);
                }
            }

            // 스테이지 이동 효과
            if (x > stageRightBound) {
                x = stageLeftBound;
                backgroundColor = (backgroundColor + 0.2f) % 1f;
            } else if (x + playerRect.width < stageLeftBound) {
                x = stageRightBound - playerRect.width;
                backgroundColor = (backgroundColor + 0.2f) % 1f;
            }

            // AI 업데이트 (벽을 피하는 로직 포함)
            ai.update(delta, walls);
        }

        // 애니메이션 타이머
        animationTimer += delta;
        if (animationTimer >= frameDuration) {
            animationTimer -= frameDuration;
            showFrame1 = !showFrame1;
        }

        batch.begin();

        // 벽 그리기
        for (Rectangle wall : walls) {
            brickRegion.setRegion(0, 0, (int) wall.width, (int) wall.height);
            batch.draw(brickRegion, wall.x, wall.y, wall.width, wall.height);
        }

        // 캐릭터 그리기
        Texture currentFrame = showFrame1 ? frame1 : frame2;
        if (lookingLeft) {
            batch.draw(currentFrame, x, y);
        } else {
            batch.draw(currentFrame, x + currentFrame.getWidth(), y,
                -currentFrame.getWidth(), currentFrame.getHeight());
        }

        // AI 그리기
        ai.render(batch);

        // 팝업
        if (showPopup) {
            batch.end();
            batch.begin();
            batch.setColor(1, 1, 1, 1);
            batch.draw(brickTexture, popupBounds.x, popupBounds.y, popupBounds.width, popupBounds.height);
            batch.draw(frame1, closeButton.x, closeButton.y, closeButton.width, closeButton.height);
        }

        batch.end();
    }



    private void handleInput(float delta) {
        float oldX = x;
        boolean moved = false;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= speed * delta;
            lookingLeft = true;
            moved = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += speed * delta;
            lookingLeft = false;
            moved = true;
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && onGround) {
            velocityY = jumpPower;
            onGround = false;
        }

        playerRect.setPosition(x, y);

        for (Rectangle wall : walls) {
            if (playerRect.overlaps(wall)) {
                x = oldX;
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
        frame1.dispose();
        frame2.dispose();
        brickTexture.dispose();
        ai.dispose();
    }
}
