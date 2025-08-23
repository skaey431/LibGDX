package LibGDX.TEST.controller;

import LibGDX.TEST.entity.abstracClass.BaseEntity;
import LibGDX.TEST.map.MiniMap;
import LibGDX.TEST.map.StageMap;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class UIManager {
    private final MiniMap miniMap;
    private final StageMap stageMap;
    private boolean isStageMapOpen;

    public UIManager(OrthographicCamera uiCamera, StageMap stageMap, MiniMap miniMap){
        this.stageMap = stageMap;
        this.miniMap = miniMap;
        this.isStageMapOpen = false;
    }

    public void toggleStageMap(){
        isStageMapOpen = !isStageMapOpen;
    }

    public boolean isStageMapOpen() {
        return isStageMapOpen;
    }

    public void render(SpriteBatch batch, OrthographicCamera uiCamera, BaseEntity player, Array<BaseEntity> entities, int currentStageIndex){
        try{
            batch.begin();
            batch.setProjectionMatrix(uiCamera.combined);
            if(!isStageMapOpen){
                miniMap.render(batch, player.getPosition(), (Array<BaseEntity>) entities);
            }

            if(isStageMapOpen){
                stageMap.render(currentStageIndex);
            }
            batch.end();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
