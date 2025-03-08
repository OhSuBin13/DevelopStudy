package com.example.STT;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/audio")
@RequiredArgsConstructor
public class AudioController {
    private final String uploadDir = "C:/developPractice/uploads/";
    private final SpeechToTextService speechToTextService;

    @PostMapping("/transcribe")
    public ResponseEntity<String> transcribe(@RequestParam("audioFile") MultipartFile audioFile) throws IOException {
        String transcribe = speechToTextService.transcribe(audioFile);
        return ResponseEntity.ok(transcribe);
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();

        try {
            String filePath = uploadDir + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            response.put("message", "파일 업로드 성공");
            response.put("filePath", filePath);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "파일 업로드 실패" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
