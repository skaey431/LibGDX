package LibGDX.TEST.controller;

import LibGDX.TEST.map.Stage;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public class CameraController {
    private final OrthographicCamera camera;
    private final float worldWidth, worldHeight;

    public CameraController(OrthographicCamera camera, float worldWidth, float worldHeight){
        this.camera = camera;
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
    }

    public void update(Vector2 playerPos, Stage stage) {
        camera.position.x = playerPos.x + worldWidth / 8;
        camera.position.y = playerPos.y + worldHeight / 4;

        camera.position.x = Math.max(camera.position.x, worldWidth / 2);
        camera.position.x = Math.min(camera.position.x, stage.getWidth() - worldWidth / 2);
        camera.position.y = Math.max(camera.position.y, worldHeight / 2);

        camera.update();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
