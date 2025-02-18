package EmergencySeagull.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClassificationResult {

    private String mainCategory;
    private String subCategory;
}