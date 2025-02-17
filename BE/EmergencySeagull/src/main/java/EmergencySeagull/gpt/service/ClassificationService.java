package EmergencySeagull.gpt.service;

import static EmergencySeagull.common.exception.ExceptionCode.GPT_CLASSIFICATION_ERROR;

import EmergencySeagull.common.exception.CustomException;
import EmergencySeagull.gpt.dto.ClassificationRequest;
import EmergencySeagull.gpt.dto.ClassificationResponse;
import EmergencySeagull.gpt.dto.ClassificationResult;
import EmergencySeagull.report.enums.EmergencyCategory;
import EmergencySeagull.report.enums.FireCategory;
import EmergencySeagull.report.enums.MedicalCategory;
import EmergencySeagull.report.enums.RescueCategory;
import EmergencySeagull.report.enums.SafetyCategory;
import java.util.Arrays;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassificationService {

    private final RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.chat.url}")
    private String apiUrl;

    private static final String MODEL = "gpt-3.5-turbo";

    public ClassificationResult classifyText(String content) {
        // 대분류 카테고리 목록 생성
        StringBuilder mainCategories = new StringBuilder();
        for (EmergencyCategory category : EmergencyCategory.values()) {
            mainCategories.append(category.getDescription()).append(", ");
        }
        String mainCategoryList = mainCategories.substring(0, mainCategories.length() - 2);

        // 대분류 분류 요청
        String mainCategory = requestClassification(content, "다음 대분류 카테고리 중 하나로 분류해주세요: " +
            mainCategoryList + ". 답변은 카테고리명만 작성해주세요.");

        // 소분류 카테고리 목록 및 분류
        String subCategory = getSubCategory(mainCategory, content);

        return new ClassificationResult(mainCategory, subCategory);
    }

    private String getSubCategory(String mainCategory, String content) {
        String prompt;

        if (mainCategory.equals("구급")) {
            StringBuilder medicalPrompt = new StringBuilder();
            medicalPrompt.append("다음 증상 분류 기준을 참고하여 가장 적절한 단계를 선택해주세요:\n\n");

            for (MedicalCategory category : MedicalCategory.values()) {
                medicalPrompt.append(category.getDescription())
                    .append(" - ")
                    .append(String.join(", ", category.getDetails()))
                    .append("\n");
            }

            medicalPrompt.append("\n답변은 단계만 작성해주세요.");
            prompt = medicalPrompt.toString();
        } else {
            String subCategoryList = getEnumDescriptions(
                Objects.requireNonNull(getEnumByMainCategory(mainCategory)));
            prompt = "다음 소분류 카테고리 중 하나로 분류해주세요: " +
                subCategoryList + ". 답변은 카테고리명만 작성해주세요.";
        }

        return requestClassification(content, prompt);
    }

    private Enum<?>[] getEnumByMainCategory(String mainCategory) {
        return switch (mainCategory) {
            case "화재" -> FireCategory.values();
            case "구조" -> RescueCategory.values();
            case "생활안전" -> SafetyCategory.values();
            default -> null;
        };
    }

    private String getEnumDescriptions(Enum<?>[] values) {
        StringBuilder descriptions = new StringBuilder();
        for (Enum<?> value : values) {
            if (value instanceof FireCategory) {
                descriptions.append(((FireCategory) value).getDescription());
            } else if (value instanceof RescueCategory) {
                descriptions.append(((RescueCategory) value).getDescription());
            } else if (value instanceof MedicalCategory) {
                descriptions.append(((MedicalCategory) value).getDescription());
            } else if (value instanceof SafetyCategory) {
                descriptions.append(((SafetyCategory) value).getDescription());
            }
            descriptions.append(", ");
        }
        return descriptions.substring(0, descriptions.length() - 2);
    }

    private String requestClassification(String content, String systemPrompt) {

        log.info(content);
        log.info(systemPrompt);

        ClassificationRequest request = ClassificationRequest.builder()
            .model(MODEL)
            .messages(Arrays.asList(
                ClassificationRequest.Message.builder()
                    .role("system")
                    .content("당신은 119 신고 내용을 분류하는 전문가입니다. " + systemPrompt)
                    .build(),
                ClassificationRequest.Message.builder()
                    .role("user")
                    .content(content)
                    .build()
            ))
            .temperature(0.3)
            .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<ClassificationRequest> entity = new HttpEntity<>(request, headers);

        try {
            ClassificationResponse response = restTemplate.postForObject(apiUrl, entity,
                ClassificationResponse.class);

            if (response != null && !response.getChoices().isEmpty()) {
                return response.getChoices().get(0).getMessage().getContent().trim();
            }
        } catch (Exception e) {
            throw new CustomException(GPT_CLASSIFICATION_ERROR);
        }

        return "기타";
    }
}