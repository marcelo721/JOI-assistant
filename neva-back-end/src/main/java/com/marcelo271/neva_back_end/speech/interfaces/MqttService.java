package com.marcelo271.neva_back_end.speech.interfaces;

public interface MqttService {

    void sendAudio(String joiId, byte[] audioChunk);
    void sendMessage(String topic, byte[] payload);
}
