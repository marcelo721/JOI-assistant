package com.marcelo271.neva_back_end.conversation.entity;

import com.marcelo271.neva_back_end.JOI.entities.JOI;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "conversation")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "device_id", nullable = false)
    private JOI device;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Conversation(Long id, JOI neva, LocalDateTime createdAt) {
        this.id = id;
        this.device = neva;
        this.createdAt = createdAt;
    }

    public Conversation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JOI getDevice() {
        return device;
    }

    public void setDevice(JOI neva) {
        this.device = neva;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
