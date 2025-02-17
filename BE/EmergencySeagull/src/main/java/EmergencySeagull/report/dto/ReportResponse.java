package EmergencySeagull.report.dto;

import EmergencySeagull.report.entity.Report;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReportResponse {

    private Long id;
    private String content;
    private String category;
    private String subCategory;
    private Double latitude;
    private Double longitude;
    private String createdAt;

    public ReportResponse(Report report) {
        this.id = report.getId();
        this.content = report.getContent();
        this.category = report.getCategory().getDescription();
        this.subCategory = report.getSubCategory();
        this.latitude = report.getLatitude();
        this.longitude = report.getLongitude();
        this.createdAt = report.getCreatedAt().toString();
    }
}