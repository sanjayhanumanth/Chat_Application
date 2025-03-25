package com.live.chat_service.repository;

import com.live.chat_service.model.GroupChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupChatMessageRepository extends JpaRepository<GroupChatMessage, Long> {

    @Query("SELECT gm FROM GroupChatMessage gm " +
            "WHERE gm.groupChat.id = :groupId " +
            "ORDER BY gm.timestamp ASC")
    List<GroupChatMessage> findByGroupChatMessage(Long groupId);
}
