package EmergencySeagull.report.entity;

import EmergencySeagull.report.enums.EmergencyCategory;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Document(indexName = "reports")
@Getter
@NoArgsConstructor
public class ReportDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private EmergencyCategory category;

    @GeoPointField
    private GeoPoint location;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;

    public ReportDocument(String content, EmergencyCategory category, Double latitude,
        Double longitude, LocalDateTime createdAt) {
        this.content = content;
        this.category = category;
        this.location = new GeoPoint(latitude, longitude);
        this.createdAt = createdAt;
    }

    public static ReportDocument from(Report report) {
        return new ReportDocument(
            report.getContent(),
            report.getCategory(),
            report.getLatitude(),
            report.getLongitude(),
            report.getCreatedAt()
        );
    }
}
