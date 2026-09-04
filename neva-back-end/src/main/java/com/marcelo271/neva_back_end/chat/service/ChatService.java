package com.marcelo271.neva_back_end.chat.service;


import com.marcelo271.neva_back_end.chat.dto.ChatRequest;
import com.marcelo271.neva_back_end.chat.dto.ChatResponse;
import com.marcelo271.neva_back_end.chat.enums.Intent;
import com.marcelo271.neva_back_end.speech.interfaces.TextToSpeechService;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final IntentService intentService;
    private final UserChatService userChatService;
    private final DeviceCommandService deviceCommandService;
    private final TextToSpeechService textToSpeechService;

    public ChatService(
            IntentService intentService,
            UserChatService userChatService,
            DeviceCommandService deviceCommandService,
            TextToSpeechService textToSpeechService
    ) {
        this.intentService = intentService;
        this.userChatService = userChatService;
        this.deviceCommandService = deviceCommandService;
        this.textToSpeechService = textToSpeechService;
    }

    public Object process(ChatRequest request) {

        Intent intent = intentService.classify(request.message());

        if (intent == Intent.COMMAND) {
            return deviceCommandService.executeCommand(
                    request.JOIId(),
                    request.message()
            );
        }

        ChatResponse response = userChatService.chat(request);

        return textToSpeechService.synthesize(response.message());

    }
}
