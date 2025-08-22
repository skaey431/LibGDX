package LibGDX.TEST.controller;

import LibGDX.TEST.entity.BaseEntity;
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
        getEntitiesInStage(stage).forEach(e -> e.updateMap(stage.getWalls()));
    }

    public void renderEntities(com.badlogic.gdx.graphics.g2d.SpriteBatch batch, Stage stage){
        getEntitiesInStage(stage).forEach(e -> e.render(batch));
    }

    public void disposeAll(){
        allEntities.forEach(BaseEntity::dispose);
    }
}
