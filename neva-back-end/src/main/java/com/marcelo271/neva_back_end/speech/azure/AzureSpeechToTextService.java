package com.marcelo271.neva_back_end.speech.azure;
import com.marcelo271.neva_back_end.speech.config.AzureSpeechConfig;
import com.marcelo271.neva_back_end.speech.interfaces.SpeechToTextService;
import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import com.microsoft.cognitiveservices.speech.audio.PushAudioInputStream;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class AzureSpeechToTextService implements SpeechToTextService {

    private final AzureSpeechConfig azureSpeechConfig;

    public AzureSpeechToTextService(AzureSpeechConfig azureSpeechConfig) {
        this.azureSpeechConfig = azureSpeechConfig;
    }

    @Override
    public String transcribe(byte[] audio) {

        SpeechConfig speechConfig = SpeechConfig.fromSubscription(
                azureSpeechConfig.getKey(),
                azureSpeechConfig.getRegion()
        );

        speechConfig.setSpeechRecognitionLanguage("pt-BR");

        Path tempFile = null;

        try {
            tempFile = Files.createTempFile("neva-", ".wav");

            Files.write(tempFile, audio);

            try (
                    AudioConfig audioConfig =
                            AudioConfig.fromWavFileInput(tempFile.toString());

                    SpeechRecognizer speechRecognizer =
                            new SpeechRecognizer(speechConfig, audioConfig)
            ) {

                SpeechRecognitionResult result =
                        speechRecognizer.recognizeOnceAsync().get();

                if (result.getReason() == ResultReason.RecognizedSpeech) {
                    return result.getText();
                }

                if (result.getReason() == ResultReason.NoMatch) {
                    throw new RuntimeException(
                            "Azure não conseguiu reconhecer a fala."
                    );
                }

                if (result.getReason() == ResultReason.Canceled) {

                    CancellationDetails cancellation =
                            CancellationDetails.fromResult(result);

                    throw new RuntimeException(
                            "Erro no Azure STT: " +
                                    cancellation.getErrorDetails()
                    );
                }

                throw new RuntimeException(
                        "Resultado inesperado do Azure STT."
                );
            }

        } catch (Exception e) {

            throw new RuntimeException(
                    "Erro ao realizar transcrição com Azure Speech: "
                            + e.getMessage(),
                    e
            );

        } finally {

            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException ignored) {
                }
            }

            speechConfig.close();
        }
    }

}
