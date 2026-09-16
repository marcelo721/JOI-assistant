package com.marcelo271.neva_back_end.speech.azure;

import com.marcelo271.neva_back_end.mqtt.TtsMqttService;
import com.marcelo271.neva_back_end.speech.config.AzureSpeechConfig;
import com.marcelo271.neva_back_end.speech.interfaces.TextToSpeechService;
import com.microsoft.cognitiveservices.speech.*;
import org.springframework.stereotype.Service;


@Service
public class AzureTextToSpeechService implements TextToSpeechService {

    private static final String LANGUAGE = "en-US";
    private static final String VOICE = "en-US-Harper:MAI-Voice-2";

    private  SpeechConfig speechConfig;
    private  SpeechSynthesizer synthesizer;
    private final TtsMqttService ttsMqttService;
    private volatile Long currentDevice;


    public AzureTextToSpeechService(TtsMqttService ttsMqttService, AzureSpeechConfig config) {
        this.ttsMqttService = ttsMqttService;
        speechConfig = SpeechConfig.fromSubscription(config.getKey(), config.getRegion());
        speechConfig.setSpeechSynthesisLanguage(LANGUAGE);
        speechConfig.setSpeechSynthesisVoiceName(VOICE);
        speechConfig.setSpeechSynthesisOutputFormat(SpeechSynthesisOutputFormat.Raw16Khz16BitMonoPcm);

        synthesizer = new SpeechSynthesizer(speechConfig, null);
        synthesizer.Synthesizing.addEventListener
                ((sender, event) -> handleAudioChunk(event));

        synthesizer.SynthesisCanceled.addEventListener(
                (sender, event) -> handleSynthesisCanceled(event)
        );
    }

    public synchronized void synthesizeAndStream(Long deviceId, String text){
        try{
            currentDevice = deviceId;
            System.out.println("Starting device with id: " + deviceId);
            ttsMqttService.startAudio(deviceId);

            SpeechSynthesisResult result = synthesizer.SpeakText(text);
            result.close();

        }catch (Exception e){
            System.err.println("error while Streaming" + e.getMessage());
            if (deviceId != null){
                try{
                    ttsMqttService.endAudio(deviceId);
                }catch(Exception mqttException){
                    System.err.println("Erro ao finalizar MQTT" + mqttException.getMessage());
                }
            }
            currentDevice = null;
            throw new RuntimeException("erro no streaming TTS", e);

        }
    }

    private void handleAudioChunk(SpeechSynthesisEventArgs event){
        Long deviceId = currentDevice;
        if (deviceId == null){
            System.err.println("deviceId is null!");
            return;
        }
        try {
            SpeechSynthesisResult result = event.getResult();
            byte[] audioChunk = result.getAudioData();
            if(audioChunk == null || audioChunk.length == 0)
                return;

            System.out.println("Chunk Azure received:" + audioChunk.length + "bytes");
            ttsMqttService.sendChunk(deviceId, audioChunk);

        }catch (Exception e){
            System.err.println("error while processing TTS chunk" + e.getMessage());

        }
    }

    private void handleSynthesisCompleted(){
        Long deviceId = currentDevice;
        if (deviceId == null)
            return;

        try {
            System.out.println("TTS streaming finalized for device" + deviceId);
            ttsMqttService.endAudio(deviceId);

        }catch (Exception e){
            System.err.println("Error when ending MQTT streaming:" + e.getMessage());
        }finally {
            currentDevice = null;
        }
    }

    @Override
    public byte[] synthesize(String text) {
        return new byte[0];
    }
}