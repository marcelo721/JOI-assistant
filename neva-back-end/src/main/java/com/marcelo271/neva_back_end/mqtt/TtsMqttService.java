package com.marcelo271.neva_back_end.mqtt;

import com.marcelo271.neva_back_end.mqtt.publisher.MqttPublisher;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class TtsMqttService {
    private final MqttPublisher mqttPublisher;

    public TtsMqttService(MqttPublisher mqttPublisher) {
        this.mqttPublisher = mqttPublisher;
    }

    public void sendAudio(
            Long deviceId,
            byte[] audio
    ) throws MqttException {

        String baseTopic =
                "neva/" + deviceId + "/audio/tts";

        mqttPublisher.publish(
                baseTopic + "/start",
                "START",
                1
        );

        int chunkSize = 1024;

        for (int i = 0; i < audio.length; i += chunkSize) {

            int end = Math.min(
                    i + chunkSize,
                    audio.length
            );

            byte[] chunk = Arrays.copyOfRange(
                    audio,
                    i,
                    end
            );

            mqttPublisher.publish(
                    baseTopic + "/data",
                    chunk,
                    1
            );
        }
        mqttPublisher.publish(
                baseTopic + "/end",
                "END",
                1
        );
    }
}
