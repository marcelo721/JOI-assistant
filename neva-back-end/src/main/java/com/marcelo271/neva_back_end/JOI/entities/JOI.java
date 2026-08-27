package com.marcelo271.neva_back_end.JOI.entities;

import com.marcelo271.neva_back_end.JOI.personality.PersonalityType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;


@Entity
@Table(name = "device")
public class JOI {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "language")
    private String language;

    @Column(name = "personality", nullable = false)
    @Enumerated(EnumType.STRING)
    private PersonalityType personality;

    public JOI() {
    }

    public JOI(Long id, String name, String language, PersonalityType personality) {
        this.id = id;
        this.name = name;
        this.language = language;
        this.personality = personality;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public PersonalityType getPersonality() {
        return personality;
    }

    public void setPersonality(PersonalityType personality) {
        this.personality = personality;
    }
}
