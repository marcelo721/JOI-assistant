package com.marcelo271.neva_back_end.conversation.repository;

import com.marcelo271.neva_back_end.JOI.entities.JOI;
import com.marcelo271.neva_back_end.conversation.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findTopByDeviceOrderByCreatedAtDesc(JOI device);
}
