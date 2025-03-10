package com.live.chat_service.repository;

import com.live.chat_service.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findBySenderIdAndReceiverIdOrderByTimestampAsc(Long senderId, Long receiverId);

    @Query("SELECT m FROM ChatMessage m " +
            "WHERE ((m.sender.id = :senderId AND m.receiver.id = :receiverId)" +
            " OR" +
            " (m.sender.id = :receiverId AND m.receiver.id = :senderId)) ORDER BY m.timestamp ASC")
    List<ChatMessage> findBySenderReceiverId(Long senderId, Long receiverId);

    @Query("SELECT m FROM ChatMessage m " +
            "WHERE ((m.sender.id = :senderId AND m.receiver.id = :receiverId)" +
           "AND m.readFlag = false ) ORDER BY m." +
            "timestamp ASC")
    List<ChatMessage> findByNonReadMessage(Long senderId, Long receiverId);

    Long countBySenderIdAndReceiverIdAndReadFlagFalse(Long senderId, Long receiverId);


    @Query(value = "SELECT c from ChatMessage c where c.receiver.id=:receiverId and c.sender.id=:senderId " +
            "order by id desc limit 1")
    Optional<ChatMessage> findLastMessage(Long senderId, Long receiverId);
}
