package com.live.chat_service.service;
import com.live.chat_service.dto.privatechat.ChatMessageDto;
import com.live.chat_service.dto.EditMessageDTO;
import com.live.chat_service.response.SuccessResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatMessageService {

    ChatMessageDto saveMessage(ChatMessageDto chatMessage);

    SuccessResponse<List<ChatMessageDto>> getChatMessages(Long senderId, Long receiverId);

    SuccessResponse<Object> editMessages(EditMessageDTO editMessageDTO);

    SuccessResponse<Object> readMessage(Long senderId, Long receiverId);

    SuccessResponse<Object> getByIdMessages(Long messageId);
}
