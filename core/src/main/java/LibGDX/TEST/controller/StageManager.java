package LibGDX.TEST.controller;

import LibGDX.TEST.entity.abstracClass.BaseCharacter;
import LibGDX.TEST.entity.abstracClass.BaseEntity;
import LibGDX.TEST.entity.Player;
import LibGDX.TEST.map.Stage;

import java.util.List;

public class StageManager {
    private final List<Stage> stages;
    private int currentIndex;

    public StageManager(List<Stage> stages){
        this.stages = stages;
        this.currentIndex = 0;
    }

    public Stage getCurrentStage() {
        return stages.get(currentIndex);
    }
    public void setCurrentStage(int stage) {
        this.currentIndex = stage;
    }

    public void moveToNextStage(boolean toRight, BaseCharacter character){
        System.out.println("Moving to next stage");
        int nextIndex = currentIndex + (toRight ? 1 : -1);
        if(nextIndex < 0 || nextIndex >= stages.size()) return;

        character.setCurrentStage(nextIndex);
        // 플레이어 재배치
        if(toRight) character.setPosition(0, character.getPosition().y);
        else character.setPosition(stages.get(character.getCurrentStage()).getWidth(), character.getPosition().y);
        System.out.println(stages.get(character.getCurrentStage()).getWidth());

    }


    public void updateEntity(BaseEntity entity){
        getCurrentStage().getEntities().add(entity);
    }

    public int getCurrentIndex(){
        return currentIndex;
    }
}
