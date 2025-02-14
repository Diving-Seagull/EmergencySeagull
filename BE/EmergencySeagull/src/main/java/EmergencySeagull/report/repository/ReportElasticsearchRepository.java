package EmergencySeagull.report.repository;

import EmergencySeagull.report.entity.ReportDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ReportElasticsearchRepository extends
    ElasticsearchRepository<ReportDocument, String> {

}