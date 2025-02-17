package EmergencySeagull.report.entity;

import EmergencySeagull.report.enums.EmergencyCategory;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
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
    @Setter
    private String id;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private EmergencyCategory category;

    @Field(type = FieldType.Keyword)
    private String subCategory;

    @GeoPointField
    private GeoPoint location;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
    private LocalDateTime createdAt;

    public ReportDocument(String id, String content, EmergencyCategory category,
        String subCategory, Double latitude, Double longitude,
        LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.category = category;
        this.subCategory = subCategory;
        this.location = new GeoPoint(latitude, longitude);
        this.createdAt = createdAt;
    }

    public static ReportDocument from(Report report) {
        return new ReportDocument(
            report.getId().toString(),
            report.getContent(),
            report.getCategory(),
            report.getSubCategory(),
            report.getLatitude(),
            report.getLongitude(),
            report.getCreatedAt()
        );
    }
}
