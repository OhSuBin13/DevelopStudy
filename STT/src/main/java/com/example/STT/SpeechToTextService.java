package com.example.STT;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SpeechToTextService {
    private final Logger logger = LoggerFactory.getLogger(SpeechToTextService.class);

    public String transcribe(MultipartFile audioFile) throws IOException {
        if (audioFile.isEmpty()) {
            throw new IOException("Required part 'audioFile' is empty");
        }

        //오디오 파일을 byte array로 decode
        byte[] audioBytes = audioFile.getBytes();

        try (SpeechClient speechClient = SpeechClient.create()){
            //오디오 객체 생성
            ByteString audioData = ByteString.copyFrom(audioBytes);
            RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
                    .setContent(audioData).build();

            //설정 객체 생성
            RecognitionConfig recognitionConfig =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.FLAC)
                            .setSampleRateHertz(44100)
                            .setLanguageCode("en-US")
                            .setAudioChannelCount(2)
                            .build();

            //오디오-텍스트 변환 수행
            RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);
            List<SpeechRecognitionResult> results = response.getResultsList();

            if (!results.isEmpty()) {
                //주어진 말 뭉치에 대해 여러 가능한 스크립트 제공. 0번 : 가장 가능성 있는 것 사용
                SpeechRecognitionResult result = results.get(0);
                return result.getAlternatives(0).getTranscript();
            } else {
                logger.error("No transcription found");
                return "";
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
