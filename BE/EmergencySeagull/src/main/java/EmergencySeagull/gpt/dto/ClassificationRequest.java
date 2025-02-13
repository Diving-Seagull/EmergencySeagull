package EmergencySeagull.gpt.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClassificationRequest {

    private String model;
    private List<Message> messages;
    private double temperature;

    @Data
    @Builder
    public static class Message {

        private String role;
        private String content;
    }
}
