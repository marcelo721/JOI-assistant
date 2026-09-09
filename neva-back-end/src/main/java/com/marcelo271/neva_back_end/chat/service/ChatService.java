package com.marcelo271.neva_back_end.chat.service;


import com.marcelo271.neva_back_end.chat.dto.ChatRequest;
import com.marcelo271.neva_back_end.chat.dto.ChatResponse;
import com.marcelo271.neva_back_end.chat.enums.Intent;
import com.marcelo271.neva_back_end.mqtt.TtsMqttService;
import com.marcelo271.neva_back_end.speech.interfaces.TextToSpeechService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final IntentService intentService;
    private final UserChatService userChatService;
    private final DeviceCommandService deviceCommandService;
    private final TextToSpeechService textToSpeechService;
    public final TtsMqttService ttsMqttService;

    public ChatService(
            IntentService intentService,
            UserChatService userChatService,
            DeviceCommandService deviceCommandService,
            TextToSpeechService textToSpeechService,
            TtsMqttService ttsMqttService
    ) {
        this.intentService = intentService;
        this.userChatService = userChatService;
        this.deviceCommandService = deviceCommandService;
        this.textToSpeechService = textToSpeechService;
        this.ttsMqttService = ttsMqttService;
    }

    public Object process(ChatRequest request) {

        Intent intent = intentService.classify(request.message());

        if (intent == Intent.COMMAND) {
            return deviceCommandService.executeCommand(request.JOIId(), request.message());
        }

        ChatResponse response = userChatService.chat(request);
        byte[] audio = textToSpeechService.synthesize(response.message());

        try {
            ttsMqttService.sendAudio(request.JOIId(), audio);
        } catch (MqttException e) {
            throw new RuntimeException("Erro ao enviar áudio via MQTT", e);
        }
        return response;
    }

    public Object process2(ChatRequest request) {

        Intent intent = intentService.classify(request.message());

        if (intent == Intent.COMMAND) {
            return deviceCommandService.executeCommand(
                    request.JOIId(),
                    request.message()
            );
        }

        ChatResponse response =
                userChatService.chat(request);

        byte[] audio =
                textToSpeechService.synthesize(
                        response.message()
                );

        try {

            ttsMqttService.sendAudio(
                    request.JOIId(),
                    audio
            );

        } catch (MqttException e) {

            throw new RuntimeException(
                    "Erro ao enviar áudio via MQTT",
                    e
            );
        }

        return response;
    }
}
