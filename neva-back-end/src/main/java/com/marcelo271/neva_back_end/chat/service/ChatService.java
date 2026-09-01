package com.marcelo271.neva_back_end.chat.service;


import com.marcelo271.neva_back_end.chat.dto.ChatRequest;
import com.marcelo271.neva_back_end.chat.enums.Intent;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final IntentService intentService;
    private final UserChatService userChatService;
    private final DeviceCommandService deviceCommandService;

    public ChatService(
            IntentService intentService,
            UserChatService userChatService,
            DeviceCommandService deviceCommandService
    ) {
        this.intentService = intentService;
        this.userChatService = userChatService;
        this.deviceCommandService = deviceCommandService;
    }

    public Object process(ChatRequest request) {

        Intent intent =
                intentService.classify(request.message());

        if (intent == Intent.COMMAND) {

            return deviceCommandService.executeCommand(
                    request.JOIId(),
                    request.message()
            );
        }
        return userChatService.chat(request);
    }
}
