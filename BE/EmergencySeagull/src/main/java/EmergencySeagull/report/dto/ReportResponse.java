package EmergencySeagull.report.dto;

import EmergencySeagull.report.entity.Report;
import EmergencySeagull.report.entity.ReportDocument;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReportResponse {

    private Long id;
    private String content;
    private String category;
    private Double latitude;
    private Double longitude;
    private String createdAt;

    public ReportResponse(Report report) {
        this.id = report.getId();
        this.content = report.getContent();
        this.category = report.getCategory().getDescription();
        this.latitude = report.getLatitude();
        this.longitude = report.getLongitude();
        this.createdAt = report.getCreatedAt().toString();
    }

    public ReportResponse(ReportDocument reportDocument) {
        this.id = reportDocument.getId();
        this.content = reportDocument.getContent();
        this.category = reportDocument.getCategory().getDescription();
        this.latitude = reportDocument.getLocation().getLat();
        this.longitude = reportDocument.getLocation().getLon();
        this.createdAt = reportDocument.getCreatedAt().toString();
    }
}