package com.live.chat_service.repository;

import com.live.chat_service.model.GroupMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupMessageRepository extends JpaRepository<GroupMessage, Long> {

    @Query("SELECT gm FROM GroupMessage gm " +
            "WHERE gm.groupChatMessage.id = :groupId ")
    List<GroupMessage> findByGroupChatMessage(@Param("groupId") Long groupId);

}
