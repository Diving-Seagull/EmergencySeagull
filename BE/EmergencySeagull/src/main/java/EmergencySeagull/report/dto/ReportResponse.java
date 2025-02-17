package EmergencySeagull.report.dto;

import EmergencySeagull.report.entity.Report;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ReportResponse {

    private final Long id;
    private final String content;
    private final String category;
    private final String subCategory;
    private final Double latitude;
    private final Double longitude;
    private final String createdAt;
    private final Long duplicateCount;
    private final String address;
    private final String inCharge;

    public ReportResponse(Report report) {
        this.id = report.getId();
        this.content = report.getContent();
        this.category = report.getCategory().getDescription();
        this.subCategory = report.getSubCategory();
        this.latitude = report.getLatitude();
        this.longitude = report.getLongitude();
        this.createdAt = report.getCreatedAt().toString();
        this.duplicateCount = report.getDuplicateCount();
        this.address = report.getAddress();
        this.inCharge = report.getInCharge();
    }

    public static ReportResponse from(Report report) {
        return new ReportResponse(report);
    }
}