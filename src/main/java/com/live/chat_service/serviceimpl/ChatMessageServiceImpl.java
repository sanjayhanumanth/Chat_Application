package com.live.chat_service.serviceimpl;

import com.live.chat_service.dto.ChatDTO;
import com.live.chat_service.dto.MessageDto;
import com.live.chat_service.exception.CustomValidationExceptions;
import com.live.chat_service.model.ChatMessage;
import com.live.chat_service.model.User;
import com.live.chat_service.repository.ChatMessageRepository;
import com.live.chat_service.repository.UserRepository;
import com.live.chat_service.response.SuccessResponse;
import com.live.chat_service.service.ChatMessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final UserRepository userRepository;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository, UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public MessageDto saveMessage(MessageDto messageDto) {
        User user=userRepository.findByIdIsActive(messageDto.getReceiverId()).
                orElseThrow(() -> new CustomValidationExceptions("Receiver not found with id: " + messageDto.getReceiverId()));
        User user1=userRepository.findByIdIsActive(messageDto.getSenderId()).
                orElseThrow(() -> new CustomValidationExceptions("Sender not found with id: " + messageDto.getSenderId()));;
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setReceiver(user);
        chatMessage.setSender(user1);
        chatMessage.setContent(messageDto.getContent());
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessageRepository.save(chatMessage);
        messageDto.setId(chatMessage.getId());
        messageDto.setTimestamp(chatMessage.getTimestamp() );
        return messageDto;
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
