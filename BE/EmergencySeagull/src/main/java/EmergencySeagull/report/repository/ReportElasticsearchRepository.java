package EmergencySeagull.report.repository;

import EmergencySeagull.report.entity.ReportDocument;
import EmergencySeagull.report.enums.EmergencyCategory;
import java.util.List;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ReportElasticsearchRepository extends
    ElasticsearchRepository<ReportDocument, String> {

    List<ReportDocument> findByCategory(EmergencyCategory category);

    @Query("{\"bool\": {\"must\": [{\"match\": {\"category\": \"?0\"}}, {\"geo_distance\": {\"distance\": \"50m\", \"location\": {\"lat\": ?1, \"lon\": ?2}}}]}}")
    List<ReportDocument> findByCategoryAndNearLocation(EmergencyCategory category, double latitude,
        double longitude);
}