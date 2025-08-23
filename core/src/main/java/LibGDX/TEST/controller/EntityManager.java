package LibGDX.TEST.controller;

import LibGDX.TEST.entity.abstracClass.BaseEntity;
import LibGDX.TEST.map.Stage;

import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    private final List<BaseEntity> allEntities;

    public EntityManager() {
        this.allEntities = new ArrayList<>();
    }

    public void addEntity(BaseEntity entity){
        allEntities.add(entity);
    }

    public List<BaseEntity> getEntitiesInStage(Stage stage){
        return allEntities;
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
