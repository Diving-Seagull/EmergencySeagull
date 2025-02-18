package EmergencySeagull.report.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class ReportRequest {

    private String content;

    @NotNull(message = "위도를 입력해주세요")
    private Double latitude;

    @NotNull(message = "경도를 입력해주세요")
    private Double longitude;
}