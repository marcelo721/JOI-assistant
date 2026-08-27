package com.marcelo271.neva_back_end.JOI.Controller.dto;

import com.marcelo271.neva_back_end.JOI.entities.JOI;
import com.marcelo271.neva_back_end.JOI.personality.PersonalityType;

public record JOICreateDto(
        String name,
        String language,
        PersonalityType personality
) {


    public static JOI ToEntity(JOICreateDto dto){
        JOI joi = new JOI();
        joi.setName(dto.name);
        joi.setPersonality(dto.personality);
        joi.setLanguage(dto.language);

        return joi;
    }
}
