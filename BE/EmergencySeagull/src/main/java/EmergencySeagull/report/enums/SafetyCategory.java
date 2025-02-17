package EmergencySeagull.report.enums;

import lombok.Getter;

@Getter
public enum SafetyCategory {
    BEEHIVE("벌집제거"),
    ANIMAL("동물처리"),
    LOCK_OPEN("잠금장치 개방"),
    OBSTACLE("장애물 제거 및 안전 조치"),
    LOCATION("위치확인"),
    LIFE_STUCK("생활끼임"),
    DAMAGE_SUPPORT("피해복구지원"),
    NON_FIRE_CHECK("비화재보 확인"),
    DISEASE_SUPPORT("감염병 지원"),
    EVENT_SUPPORT("행사장 지원"),
    OTHER("기타");

    private final String description;

    SafetyCategory(String description) {
        this.description = description;
    }

    public static SafetyCategory fromDescription(String description) {
        for (SafetyCategory category : values()) {
            if (category.getDescription().equals(description)) {
                return category;
            }
        }
        return OTHER;
    }
}