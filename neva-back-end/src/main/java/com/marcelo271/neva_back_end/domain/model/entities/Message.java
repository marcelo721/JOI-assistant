package com.marcelo271.neva_back_end.domain.model.entities;

import com.marcelo271.neva_back_end.domain.model.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public class Message {

    private UUID id;
    private Conversation conversation;
    private Role role;
    private String content;
    private LocalDateTime createdAt;
}
