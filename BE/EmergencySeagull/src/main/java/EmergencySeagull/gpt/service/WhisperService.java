package EmergencySeagull.gpt.service;

import EmergencySeagull.common.exception.CustomException;
import EmergencySeagull.common.exception.ExceptionCode;
import EmergencySeagull.gpt.dto.TranscriptionRequest;
import EmergencySeagull.report.dto.ReportRequest;
import EmergencySeagull.report.dto.ReportResponse;
import EmergencySeagull.report.service.ReportService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhisperService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final ReportService reportService;

    public ReportResponse transcribeAudio(MultipartFile audioFile, TranscriptionRequest request) {
        File file;
        try {
            file = convertMultipartFileToFile(audioFile);
        } catch (IOException e) {
            throw new CustomException(ExceptionCode.GPT_CLASSIFICATION_ERROR);
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(apiUrl);
            httpPost.setHeader("Authorization", "Bearer " + apiKey);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.addPart("file", new FileBody(file));
            entityBuilder.addTextBody("model", "whisper-1");

            httpPost.setEntity(entityBuilder.build());

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonResponse = objectMapper.readTree(response.getEntity().getContent());

                log.info("Whisper API Response: " + jsonResponse.toPrettyString());

                String transcription = jsonResponse.has("text") ? jsonResponse.get("text").asText()
                    : "Transcription failed";

                ReportRequest reportRequest = new ReportRequest(transcription,
                    request.getLatitude(), request.getLongitude());

                return reportService.classifyAndSaveReport(reportRequest);
            } catch (IOException e) {
                throw new CustomException(ExceptionCode.GPT_CLASSIFICATION_ERROR);
            }
        } catch (IOException e) {
            throw new CustomException(ExceptionCode.GPT_CLASSIFICATION_ERROR);
        } finally {
            file.delete();
        }
    }

    private File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File convFile = File.createTempFile("upload_", ".mp3");
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipartFile.getBytes());
        }
        return convFile;
    }
}