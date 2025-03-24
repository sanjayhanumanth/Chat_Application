package com.live.chat_service.repository;

import com.live.chat_service.model.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Long> {

    @Query("SELECT gm FROM GroupMessage gm " +
            "WHERE gm.groupChatMessage.groupChat.id = :groupId " +
            "ORDER BY gm.groupChatMessage.timestamp ASC")
    List<GroupMessage> findByGroupChatMessage(@Param("groupId") Long groupId);

}
