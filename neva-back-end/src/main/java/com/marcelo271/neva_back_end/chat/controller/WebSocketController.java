package com.marcelo271.neva_back_end.chat.controller;

import com.marcelo271.neva_back_end.chat.dto.ChatRequest;
import com.marcelo271.neva_back_end.chat.dto.ChatResponse;
import com.marcelo271.neva_back_end.chat.service.UserChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    private final UserChatService chatService;

    public WebSocketController(UserChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/JOI")
    @SendTo("/topic/JOI")
    public ChatResponse receiveMessage(ChatRequest chatRequest){
        return chatService.chat(chatRequest);
    }
}
