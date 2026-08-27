package com.marcelo271.neva_back_end.ai.Dto;

import java.util.List;

public record GroqChatRequest(
        String model,
        List<GroqMessage> messages
) {
}
