package com.live.chat_service.repository;

import com.live.chat_service.model.GroupChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupChatUserRepository extends JpaRepository<GroupChatUser,Long> {
}
