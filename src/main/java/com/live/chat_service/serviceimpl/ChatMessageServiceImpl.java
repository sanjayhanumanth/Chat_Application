package com.live.chat_service.serviceimpl;

import com.live.chat_service.constant.Constant;
import com.live.chat_service.dto.privatechat.ChatMessageDto;
import com.live.chat_service.dto.EditMessageDTO;
import com.live.chat_service.exception.CustomValidationExceptions;
import com.live.chat_service.model.ChatMessage;
import com.live.chat_service.model.User;
import com.live.chat_service.repository.ChatMessageRepository;

import com.live.chat_service.repository.UserRepository;
import com.live.chat_service.response.SuccessResponse;
import com.live.chat_service.response.UserContextHolder;
import com.live.chat_service.service.ChatMessageService;
import com.live.chat_service.util.CommonUtil;
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
    private final CommonUtil commonUtil;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository,  UserRepository userRepository, CommonUtil commonUtil) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.commonUtil = commonUtil;
    }

    @Override
    public ChatMessageDto saveMessage(ChatMessageDto messageDto) {
        User user=userRepository.findByIdIsActive(messageDto.getReceiverId()).
                orElseThrow(() -> new CustomValidationExceptions("Receiver not found with id: " + messageDto.getReceiverId()));
        User user1=userRepository.findByIdIsActive(messageDto.getSenderId()).
                orElseThrow(() -> new CustomValidationExceptions("Sender not found with id: " + messageDto.getSenderId()));
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setReceiver(user);
        chatMessage.setSender(user1);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setReadFlag(false);
        chatMessage.setContent(commonUtil.encryptMessage(messageDto.getContent()));
        chatMessageRepository.save(chatMessage);

        messageDto.setId(chatMessage.getId());
        messageDto.setTimestamp(chatMessage.getTimestamp() );
        return messageDto;
    }

    @Override
    public SuccessResponse<List<ChatMessageDto>> getChatMessages(Long senderId, Long receiverId) {
        SuccessResponse<List<ChatMessageDto>> successResponse = new SuccessResponse<>();
        List<ChatMessage> chatMessages = chatMessageRepository.findBySenderReceiverId(senderId, receiverId);
       List<ChatMessageDto> messageDtos = new ArrayList<>();
        for (ChatMessage chat : chatMessages){
            ChatMessageDto messageDto = new ChatMessageDto();
            messageDto.setId(chat.getId());
            messageDto.setSenderId(chat.getSender().getId());
            messageDto.setReceiverId(chat.getReceiver().getId());
            messageDto.setTimestamp(chat.getTimestamp());
            messageDto.setContent(commonUtil.decryptMessage(chat.getContent()));
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
            chatMessage.setContent(commonUtil.encryptMessage(editMessageDTO.getContent()));
            chatMessageRepository.save(chatMessage);
        }
        else {
            throw new CustomValidationExceptions(Constant.MESSAGE_NOT_FOUND);
        }
        successResponse.setStatusMessage(Constant.MESSAGE_UPDATED);
        return successResponse;
    }

    @Override
    public SuccessResponse<Object> readMessage(Long senderId, Long receiverId) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        List<ChatMessage> chatMessages = chatMessageRepository.findByNonReadMessage(senderId, receiverId);
        if(!chatMessages.isEmpty()){
            chatMessages.forEach(chatMessage -> chatMessage.setReadFlag(true));
            chatMessageRepository.saveAll(chatMessages);
            successResponse.setStatusMessage(Constant.MESSAGE_RED);
        }
        return successResponse;
    }

    @Override
    public SuccessResponse<Object> getByIdMessages(Long messageId) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        ChatMessageDto messageDto = new ChatMessageDto();
        Optional<ChatMessage> message = chatMessageRepository.findById(messageId);
        if(message.isPresent()){
            ChatMessage chatMessage = message.get();
            messageDto.setId(chatMessage.getId());
            messageDto.setSenderId(chatMessage.getSender().getId());
            messageDto.setReceiverId(chatMessage.getReceiver().getId());
            messageDto.setTimestamp(chatMessage.getTimestamp());
            messageDto.setContent(commonUtil.decryptMessage(chatMessage.getContent()));
        }
        successResponse.setData(messageDto);
        return successResponse;
    }


}
