package com.live.chat_service.service;
import com.live.chat_service.model.ChatMessage;
import com.live.chat_service.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public interface ChatMessageService {

    ChatMessage saveMessage(ChatMessage chatMessage);
}
