package com.marcelo271.neva_back_end.JOI.Controller.dto;

import com.marcelo271.neva_back_end.JOI.entities.JOI;
import com.marcelo271.neva_back_end.JOI.personality.PersonalityType;

public record JOIResponseDto(
        String name,
        String language,
        PersonalityType personality
) {

    public static JOIResponseDto toResponse(JOI joi){
        return new JOIResponseDto(
                joi.getName(),
                joi.getLanguage(),
                joi.getPersonality()
        );
    }
}
