package com.live.chat_service.repository;

import com.live.chat_service.model.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupChatMessageRepository extends JpaRepository<GroupChat,Long> {
    @Query("SELECT u FROM GroupChat u WHERE u.id=:groupId AND u.isActive=true ")
    Optional<GroupChat> findByIdAndIsActiveTrue(Long groupId);
}
