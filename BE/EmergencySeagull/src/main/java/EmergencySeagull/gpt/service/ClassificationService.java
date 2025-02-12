package EmergencySeagull.gpt.service;

import static EmergencySeagull.common.exception.ExceptionCode.GPT_CLASSIFICATION_ERROR;

import EmergencySeagull.common.exception.CustomException;
import EmergencySeagull.gpt.dto.ClassificationRequest;
import EmergencySeagull.gpt.dto.ClassificationResponse;
import EmergencySeagull.report.enums.EmergencyCategory;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ClassificationService {

    private final RestTemplate restTemplate;

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.chat.url}")
    private String apiUrl;

    private static final String MODEL = "gpt-3.5-turbo";

    public String classifyText(String content) {
        StringBuilder categories = new StringBuilder();
        for (EmergencyCategory category : EmergencyCategory.values()) {
            categories.append(category.getDescription()).append(", ");
        }

        String categoryList = categories.substring(0, categories.length() - 2);

        ClassificationRequest request = ClassificationRequest.builder()
            .model(MODEL)
            .messages(Arrays.asList(
                ClassificationRequest.Message.builder()
                    .role("system")
                    .content("당신은 119 신고 내용을 분류하는 전문가입니다. " +
                        "주어진 신고 내용을 다음 카테고리 중 하나로 분류해주세요: " +
                        categoryList + ". " +
                        "답변은 카테고리명만 작성해주세요.")
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
                return response.getChoices().get(0).getMessage().getContent()
                    .trim();
            }
        } catch (Exception e) {
            throw new CustomException(GPT_CLASSIFICATION_ERROR);
        }

        return "생활안전"; // 기본값
    }
}