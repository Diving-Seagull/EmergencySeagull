package EmergencySeagull.gpt.dto;

import java.util.List;
import lombok.Data;

@Data
public class ClassificationResponse {

    private List<Choice> choices;

    @Data
    public static class Choice {

        private Message message;
        private int index;
        private String finishReason;
    }

    @Data
    public static class Message {

        private String role;
        private String content;
    }
}