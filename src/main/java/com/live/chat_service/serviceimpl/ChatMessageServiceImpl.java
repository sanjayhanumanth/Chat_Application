package com.live.chat_service.serviceimpl;

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
    public SuccessResponse<List<MessageDto>> getChatMessages(Long senderId, Long receiverId) {
        SuccessResponse<List<MessageDto>> successResponse = new SuccessResponse<>();
        List<ChatMessage> chatMessages = chatMessageRepository.findBySenderIdAndReceiverIdOrderByTimestampAsc(senderId, receiverId);
       List<MessageDto> messageDtos = new ArrayList<>();
        for (ChatMessage chat : chatMessages){
            MessageDto messageDto = new MessageDto();
            messageDto.setId(chat.getId());
            messageDto.setContent(chat.getContent());
            messageDto.setSenderId(chat.getSender().getId());
            messageDto.setReceiverId(chat.getReceiver().getId());
            messageDto.setTimestamp(chat.getTimestamp());
            messageDtos.add(messageDto);
        }
        successResponse.setData(messageDtos);
        return successResponse;

    }
}
