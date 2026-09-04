package com.marcelo271.neva_back_end.chat.service;

import com.marcelo271.neva_back_end.JOI.entities.JOI;
import com.marcelo271.neva_back_end.JOI.personality.PersonalityCatalog;
import com.marcelo271.neva_back_end.JOI.personality.PersonalityDefinition;
import com.marcelo271.neva_back_end.JOI.personality.PersonalityType;
import com.marcelo271.neva_back_end.JOI.repository.JOIRepository;
import com.marcelo271.neva_back_end.ai.Dto.GroqMessage;
import com.marcelo271.neva_back_end.ai.service.GroqService;
import com.marcelo271.neva_back_end.chat.dto.ChatRequest;
import com.marcelo271.neva_back_end.chat.dto.ChatResponse;
import com.marcelo271.neva_back_end.chat.utils.ChatUtils;
import com.marcelo271.neva_back_end.chat.conversation.entity.Conversation;
import com.marcelo271.neva_back_end.chat.conversation.entity.Message;
import com.marcelo271.neva_back_end.chat.conversation.repository.ConversationRepository;
import com.marcelo271.neva_back_end.chat.conversation.repository.MessageRepository;
import com.marcelo271.neva_back_end.user.enums.Role;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final JOIRepository deviceRepository;
    private final GroqService groqService;
    private final PersonalityCatalog personalityCatalog;

    public UserChatService(
            ConversationRepository conversationRepository,
            MessageRepository messageRepository,
            JOIRepository deviceRepository,
            GroqService groqService,
            PersonalityCatalog personalityCatalog
    ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.deviceRepository = deviceRepository;
        this.groqService = groqService;
        this.personalityCatalog = personalityCatalog;
    }

    public ChatResponse chat(ChatRequest request) {

        JOI device = deviceRepository
                .findById(request.JOIId())
                .orElseThrow(() ->
                        new RuntimeException("Neva não encontrada")
                );
        Conversation conversation = getOrCreateConversation(device);

        if ("obrigado joi".equalsIgnoreCase(request.message().trim())) {

            conversation.setActive(false);
            conversationRepository.save(conversation);
            return new ChatResponse("Até mais!");
        }
        saveMessage(
                conversation,
                Role.USER,
                request.message()
        );
        List<Message> history =
                messageRepository
                        .findByConversationOrderByCreatedAtAsc(
                                conversation
                        );
        List<GroqMessage> groqMessages =
                buildGroqMessages(
                        device,
                        history
                );
        String response = groqService.chat(groqMessages);

        saveMessage(
                conversation,
                Role.ASSISTANT,
                response
        );

        return new ChatResponse(response);
    }

    private Conversation getOrCreateConversation(JOI device) {
        return conversationRepository
                .findTopByDeviceAndActiveTrueOrderByCreatedAtDesc(device)
                .orElseGet(() -> {

                    Conversation conversation =
                            new Conversation();

                    conversation.setDevice(device);
                    conversation.setCreatedAt(
                            LocalDateTime.now()
                    );
                    conversation.setActive(true);

                    return conversationRepository.save(
                            conversation
                    );
                });
    }

    private void saveMessage(
            Conversation conversation,
            Role role,
            String content
    ) {

        Message message = new Message();

        message.setConversation(conversation);
        message.setRole(role);
        message.setContent(content);
        message.setCreatedAt(
                LocalDateTime.now()
        );
        messageRepository.save(message);
    }

    private List<GroqMessage> buildGroqMessages(
            JOI device,
            List<Message> history
    ) {

        PersonalityType personalityType =
                device.getPersonality();

        PersonalityDefinition personality =
                personalityCatalog.get(
                        personalityType
                );

        List<GroqMessage> groqMessages =
                new ArrayList<>();
        groqMessages.add(
                new GroqMessage(
                        "system",
                        ChatUtils.buildSystemPrompt(personality)
                )
        );
        for (Message message : history) {

            String role = switch (message.getRole()) {

                case USER -> "user";

                case ASSISTANT -> "assistant";

                default -> "user";
            };
            groqMessages.add(
                    new GroqMessage(
                            role,
                            message.getContent()
                    )
            );
        }
        return groqMessages;
    }
}