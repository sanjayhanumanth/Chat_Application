package com.live.chat_service.repository;

import com.live.chat_service.model.ChatMessage;
import com.live.chat_service.model.GroupChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessage, Long> {
    @Query(value = "SELECT c from ChatMessage c where c.groupChat.id = :groupId ORDER BY c.timestamp ASC")
    List<GroupChatMessage> findByGroupId(Long groupId);
}
