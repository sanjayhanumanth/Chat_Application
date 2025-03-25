package com.live.chat_service.repository;

import com.live.chat_service.model.ChatCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatCallRepository extends JpaRepository<ChatCall, Long> {
}
