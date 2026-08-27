package com.marcelo271.neva_back_end.JOI.personality;

public record PersonalityDefinition (
    PersonalityType type,
    String name,
    String description,
    String behavior,
    String communicationStyle
    )
{}
