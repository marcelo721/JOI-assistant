package com.marcelo271.neva_back_end.mqtt;

import com.marcelo271.neva_back_end.mqtt.publisher.MqttPublisher;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Service;

@Service
public class TtsMqttService {

    private static final int MQTT_CHUNK_SIZE = 1024;

    private final MqttPublisher mqttPublisher;

    public TtsMqttService(
            MqttPublisher mqttPublisher
    ) {
        this.mqttPublisher = mqttPublisher;
    }

    public void startAudio(Long deviceId)
            throws MqttException {

        String topic =
                "joi/" + deviceId + "/audio/tts/start";

        mqttPublisher.publish(
                topic,
                "START",
                1
        );

        System.out.println(
                "TTS START -> " + topic
        );
    }

    public void sendChunk(
            Long deviceId,
            byte[] audioChunk
    ) throws MqttException {

        if (
                audioChunk == null ||
                        audioChunk.length == 0
        ) {
            return;
        }

        String topic =
                "joi/" + deviceId + "/audio/tts/data";

        for (
                int offset = 0;
                offset < audioChunk.length;
                offset += MQTT_CHUNK_SIZE
        ) {

            int remaining =
                    audioChunk.length - offset;

            int size =
                    Math.min(
                            MQTT_CHUNK_SIZE,
                            remaining
                    );

            byte[] chunk =
                    new byte[size];

            System.arraycopy(
                    audioChunk,
                    offset,
                    chunk,
                    0,
                    size
            );

            mqttPublisher.publish(
                    topic,
                    chunk,
                    1
            );

            System.out.println(
                    "MQTT chunk enviado: "
                            + size
                            + " bytes"
            );
        }
    }

    public void endAudio(
            Long deviceId
    ) throws MqttException {

        String topic =
                "joi/" + deviceId + "/audio/tts/end";

        mqttPublisher.publish(
                topic,
                "END",
                1
        );

        System.out.println(
                "TTS END -> " + topic
        );
    }
}