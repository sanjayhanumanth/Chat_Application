package com.live.chat_service.repository;

import com.live.chat_service.dto.ChatDTO;
import com.live.chat_service.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySender_IdOrReceiver_IdOrderByTimestampAsc(Long senderId, Long receiverId);

//    @Query("SELECT m FROM ChatMessage m WHERE m.sender.id = :senderId OR m.receiver.id = :receiverId ORDER BY m.timestamp ASC")
//    List<ChatMessage> findBySenderReceiverId(Long senderId, Long receiverId);

}
