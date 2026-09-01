package com.marcelo271.neva_back_end.chat.conversation.repository;


import com.marcelo271.neva_back_end.chat.conversation.entity.Conversation;
import com.marcelo271.neva_back_end.chat.conversation.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository
        extends JpaRepository<Message, Long> {

    List<Message> findByConversationOrderByCreatedAtAsc(
            Conversation conversation
    );
}