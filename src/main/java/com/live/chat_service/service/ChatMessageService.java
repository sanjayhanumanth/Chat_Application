package com.live.chat_service.service;
import com.live.chat_service.dto.ChatDTO;
import com.live.chat_service.dto.MessageDto;
import com.live.chat_service.response.SuccessResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChatMessageService {

    MessageDto saveMessage(MessageDto chatMessage);

//    List<ChatDTO> getChatMessages(Long senderId, Long receiverId);

    SuccessResponse<List<ChatDTO>> getChatMessages(Long senderId, Long receiverId);
}
