package LibGDX.TEST.entity.abstracClass;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public abstract class BaseCharacter extends BaseEntity {
    protected int currentStage;


    public BaseCharacter(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    @Override
    public void updateMap(Array<Rectangle> walls) { }

    @Override
    public void update(float delta) { }

    @Override
    public void render(SpriteBatch batch) { }

    @Override
    public void dispose() { }
    public abstract void setPosition(float x, float y);
    public abstract int getCurrentStage();
    public abstract void setCurrentStage(int stage);
}

