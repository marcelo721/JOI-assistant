package com.marcelo271.neva_back_end.chat.controller;


import com.marcelo271.neva_back_end.chat.dto.ChatRequest;
import com.marcelo271.neva_back_end.chat.service.ChatService;
import com.marcelo271.neva_back_end.speech.controller.SpeechController;
import com.marcelo271.neva_back_end.speech.interfaces.SpeechToTextService;
import com.marcelo271.neva_back_end.speech.interfaces.TextToSpeechService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@RequestBody ChatRequest request) {
        try {
            Object result = chatService.process(request);
            if (result instanceof byte[] audio) {
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("audio/wav"))
                        .body(audio);
            }
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao processar mensagem: " + e.getMessage());
        }
    }
}
