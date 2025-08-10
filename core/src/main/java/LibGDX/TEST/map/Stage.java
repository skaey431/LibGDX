package LibGDX.TEST.map;

import LibGDX.TEST.entity.AI.AIInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class Stage {
    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Array<Rectangle> getWalls() {
        return walls;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getSize() {
        return size;
    }

    public boolean isVertical() {
        return vertical;
    }

    public Vector2 getSizeVector() {
        return sizeVector;
    }

    public final String id;  // 스테이지 고유 ID

    Texture backgroundTexture;
    float width, height;
    Array<Rectangle> walls;
    ArrayList<AIInfo> aiInfos;  // AI 정보 리스트 (ID, 타입, 위치 포함)
    float x;
    float y;
    float size;
    boolean vertical;
    Vector2 sizeVector;

    public String getId() {
        return id;
    }

    public Stage(String id, String backgroundPath, Array<Rectangle> walls, ArrayList<AIInfo> aiInfos, float x, float y, float size, boolean vertical) {
        this.id = id;
        this.backgroundTexture = new Texture(Gdx.files.internal(backgroundPath));
        this.width = backgroundTexture.getWidth();
        this.height = backgroundTexture.getHeight();
        this.walls = walls;
        this.aiInfos = aiInfos;
        this.x = x;
        this.y = y;
        this.size = size;
        if (vertical){
            sizeVector = new Vector2(width/16,size);
        }else {
            sizeVector = new Vector2(size,width/16);
        }


        // 바닥 추가
        walls.add(new Rectangle(0, 40, backgroundTexture.getWidth(), 0));
    }

    public Texture getBackgroundTexture() {
        return backgroundTexture;
    }

    public ArrayList<AIInfo> getAiInfos() {
        return aiInfos;
    }

    public void dispose() {
        backgroundTexture.dispose();
    }
}
