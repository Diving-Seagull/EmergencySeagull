package EmergencySeagull.report.service;

import static EmergencySeagull.common.exception.ExceptionCode.REPORT_NOT_FOUND;

import EmergencySeagull.common.exception.CustomException;
import EmergencySeagull.common.utils.AddressParser;
import EmergencySeagull.common.utils.GoogleGeocodingUtils;
import EmergencySeagull.gpt.dto.ClassificationResult;
import EmergencySeagull.gpt.service.ClassificationService;
import EmergencySeagull.report.dto.CategoryUpdateRequest;
import EmergencySeagull.report.dto.ChargeRequest;
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
        ClassificationResult classificationResult = classificationService.classifyText(
            request.getContent());
        EmergencyCategory category = EmergencyCategory.fromDescription(
            classificationResult.getMainCategory());
        String subCategory = classificationResult.getSubCategory();

        assert category != null;
        Report duplicateReport = findDuplicateReport(category, subCategory, request.getLatitude(),
            request.getLongitude());

        if (duplicateReport != null) {
            long newCount = duplicateReport.getDuplicateCount() + 1;
            if (newCount > 5) {
                return new ReportResponse(duplicateReport);
            }

            duplicateReport.incrementDuplicateCount();
            Report updatedReport = reportRepository.save(duplicateReport);
            return new ReportResponse(updatedReport);
        }

        String address = GoogleGeocodingUtils.getAddressFromCoordinates(request.getLatitude(),
            request.getLongitude());

        String inCharge = AddressParser.parseCity(address);

        Report report = new Report(
            request.getContent(),
            category,
            subCategory,
            request.getLatitude(),
            request.getLongitude(),
            address,
            inCharge
        );

        Report savedReport = reportRepository.save(report);
        ReportDocument reportDocument = ReportDocument.from(savedReport);
        reportElasticsearchRepository.save(reportDocument);

        return new ReportResponse(savedReport);
    }

    @Transactional(readOnly = true)
    public ReportResponse getReport(Long id) {
        Report report = reportRepository.findById(id)
            .orElseThrow(() -> new CustomException(REPORT_NOT_FOUND));
        return new ReportResponse(report);
    }

    @Transactional(readOnly = true)
    public Page<ReportResponse> getReportsByCategory(EmergencyCategory category,
        Pageable pageable) {
        Page<Report> reports = reportRepository.findByCategory(category, pageable);
        return reports.map(ReportResponse::from);
    }

    @Transactional
    public void removeReport(Long reportId) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new CustomException(REPORT_NOT_FOUND));

        reportRepository.delete(report);
    }

    @Transactional
    public ReportResponse updateReportCategory(Long id, CategoryUpdateRequest request) {
        Report report = reportRepository.findById(id)
            .orElseThrow(() -> new CustomException(REPORT_NOT_FOUND));

        EmergencyCategory newCategory = EmergencyCategory.fromDescription(request.getMainCategory());

        assert newCategory != null;
        Report duplicateReport = findDuplicateReport(newCategory, request.getSubCategory(),
            report.getLatitude(), report.getLongitude());

        if (duplicateReport != null && !duplicateReport.getId().equals(report.getId())) {
            duplicateReport.incrementDuplicateCount(report.getDuplicateCount());
            reportRepository.delete(report); // 기존 신고 삭제
            Report updatedReport = reportRepository.save(duplicateReport);
            return new ReportResponse(updatedReport);
        }

        report.updateCategory(newCategory, request.getSubCategory());
        Report updatedReport = reportRepository.save(report);
        ReportDocument reportDocument = ReportDocument.from(updatedReport);
        reportElasticsearchRepository.save(reportDocument);

        return new ReportResponse(updatedReport);
    }

    private Report findDuplicateReport(EmergencyCategory category, String subCategory,
        Double latitude, Double longitude) {
        Criteria criteria = new Criteria("category").is(category.name())
            .and("subCategory").is(subCategory)
            .and(new Criteria("createdAt").greaterThanEqual(LocalDateTime.now().minusMinutes(30)))
            .and(new Criteria("location").within(
                new GeoPoint(latitude, longitude),
                "50m"
            ));

        CriteriaQuery searchQuery = new CriteriaQuery(criteria);

        SearchHits<ReportDocument> searchHits = elasticsearchOperations.search(
            searchQuery,
            ReportDocument.class
        );

        if (!searchHits.isEmpty()) {
            ReportDocument latestDuplicate = searchHits.getSearchHit(0).getContent();
            return reportRepository.findById(Long.valueOf(latestDuplicate.getId()))
                .orElseThrow(() -> new CustomException(REPORT_NOT_FOUND));
        }

        return null;
    }

    public ReportResponse updateCharge(Long reportId, ChargeRequest chargeRequest) {
        Report report = reportRepository.findById(reportId)
            .orElseThrow(() -> new CustomException(REPORT_NOT_FOUND));

        report.updateCharge(chargeRequest.getCharge());
        Report updatedReport = reportRepository.save(report);

        return new ReportResponse(updatedReport);
    }
}
