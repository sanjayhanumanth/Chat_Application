package com.live.chat_service.repository;

import com.live.chat_service.model.User;
import com.live.chat_service.model.UserAccessLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccessLogRepository extends JpaRepository<UserAccessLog, Long> {

    @Query("SELECT u.contactUser FROM UserAccessLog u WHERE u.user.id = :userId ORDER BY u.lastContacted DESC")
    List<User> findFrequentlyContactedUsers(@Param("userId") Long userId, Pageable pageable);


    @Query("SELECT u.contactUser FROM UserAccessLog u WHERE u.user.id = :userId ORDER BY u.lastContacted DESC")
    List<User> findFrequentlyContacted(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT u FROM UserAccessLog u WHERE u.user.id = :userId AND u.contactUser.id = :contactUserId")
    Optional<UserAccessLog> findByUserIdAndContactUserId(@Param("userId") Long userId, @Param("contactUserId") Long contactUserId);
}

