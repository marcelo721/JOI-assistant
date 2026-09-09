package com.marcelo271.neva_back_end.speech.azure;

import com.marcelo271.neva_back_end.speech.config.AzureSpeechConfig;
import com.marcelo271.neva_back_end.speech.interfaces.TextToSpeechService;
import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;


@Service
public class AzureTextToSpeechService implements TextToSpeechService {

    private static final String LANGUAGE = "en-US";
    private static final String VOICE = "en-US-Harper:MAI-Voice-2";

    private final SpeechConfig speechConfig;
    private final SpeechSynthesizer synthesizer;

    public AzureTextToSpeechService(AzureSpeechConfig config) {

        speechConfig = SpeechConfig.fromSubscription(
                config.getKey(),
                config.getRegion()
        );

        speechConfig.setSpeechSynthesisLanguage(LANGUAGE);
        speechConfig.setSpeechSynthesisVoiceName(VOICE);

        speechConfig.setSpeechSynthesisOutputFormat(
                SpeechSynthesisOutputFormat.Riff16Khz16BitMonoPcm
        );

        synthesizer = new SpeechSynthesizer(
                speechConfig,
                null
        );
    }
    @Override
    public byte[] synthesize(String text) {

        try (
                AudioConfig audioConfig = AudioConfig.fromDefaultSpeakerOutput();
                SpeechSynthesizer synthesizer = new SpeechSynthesizer(
                        speechConfig,
                        audioConfig
                )
        ) {

            SpeechSynthesisResult result = synthesizer.SpeakText(text);

            if (result.getReason() == ResultReason.SynthesizingAudioCompleted) {
                return result.getAudioData();
            }

            throw new RuntimeException(
                    "Falha ao sintetizar áudio: " + result.getReason()
            );
        }
    }

    @PreDestroy
    public void shutdown() {
        synthesizer.close();
        speechConfig.close();
    }
}