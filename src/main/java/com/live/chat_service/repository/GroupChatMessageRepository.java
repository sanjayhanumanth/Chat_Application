package com.live.chat_service.repository;

import com.live.chat_service.model.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupChatMessageRepository extends JpaRepository<GroupChat,Long> {
}
