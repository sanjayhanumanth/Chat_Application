package com.live.chat_service.serviceimpl;

import com.live.chat_service.model.ChatMessage;
import com.live.chat_service.repository.ChatMessageRepository;
import com.live.chat_service.service.ChatMessageService;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public ChatMessage saveMessage(ChatMessage chatMessage) {
        if (chatMessage.getSenderId() == null || chatMessage.getReceiverId() == null || chatMessage.getContent().isEmpty()) {
            throw new IllegalArgumentException("Invalid message data: Sender, Receiver, and Content must be provided.");
        }
        return chatMessageRepository.save(chatMessage);
    }
}
