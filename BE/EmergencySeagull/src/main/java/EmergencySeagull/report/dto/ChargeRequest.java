package EmergencySeagull.report.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class ChargeRequest {

    @NotBlank(message = "담당 구역을 입력해주세요")
    @Pattern(regexp = "^.+(구|군)$", message = "구 또는 군으로 끝나야 합니다")
    private String charge;
}
