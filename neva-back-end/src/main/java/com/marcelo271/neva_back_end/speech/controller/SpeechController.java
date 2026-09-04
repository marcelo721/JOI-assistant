package com.marcelo271.neva_back_end.speech.controller;


import com.marcelo271.neva_back_end.speech.dto.TextToSpeechRequest;
import com.marcelo271.neva_back_end.speech.interfaces.SpeechToTextService;
import com.marcelo271.neva_back_end.speech.interfaces.TextToSpeechService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;

@RestController
@RequestMapping("api/v1/speech")
public class SpeechController {
    private final SpeechToTextService speechToTextService;
    private final TextToSpeechService textToSpeechService;

    public SpeechController(
            SpeechToTextService speechToTextService,
            TextToSpeechService textToSpeechService
    ) {
        this.speechToTextService = speechToTextService;
        this.textToSpeechService = textToSpeechService;
    }


    @PostMapping(value = "/stt", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> speechToText(@RequestParam("audio") MultipartFile audio) {
        try {
            byte[] audioBytes = audio.getBytes();
            String transcription = speechToTextService.transcribe(audioBytes);
            return ResponseEntity.ok(transcription);

        } catch (Exception e) {

            return ResponseEntity
                    .internalServerError()
                    .body("Erro ao processar áudio: " + e.getMessage());
        }
    }

}
