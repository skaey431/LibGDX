// Main.java (일부 수정)
package LibGDX.TEST;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Main extends ApplicationAdapter {
    SpriteBatch batch;
    Player player;
    PatrolAI ai;
    Array<Rectangle> walls;
    TestPopup popup;

    float worldWidth = 800;
    float worldHeight = 480;

    @Override
    public void create() {
        batch = new SpriteBatch();

        walls = new Array<>();
        walls.add(new Rectangle(0, 0, worldWidth, 40));      // 바닥
        walls.add(new Rectangle(0, 0, 20, worldHeight));     // 왼쪽 벽
        walls.add(new Rectangle(worldWidth - 20, 0, 20, worldHeight)); // 오른쪽 벽
        walls.add(new Rectangle(300, 40, 200, 40));          // 중간 벽

        player = new Player(100, 80);
        ai = new PatrolAI(400, 80);

        popup = new TestPopup(200, 150, 400, 200);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // 팝업 열림 상태 체크
        if (popup.isVisible()) {
            // 팝업이 열려 있으면 입력은 팝업에만 전달
            if (Gdx.input.justTouched()) {
                float clickX = Gdx.input.getX();
                float clickY = Gdx.graphics.getHeight() - Gdx.input.getY(); // 좌표계 변환
                popup.handleClick(clickX, clickY);
            }
        } else {
            // 팝업 닫힌 상태면 플레이어와 AI 업데이트

            player.update(delta, walls);
            ai.update(delta, walls,player.getPosition().x,player.getPosition().y);

            // AI가 멈춰있고 플레이어가 일정거리 내면 F 눌러서 팝업 열기
/*            if (ai.isStopped() && player.getPosition().dst(ai.getPosition()) < 100) {
                if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
                    popup.setVisible(true);
                }
            }*/
        }

        Gdx.gl.glClearColor(0.1f, 0.1f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // 벽 렌더링 (회색)
        for (Rectangle wall : walls) {
            batch.draw(new com.badlogic.gdx.graphics.Texture(Gdx.files.internal("resources/bricks.png")),
                wall.x, wall.y, wall.width, wall.height);
        }

        player.render(batch);
        ai.render(batch);

        // AI가 정지하면 AI 위에 'F' 표시
        if (ai.isStopped()) {
            // 간단 텍스트 대신 빨간색 글자 출력 등 처리 가능
            // BitmapFont 필요, 여기선 생략
        }

        batch.end();

        // 팝업 렌더링 (ShapeRenderer 사용하므로 batch.end() 후 호출)
        popup.render(batch);
    }

    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        ai.dispose();
        popup.dispose();
    }
}
