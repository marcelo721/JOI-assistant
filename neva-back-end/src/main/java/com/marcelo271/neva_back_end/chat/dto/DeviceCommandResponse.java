package com.marcelo271.neva_back_end.chat.dto;

public record DeviceCommandResponse(
        boolean success,
        DeviceCommand command
) {
}
