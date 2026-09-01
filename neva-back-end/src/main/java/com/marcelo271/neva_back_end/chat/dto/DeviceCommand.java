package com.marcelo271.neva_back_end.chat.dto;

public record DeviceCommand(
        String device,
        String action,
        String value
) {
}
