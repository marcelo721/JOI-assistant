package com.marcelo271.neva_back_end.chat.dto;

import java.util.UUID;

public record ChatRequest(
        Long nevaId,
        String message
) {
}
