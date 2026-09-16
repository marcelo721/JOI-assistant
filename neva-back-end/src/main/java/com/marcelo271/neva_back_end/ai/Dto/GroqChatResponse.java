package com.marcelo271.neva_back_end.ai.Dto;


import java.util.List;

public record GroqChatResponse(
        List<Choice> choices
) {
    public record Choice(
            GroqMessage message
    ) {}
}