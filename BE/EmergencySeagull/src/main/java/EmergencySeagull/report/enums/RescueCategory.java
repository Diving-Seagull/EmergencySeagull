package EmergencySeagull.report.enums;

import lombok.Getter;

@Getter
public enum RescueCategory {
    TRAFFIC("교통사고"),
    ELEVATOR("승강기사고"),
    ENTRAPMENT("인명갇힘"),
    SUICIDE_ATTEMPT("자살기도"),
    MOUNTAIN("산악사고"),
    WATER("수난사고"),
    FALL("추락사고"),
    STUCK("끼임사고"),
    COLLAPSE("붕괴·도괴"),
    EXPLOSION("폭발사고"),
    LEAKAGE("누출사고"),
    TERROR("테러(의심)"),
    AIRCRAFT("항공기 사고"),
    OTHER("기타");

    private final String description;

    RescueCategory(String description) {
        this.description = description;
    }

    public static RescueCategory fromDescription(String description) {
        for (RescueCategory category : values()) {
            if (category.getDescription().equals(description)) {
                return category;
            }
        }
        return OTHER;
    }
}