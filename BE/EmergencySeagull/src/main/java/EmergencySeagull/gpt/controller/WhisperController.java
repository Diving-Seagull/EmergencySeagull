package EmergencySeagull.gpt.controller;

import EmergencySeagull.gpt.dto.TranscriptionResponse;
import EmergencySeagull.gpt.service.WhisperService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/whisper")
@RequiredArgsConstructor
public class WhisperController {

    private final WhisperService whisperService;

    @PostMapping("/transcribe")
    public ResponseEntity<TranscriptionResponse> transcribeAudio(@RequestParam("file") MultipartFile file) {
        try {
            TranscriptionResponse response = whisperService.transcribeAudio(file);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}