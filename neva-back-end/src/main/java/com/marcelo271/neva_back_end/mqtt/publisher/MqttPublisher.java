package com.marcelo271.neva_back_end.mqtt.publisher;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class MqttPublisher {

    private final MqttClient mqttClient;

    public MqttPublisher(MqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    public void publish(
            String topic,
            byte[] payload,
            int qos
    ) throws MqttException {

        MqttMessage message = new MqttMessage(payload);

        message.setQos(qos);
        message.setRetained(false);

        mqttClient.publish(topic, message);
    }

    public void publish(
            String topic,
            String payload,
            int qos
    ) throws MqttException {

        publish(
                topic,
                payload.getBytes(StandardCharsets.UTF_8),
                qos
        );
    }
}