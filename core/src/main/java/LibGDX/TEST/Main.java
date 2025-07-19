package LibGDX.TEST;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private Texture frame1, frame2;
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
    private boolean lookingLeft = true;

    private Texture brickTexture;
    private TextureRegion brickRegion;

    private boolean showPopup = false;
    private Rectangle popupRect;
    private Rectangle closeButtonRect;

    private Color currentBackgroundColor = new Color(0, 0, 0, 1);

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        frame1 = new Texture(Gdx.files.internal("resources/chatgpt_character_walking_0001.png"));
        frame2 = new Texture(Gdx.files.internal("resources/chatgpt_character_walking_0002.png"));

        brickTexture = new Texture(Gdx.files.internal("resources/bricks.png"));
        brickTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        brickRegion = new TextureRegion(brickTexture);

        playerRect = new Rectangle(x, y, frame1.getWidth(), frame1.getHeight());

        walls = new ArrayList<>();
        walls.add(new Rectangle(0, 50, 800, 50));     // 바닥
        walls.add(new Rectangle(500, 100, 50, 300));   // 벽
        walls.add(new Rectangle(300, 100, 150, 30));   // 플랫폼

        popupRect = new Rectangle((Gdx.graphics.getWidth() - 300) / 2f,
            (Gdx.graphics.getHeight() - 200) / 2f,
            300, 200);

        closeButtonRect = new Rectangle(popupRect.x + popupRect.width - 30,
            popupRect.y + popupRect.height - 30,
            20, 20);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        Gdx.gl.glClearColor(currentBackgroundColor.r, currentBackgroundColor.g, currentBackgroundColor.b, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 팝업 입력 처리
        if (showPopup && Gdx.input.justTouched()) {
            int mouseX = Gdx.input.getX();
            int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (closeButtonRect.contains(mouseX, mouseY)) {
                showPopup = false;
            }
        }

        // 팝업 중 아닐 때만 캐릭터 조작
        if (!showPopup) {
            handleInput(delta);

            // 중력 적용
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

            animationTimer += delta;
            if (animationTimer >= frameDuration) {
                animationTimer -= frameDuration;
                showFrame1 = !showFrame1;
            }

            // 양쪽 끝 넘어가면 반대쪽으로 보내기 + 배경색 전환
            if (x < -playerRect.width) {
                x = Gdx.graphics.getWidth();
                changeBackgroundColor();
            } else if (x > Gdx.graphics.getWidth()) {
                x = -playerRect.width;
                changeBackgroundColor();
            }
        }

        // 캐릭터 및 벽 그리기
        batch.begin();

        // 벽
        for (Rectangle wall : walls) {
            brickRegion.setRegion(0, 0, (int) wall.width, (int) wall.height);
            batch.draw(brickRegion, wall.x, wall.y, wall.width, wall.height);
        }

        // 캐릭터
        Texture currentFrame = showFrame1 ? frame1 : frame2;
        if (lookingLeft) {
            batch.draw(currentFrame, x, y);
        } else {
            batch.draw(currentFrame, x + currentFrame.getWidth(), y, -currentFrame.getWidth(), currentFrame.getHeight());
        }

        batch.end();

        // 팝업 렌더링
        if (showPopup) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.rect(popupRect.x, popupRect.y, popupRect.width, popupRect.height);

            // 닫기 버튼 (X)
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(closeButtonRect.x, closeButtonRect.y, closeButtonRect.width, closeButtonRect.height);
            shapeRenderer.end();
        }

        // F 키로 팝업 토글
        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            showPopup = true;
        }
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

    private void changeBackgroundColor() {
        currentBackgroundColor = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        frame1.dispose();
        frame2.dispose();
        brickTexture.dispose();
    }
}
