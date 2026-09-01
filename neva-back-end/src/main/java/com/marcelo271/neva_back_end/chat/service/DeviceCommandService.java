package com.marcelo271.neva_back_end.chat.service;

import com.marcelo271.neva_back_end.JOI.entities.JOI;
import com.marcelo271.neva_back_end.JOI.repository.JOIRepository;
import com.marcelo271.neva_back_end.ai.Dto.GroqMessage;
import com.marcelo271.neva_back_end.ai.service.GroqService;
import com.marcelo271.neva_back_end.chat.dto.DeviceCommand;
import com.marcelo271.neva_back_end.chat.dto.DeviceCommandResponse;
import com.marcelo271.neva_back_end.chat.utils.DeviceCommandUtils;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceCommandService {

    private final JOIRepository deviceRepository;
    private final GroqService groqService;
    private final ObjectMapper objectMapper;

    public DeviceCommandService( JOIRepository deviceRepository,
                                 GroqService groqService,
                                 ObjectMapper objectMapper )
    { this.deviceRepository = deviceRepository;
        this.groqService = groqService;
        this.objectMapper = objectMapper;
    }

    public DeviceCommandResponse executeCommand(Long JOIId, String message){
        JOI joi = deviceRepository.findById(JOIId).orElseThrow(
                ()-> new RuntimeException("JOI not found!")
        );

        List<GroqMessage> groqMessages = buildGroqMessages(JOIId, message);
        String response = groqService.chat(groqMessages);

        return parseResponse(response);
    }


    private List<GroqMessage> buildGroqMessages(Long JOIId, String message){
        List<GroqMessage> messages = new ArrayList<>();
        messages.add(new GroqMessage("system", DeviceCommandUtils.buildSystemPrompt()));

        messages.add(new GroqMessage("user", message));
        return messages;
    }

    private DeviceCommandResponse parseResponse(String response) {

        DeviceCommand command = objectMapper.readValue(response, DeviceCommand.class);
        return new DeviceCommandResponse(true, command);
    }
}
