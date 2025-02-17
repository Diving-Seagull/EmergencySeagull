package EmergencySeagull.report.enums;

import lombok.Getter;
import java.util.Arrays;
import java.util.List;

@Getter
public enum MedicalCategory {
    RESUSCITATION("소생", Arrays.asList(
        "심장마비",
        "무호흡",
        "음주와 관련되지 않은 무의식"
    )),

    EMERGENCY("긴급", Arrays.asList(
        "심근경색",
        "뇌출혈",
        "뇌경색"
    )),

    URGENT("응급", Arrays.asList(
        "호흡곤란",
        "출혈을 동반한 설사"
    )),

    SEMI_URGENT("준응급", Arrays.asList(
        "발열을 동반한 장염",
        "복통을 동반한 요로감염"
    )),

    NON_URGENT("비응급", Arrays.asList(
        "감기",
        "장염",
        "설사",
        "열상"
    ));

    private final String description;
    private final List<String> details;

    MedicalCategory(String description, List<String> details) {
        this.description = description;
        this.details = details;
    }

    public static MedicalCategory fromDescription(String description) {
        for (MedicalCategory category : values()) {
            if (category.getDescription().equals(description)) {
                return category;
            }
        }
        return NON_URGENT;
    }

    public static MedicalCategory fromDetail(String detail) {
        for (MedicalCategory category : values()) {
            if (category.getDetails().contains(detail)) {
                return category;
            }
        }
        return NON_URGENT;
    }
}
