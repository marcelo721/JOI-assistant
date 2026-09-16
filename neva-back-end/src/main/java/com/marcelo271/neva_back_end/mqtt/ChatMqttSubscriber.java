package com.marcelo271.neva_back_end.mqtt;

import com.marcelo271.neva_back_end.chat.dto.ChatRequest;
import com.marcelo271.neva_back_end.chat.service.ChatService;
import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
public class ChatMqttSubscriber {

    private final ChatService chatService;
    private final ObjectMapper objectMapper;
    private final MqttClient mqttClient;

    public ChatMqttSubscriber(ChatService chatService, ObjectMapper objectMapper, MqttClient mqttClient) {
        this.chatService = chatService;
        this.objectMapper = objectMapper;
        this.mqttClient = mqttClient;
    }

    @Value("${neva.mqtt.chat-topic:neva/+/chat/message}")
    private String chatTopic;

    @PostConstruct
    public void subscribe() throws MqttException {

        mqttClient.subscribe(chatTopic, (topic, message) -> {

            try {

                String payload = new String(
                        message.getPayload()
                );

                System.out.println(
                        "Mensagem MQTT recebida: " + payload
                );

                ChatRequest request =
                        objectMapper.readValue(
                                payload,
                                ChatRequest.class
                        );

                chatService.process(request);

            } catch (Exception e) {

                System.err.println(
                        "Erro ao processar mensagem MQTT: "
                                + e.getMessage()
                );
            }
        });

        System.out.println(
                "Inscrito no tópico: " + chatTopic
        );
    }
}