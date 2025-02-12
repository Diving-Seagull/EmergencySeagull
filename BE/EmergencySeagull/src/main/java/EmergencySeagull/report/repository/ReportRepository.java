package EmergencySeagull.report.repository;

import EmergencySeagull.report.entity.Report;
import EmergencySeagull.report.enums.EmergencyCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByCategory(EmergencyCategory category);
}