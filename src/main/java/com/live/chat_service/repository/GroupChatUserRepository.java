package com.live.chat_service.repository;

import com.live.chat_service.dto.GroupChatProjection;
import com.live.chat_service.model.GroupChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupChatUserRepository extends JpaRepository<GroupChatUser,Long> {

    @Query("SELECT u FROM GroupChatUser u WHERE u.groupChat.id=:groupId AND u.isActive=true ")
    List<GroupChatUser> findByIsActiveTrue(Long groupId);

    @Query(value = "SELECT cg.id as groupId, cg.group_name as groupName " +
            "FROM chat_group_chat_user AS cgcu " +
            "JOIN chat_group AS cg ON cgcu.group_chat_id_fk = cg.id " +
            "JOIN user AS u ON cgcu.user_id_fk = u.id " +
            "WHERE u.id = :userId AND u.is_active = 1 AND cg.is_active = 1",
            nativeQuery = true)
    List<GroupChatProjection> findUserGroup(Long userId);

    @Query(value = "SELECT cg.id AS groupId, cg.group_name AS groupName " +
            "FROM chat_group_chat_user AS cgcu " +
            "JOIN chat_group AS cg ON cgcu.group_chat_id_fk = cg.id " +
            "JOIN user AS u ON cgcu.user_id_fk = u.id " +
            "WHERE u.id = :userId AND u.is_active = 1 AND cg.is_active = 1 " +
            "AND cg.group_name LIKE %:search% ",
            nativeQuery = true)
    List<GroupChatProjection> findByGroupName(@Param("search") String search, @Param("userId") Long userId);

}
