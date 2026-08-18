package com.marcelo271.neva_back_end.domain.model.entities;

import java.util.UUID;

public class Neva {

    private UUID id;
    private String name;
    private String language;
    private String personality;


    public Neva() {
    }

    public Neva(UUID id, String name, String language, String personality) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.personality = personality;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }
}
