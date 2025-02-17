package EmergencySeagull.report.enums;

import lombok.Getter;

@Getter
public enum FireCategory {
    GENERAL_FIRE("일반화재"),
    OIL_FIRE("유류화재"),
    ELECTRICAL_FIRE("전기화재"),
    METAL_FIRE("금속화재"),
    GAS_FIRE("가스화재"),
    COOKING_OIL_FIRE("식용유화재");

    private final String description;

    FireCategory(String description) {
        this.description = description;
    }

    public static FireCategory fromDescription(String description) {
        for (FireCategory category : values()) {
            if (category.getDescription().equals(description)) {
                return category;
            }
        }
        return GENERAL_FIRE;
    }
}