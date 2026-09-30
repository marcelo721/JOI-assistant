package com.marcelo271.neva_back_end.mqtt.config;

import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {

    @Value("${mqtt.broker}")
    private String broker;

    @Value("${mqtt.client-id}")
    private String clientId;

    private MqttClient client;


    @Bean
    public MqttClient mqttClient() throws MqttException {
        client = new MqttClient(broker, clientId);

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);
        options.setKeepAliveInterval(30);

        client.connect(options);
        System.out.println("=================================");
        System.out.println("MQTT conectado!");

        System.out.println("Broker: " + broker);
        System.out.println("Client ID: " + clientId);
        System.out.println("Connected: " + client.isConnected());
        System.out.println("=================================");
        return client;
    }

    @PreDestroy
    public void shutdown() {

        if (client == null) {
            return;
        }

        try {

            if (client.isConnected()) {
                client.disconnect();
            }

            if (client.isConnected() == false) {
                client.close();
            }

        } catch (MqttException e) {

            System.err.println(
                    "Erro ao fechar MQTT: "
                            + e.getMessage()
            );
        }
    }
}
