package com.live.chat_service.repository;

import com.live.chat_service.model.GroupChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupChatUserRepository extends JpaRepository<GroupChatUser,Long> {

    @Query("SELECT u FROM GroupChatUser u WHERE u.groupChat.id=:groupId AND u.isActive=true ")
    List<GroupChatUser> findByIsActiveTrue(Long groupId);
}
