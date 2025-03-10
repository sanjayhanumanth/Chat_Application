package com.live.chat_service.serviceimpl;

import com.live.chat_service.constant.Constant;
import com.live.chat_service.dto.EditMessageDTO;
import com.live.chat_service.dto.MessageDto;
import com.live.chat_service.exception.CustomValidationExceptions;
import com.live.chat_service.model.ChatMessage;
import com.live.chat_service.model.User;
import com.live.chat_service.repository.ChatMessageRepository;
import com.live.chat_service.repository.UserRepository;
import com.live.chat_service.response.SuccessResponse;
import com.live.chat_service.response.UserContextHolder;
import com.live.chat_service.service.ChatMessageService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        chatMessage.setReadFlag(false);
        chatMessageRepository.save(chatMessage);
        messageDto.setId(chatMessage.getId());
        messageDto.setTimestamp(chatMessage.getTimestamp() );
        return messageDto;
    }

    @Override
    public SuccessResponse<List<MessageDto>> getChatMessages(Long senderId, Long receiverId) {
        SuccessResponse<List<MessageDto>> successResponse = new SuccessResponse<>();
        List<ChatMessage> chatMessages = chatMessageRepository.findBySenderReceiverId(senderId, receiverId);
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

    @Override
    public SuccessResponse<Object> editMessages(EditMessageDTO editMessageDTO) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        Long userId = UserContextHolder.getUserTokenDto().getId();
        Optional<ChatMessage> message = chatMessageRepository.findById(editMessageDTO.getMessageId());
        if(message.isPresent() && Objects.equals(message.get().getSender().getId(), userId)){
            ChatMessage chatMessage = message.get();
            LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
            if (chatMessage.getTimestamp().isBefore(tenMinutesAgo)) {
                throw new CustomValidationExceptions(Constant.EDITED_TIME_EXCEEDED);
            }
            chatMessage.setContent(editMessageDTO.getContent());
            chatMessageRepository.save(chatMessage);
        }
        else {
            throw new CustomValidationExceptions(Constant.MESSAGE_NOT_FOUND);
        }
        successResponse.setStatusMessage(Constant.MESSAGE_UPDATED);
        return successResponse;
    }
}
