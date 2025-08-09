package LibGDX.TEST.entity.AI;

import com.badlogic.gdx.math.Vector2;

public class AIInfo {
    public final String id;       // AI 고유 ID 또는 이름
    public final String type;     // AI 타입 (예: "Patrol", "Guard" 등)
    public final Vector2 startPosition; // 시작 위치

    public AIInfo(String id, String type, Vector2 startPosition) {
        this.id = id;
        this.type = type;
        this.startPosition = startPosition;
    }
}
