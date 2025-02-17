package EmergencySeagull.report.service;

import static EmergencySeagull.common.exception.ExceptionCode.REPORT_NOT_FOUND;

import EmergencySeagull.common.exception.CustomException;
import EmergencySeagull.gpt.service.ClassificationService;
import EmergencySeagull.report.dto.ReportRequest;
import EmergencySeagull.report.dto.ReportResponse;
import EmergencySeagull.report.entity.Report;
import EmergencySeagull.report.entity.ReportDocument;
import EmergencySeagull.report.enums.EmergencyCategory;
import EmergencySeagull.report.repository.ReportElasticsearchRepository;
import EmergencySeagull.report.repository.ReportRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {

    private final ClassificationService classificationService;
    private final ReportRepository reportRepository;
    private final ReportElasticsearchRepository reportElasticsearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    @Transactional
    public ReportResponse classifyAndSaveReport(ReportRequest request) {
        String categoryDescription = classificationService.classifyText(request.getContent());
        EmergencyCategory category = EmergencyCategory.fromDescription(categoryDescription);

        // 중복 신고 검사를 위한 쿼리 생성
        Criteria criteria = new Criteria("category").is(category.name())
            .and(new Criteria("createdAt").greaterThanEqual(LocalDateTime.now().minusMinutes(30)))
            .and(new Criteria("location").within(
                new GeoPoint(request.getLatitude(), request.getLongitude()),
                "50m"
            ));

        CriteriaQuery searchQuery = new CriteriaQuery(criteria);

        SearchHits<ReportDocument> searchHits = elasticsearchOperations.search(
            searchQuery,
            ReportDocument.class
        );

        if (!searchHits.isEmpty()) {
            ReportDocument latestDuplicate = searchHits.getSearchHit(0).getContent();
            Report existingReport = reportRepository.findById(Long.valueOf(latestDuplicate.getId()))
                .orElseThrow(() -> new CustomException(REPORT_NOT_FOUND));

            long newCount = existingReport.getDuplicateCount() + 1;

            if (newCount > 5) {
                // 자동 완료 처리 로직
                return new ReportResponse(existingReport);
            }

            existingReport.incrementDuplicateCount();
            Report updatedReport = reportRepository.save(existingReport);
            return new ReportResponse(updatedReport);
        }

        Report report = new Report(
            request.getContent(),
            category,
            request.getLatitude(),
            request.getLongitude()
        );

        Report savedReport = reportRepository.save(report);
        ReportDocument reportDocument = ReportDocument.from(savedReport);
        reportElasticsearchRepository.save(reportDocument);

        return new ReportResponse(savedReport);
    }

    @Transactional(readOnly = true)
    public Page<ReportResponse> getAllReports(Pageable pageable) {
        return reportRepository.findAll(pageable).map(ReportResponse::new);
    }

    @Transactional(readOnly = true)
    public ReportResponse getReport(Long id) {
        Report report = reportRepository.findById(id)
            .orElseThrow(() -> new CustomException(REPORT_NOT_FOUND));
        return new ReportResponse(report);
    }

    @Transactional(readOnly = true)
    public List<ReportResponse> getReportsByCategory(EmergencyCategory category) {
        return reportRepository.findByCategory(category).stream()
            .map(ReportResponse::new)
            .toList();
    }

    @Transactional
    public void removeReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new CustomException(REPORT_NOT_FOUND));

        reportRepository.delete(report);
    }
}
