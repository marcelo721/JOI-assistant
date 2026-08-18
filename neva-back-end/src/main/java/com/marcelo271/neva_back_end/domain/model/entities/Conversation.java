package com.marcelo271.neva_back_end.domain.model.entities;

import java.time.LocalDateTime;
import java.util.UUID;

public class Conversation {

    private UUID id;
    private Neva neva;
    private LocalDateTime createdAt;

    public Conversation(UUID id, Neva neva, LocalDateTime createdAt) {
        this.id = id;
        this.neva = neva;
        this.createdAt = createdAt;
    }

    public Conversation() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Neva getNeva() {
        return neva;
    }

    public void setNeva(Neva neva) {
        this.neva = neva;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
