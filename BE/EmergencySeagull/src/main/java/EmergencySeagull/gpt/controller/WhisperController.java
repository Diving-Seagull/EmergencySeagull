package EmergencySeagull.gpt.controller;

import static EmergencySeagull.common.exception.ExceptionCode.JSON_PARSE_ERROR;

import EmergencySeagull.common.exception.CustomException;
import EmergencySeagull.gpt.dto.TranscriptionRequest;
import EmergencySeagull.gpt.service.WhisperService;
import EmergencySeagull.report.dto.ReportResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/whisper")
@RequiredArgsConstructor
public class WhisperController {

    private final WhisperService whisperService;

    @PostMapping("/transcribe")
    public ResponseEntity<ReportResponse> transcribeAudio(
        @RequestPart("file") MultipartFile file, @RequestPart("data") String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        TranscriptionRequest request;

        try {
            request = objectMapper.readValue(data, TranscriptionRequest.class);
        } catch (Exception e) {
            throw new CustomException(JSON_PARSE_ERROR);
        }

        ReportResponse response = whisperService.transcribeAudio(file, request);
        return ResponseEntity.ok(response);
    }
}