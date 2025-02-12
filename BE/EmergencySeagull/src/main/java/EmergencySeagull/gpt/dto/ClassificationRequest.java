package EmergencySeagull.gpt.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
