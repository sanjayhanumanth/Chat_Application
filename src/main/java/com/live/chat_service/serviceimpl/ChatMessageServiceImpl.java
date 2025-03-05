package com.live.chat_service.serviceimpl;

import com.live.chat_service.dto.ChatDTO;
import com.live.chat_service.model.ChatMessage;
import com.live.chat_service.repository.ChatMessageRepository;
import com.live.chat_service.response.SuccessResponse;
import com.live.chat_service.service.ChatMessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public ChatMessage saveMessage(ChatMessage chatMessage) {
        if (chatMessage.getSender() == null || chatMessage.getReceiver() == null || chatMessage.getContent().isEmpty()) {
            throw new IllegalArgumentException("Invalid message data: Sender, Receiver, and Content must be provided.");
        }
        chatMessage.setTimestamp(LocalDateTime.now());
        return chatMessageRepository.save(chatMessage);
    }

    @Override
    public SuccessResponse<List<ChatDTO>> getChatMessages(Long senderId, Long receiverId) {
        SuccessResponse<List<ChatDTO>> successResponse = new SuccessResponse<>();
        List<ChatMessage> chatMessages = chatMessageRepository.findBySender_IdOrReceiver_IdOrderByTimestampAsc(senderId, receiverId);
       List<ChatDTO> chatDTOList1 = new ArrayList<>();
        for (ChatMessage chat : chatMessages){
            ChatDTO chatDTO1 = new ChatDTO();
            chatDTO1.setId(chat.getId());
            chatDTO1.setContent(chat.getContent());
            chatDTO1.setSender(chat.getSender().getId());
            chatDTO1.setReceiver(chat.getReceiver().getId());
            chatDTO1.setTimestamp(chat.getTimestamp());
            chatDTOList1.add(chatDTO1);
        }
        successResponse.setData(chatDTOList1);
        return successResponse;

    }
}
