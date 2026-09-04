package com.marcelo271.neva_back_end.speech.interfaces;


import java.util.function.Consumer;

public interface TextToSpeechService {
    byte[] synthesize(String text);

}
