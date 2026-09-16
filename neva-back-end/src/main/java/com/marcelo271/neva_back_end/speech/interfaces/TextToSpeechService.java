package com.marcelo271.neva_back_end.speech.interfaces;

public interface TextToSpeechService {
    byte[] synthesize(String text);

}
