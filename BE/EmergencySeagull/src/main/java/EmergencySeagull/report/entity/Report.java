package EmergencySeagull.report.entity;

import EmergencySeagull.report.enums.EmergencyCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "reports")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EmergencyCategory category;

    @Column(nullable = false, length = 50)
    private String subCategory;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // 중복 판단
    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 1")
    private Long duplicateCount = 1L;  // 초기값 1

    @Version
    private Long version;  // 동시성 제어

    public Report(String content, EmergencyCategory category, String subCategory, Double latitude,
        Double longitude) {
        this.content = content;
        this.category = category;
        this.subCategory = subCategory;
        this.latitude = latitude;
        this.longitude = longitude;
        this.duplicateCount = 1L;
    }

    public void incrementDuplicateCount() {
        this.duplicateCount += 1;
    }

    public void incrementDuplicateCount(Long cnt) {
        this.duplicateCount += cnt;
    }

    public void updateCategory(EmergencyCategory category, String subCategory) {
        this.category = category;
        this.subCategory = subCategory;
    }
}