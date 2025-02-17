package EmergencySeagull.report.enums;

import lombok.Getter;

@Getter
public enum EmergencyCategory {
    FIRE("화재"),
    RESCUE("구조"),
    MEDICAL("구급"),
    SAFETY("생활안전");

    private final String description;

    EmergencyCategory(String description) {
        this.description = description;
    }

    public static EmergencyCategory fromDescription(String description) {
        for (EmergencyCategory category : values()) {
            if (category.getDescription().equals(description)) {
                return category;
            }
        }
        return SAFETY;
    }
}