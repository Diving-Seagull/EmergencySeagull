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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final ReportElasticsearchRepository reportElasticsearchRepository;
    private final ClassificationService classificationService;

    @Transactional
    public ReportResponse classifyAndSaveReport(ReportRequest request) {
        // 신고 내용 분류
        String categoryDescription = classificationService.classifyText(request.getContent());
        EmergencyCategory category = EmergencyCategory.fromDescription(categoryDescription);

        // 신고 내용 저장
        Report report = new Report(
            request.getContent(),
            category,
            request.getLatitude(),
            request.getLongitude()
        );

        Report savedReport = reportRepository.save(report);

        // 엘라스틱서치에 저장
        reportElasticsearchRepository.save(ReportDocument.from(savedReport));

        return new ReportResponse(savedReport);
    }

    public Page<ReportResponse> getAllReports(Pageable pageable) {
        return reportRepository.findAll(pageable).map(ReportResponse::new);
    }

    public ReportResponse getReport(Long id) {
        Report report = reportRepository.findById(id)
            .orElseThrow(() -> new CustomException(REPORT_NOT_FOUND));
        return new ReportResponse(report);
    }

    public List<ReportResponse> getReportsByCategory(EmergencyCategory category) {
        return reportRepository.findByCategory(category).stream()
            .map(ReportResponse::new)
            .toList();
    }

    // 엘라스틱서치에서 근처 신고 검색
    public List<ReportResponse> findNearbyReports(EmergencyCategory category, Double latitude,
        Double longitude) {
        List<ReportDocument> nearbyReports = reportElasticsearchRepository.findByCategoryAndNearLocation(
            category, latitude, longitude);
        return nearbyReports.stream()
            .map(ReportResponse::new)
            .toList();
    }
}
