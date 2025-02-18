package EmergencySeagull.report.repository;

import EmergencySeagull.report.entity.Report;
import EmergencySeagull.report.enums.EmergencyCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findByCategoryAndInChargeAndIsAcceptedFalse(EmergencyCategory category, String inCharge, Pageable pageable);

    Page<Report> findByCategoryAndIsAcceptedTrue(EmergencyCategory category, Pageable pageable);
}