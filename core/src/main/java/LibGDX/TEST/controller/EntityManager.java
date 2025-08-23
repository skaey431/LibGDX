package LibGDX.TEST.controller;

import LibGDX.TEST.entity.abstracClass.BaseEntity;
import LibGDX.TEST.map.Stage;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    private final Array<BaseEntity> allEntities;

    public EntityManager() {
        this.allEntities = new Array<>();
    }

    public void addEntity(BaseEntity entity){
        allEntities.add(entity);
    }

    public Array<BaseEntity> getEntitiesInStage(Stage stage){
        return stage.getEntities();
    }

    public void updateEntities(float delta, Stage stage){
        for (BaseEntity entity : stage.getEntities()) {
            entity.updateMap(stage.getWalls());
            entity.update(delta);
        }
    }
    

    public void renderEntities(com.badlogic.gdx.graphics.g2d.SpriteBatch batch, Stage stage){
        getEntitiesInStage(stage).forEach(e -> e.render(batch));
    }

    public void disposeAll(){
        allEntities.forEach(BaseEntity::dispose);
    }
}
