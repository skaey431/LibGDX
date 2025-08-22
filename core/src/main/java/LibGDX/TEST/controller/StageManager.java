package LibGDX.TEST.controller;

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

    public void moveToNextStage(boolean toRight){
        int nextIndex = currentIndex + (toRight ? 1 : -1);
        if(nextIndex < 0 || nextIndex >= stages.size()) return;
        currentIndex = nextIndex;
    }

    public int getCurrentIndex(){
        return currentIndex;
    }
}
