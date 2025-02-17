package EmergencySeagull.report.controller;

import static EmergencySeagull.common.exception.ExceptionCode.INVALID_MAIN_CATEGORY;
import static EmergencySeagull.common.exception.ExceptionCode.INVALID_SUB_CATEGORY;

import EmergencySeagull.common.exception.CustomException;
import EmergencySeagull.report.dto.CategoryUpdateRequest;
import EmergencySeagull.report.dto.ChargeRequest;
import EmergencySeagull.report.dto.ReportRequest;
import EmergencySeagull.report.dto.ReportResponse;
import EmergencySeagull.report.enums.EmergencyCategory;
import EmergencySeagull.report.enums.FireCategory;
import EmergencySeagull.report.enums.MedicalCategory;
import EmergencySeagull.report.enums.RescueCategory;
import EmergencySeagull.report.enums.SafetyCategory;
import EmergencySeagull.report.service.ReportService;
import jakarta.validation.Valid;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponse> createReport(
        @Valid @RequestBody ReportRequest request) {
        ReportResponse response = reportService.classifyAndSaveReport(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getReport(@PathVariable Long reportId) {
        ReportResponse response = reportService.getReport(reportId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<ReportResponse>> getReportsByCategory(
        @PathVariable EmergencyCategory category,
        @PageableDefault Pageable pageable) {
        Sort sort = Sort.by(
            Sort.Order.desc("duplicateCount"),
            Sort.Order.asc("createdAt")
        );

        Pageable sortedPageable = PageRequest.of(
            pageable.getPageNumber(),
            pageable.getPageSize(),
            sort
        );

        Page<ReportResponse> responses = reportService.getReportsByCategory(category,
            sortedPageable);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{reportId}/category")
    public ResponseEntity<ReportResponse> updateReportCategory(
        @PathVariable Long reportId,
        @Valid @RequestBody CategoryUpdateRequest request) {
        EmergencyCategory mainCategory = EmergencyCategory.fromDescription(
            request.getMainCategory());
        if (mainCategory == null) {
            throw new CustomException(INVALID_MAIN_CATEGORY);
        }

        boolean isValidSubCategory = switch (mainCategory.getDescription()) {
            case "화재" -> Arrays.stream(FireCategory.values())
                .anyMatch(category -> category.getDescription().equals(request.getSubCategory()));
            case "구조" -> Arrays.stream(RescueCategory.values())
                .anyMatch(category -> category.getDescription().equals(request.getSubCategory()));
            case "구급" -> Arrays.stream(MedicalCategory.values())
                .anyMatch(category -> category.getDescription().equals(request.getSubCategory()));
            case "생활안전" -> Arrays.stream(SafetyCategory.values())
                .anyMatch(category -> category.getDescription().equals(request.getSubCategory()));
            default -> false;
        };

        if (!isValidSubCategory) {
            throw new CustomException(INVALID_SUB_CATEGORY);
        }

        ReportResponse response = reportService.updateReportCategory(reportId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{reportId}/charge")
    public ResponseEntity<ReportResponse> updateReportCharge(@PathVariable Long reportId,
        @Valid @RequestBody ChargeRequest chargeRequest) {
        ReportResponse response = reportService.updateCharge(reportId, chargeRequest);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> removeReport(@PathVariable Long reportId) {
        reportService.removeReport(reportId);
        return ResponseEntity.noContent().build();
    }
}