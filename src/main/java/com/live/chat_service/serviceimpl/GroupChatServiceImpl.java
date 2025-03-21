package com.live.chat_service.serviceimpl;

import com.live.chat_service.dto.CreateGroupDto;
import com.live.chat_service.dto.MessageDto;
import com.live.chat_service.exception.CustomValidationExceptions;
import com.live.chat_service.model.ChatMessage;
import com.live.chat_service.model.GroupChat;
import com.live.chat_service.model.GroupChatMessage;
import com.live.chat_service.model.GroupChatUser;
import com.live.chat_service.model.User;
import com.live.chat_service.repository.ChatMessageRepository;
import com.live.chat_service.repository.GroupChatMessageRepository;
import com.live.chat_service.repository.GroupChatRepository;
import com.live.chat_service.repository.GroupChatUserRepository;
import com.live.chat_service.repository.UserRepository;
import com.live.chat_service.response.SuccessResponse;
import com.live.chat_service.service.GroupChatService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class GroupChatServiceImpl implements GroupChatService {

    private static final String ALGORITHM = "AES";
    private static final byte[] SECRET_KEY = "1234567890123456".getBytes();
    private final GroupChatRepository groupChatRepository;
    private final GroupChatMessageRepository groupChatMessageRepository;
    private final UserRepository userRepository;

    private final GroupChatUserRepository groupChatUserRepository;


    public GroupChatServiceImpl(GroupChatRepository groupChatRepository, GroupChatMessageRepository groupChatMessageRepository, UserRepository userRepository, GroupChatUserRepository groupChatUserRepository) {
        this.groupChatRepository = groupChatRepository;
        this.groupChatMessageRepository = groupChatMessageRepository;
        this.userRepository = userRepository;
        this.groupChatUserRepository = groupChatUserRepository;
    }

    @Transactional
    @Override
    public CreateGroupDto createGroup(CreateGroupDto createGroupDto) {
        GroupChat groupChat =new GroupChat();
        groupChat.setGroupName(createGroupDto.getGroupName());
        groupChat.setActive(true);
        groupChat.setDeletedFlag(false);
        groupChat.setCreatedAt(Timestamp.from(Instant.now()));
        groupChat.setCreatedBy(createGroupDto.getSenderId());
        groupChatRepository.save(groupChat);
        List<Long> groupMembers = new ArrayList<>(createGroupDto.getGroupMemberIds());
        groupMembers.add(createGroupDto.getSenderId());

        List<GroupChatUser> groupChatUserList= new ArrayList<>(groupMembers.
                stream().map(users -> {
                    GroupChatUser groupChatUser = new GroupChatUser();
                    User user = userRepository.findByIdIsActive(users).
                            orElseThrow(() -> new CustomValidationExceptions("User not found with id: " + users));
                    groupChatUser.setUser(user);
                    groupChatUser.setGroupChat(groupChat);
                    groupChatUser.setActive(true);
                    return groupChatUser;
                }).toList());
        groupChatUserRepository.saveAll(groupChatUserList);
        createGroupDto.setId(groupChat.getId());
        return createGroupDto;
    }

    @Override
    public MessageDto saveGroupMessage(MessageDto messageDto) {
        Optional<GroupChat> group = groupChatRepository.findByIdAndIsActiveTrue(messageDto.getGroupId());
        if(group.isEmpty()){
            throw new CustomValidationExceptions("Group not found with id: " + messageDto.getGroupId());
        }
        User sender = userRepository.findByIdIsActive(messageDto.getSenderId())
                .orElseThrow(() -> new CustomValidationExceptions("Sender not found with id: " + messageDto.getSenderId()));

        List<GroupChatUser> groupChatUserList = groupChatUserRepository.findByIsActiveTrue(messageDto.getGroupId());
        if(!groupChatUserList.isEmpty()) {
            for (GroupChatUser member : groupChatUserList) {
                GroupChatMessage chatMessage = new GroupChatMessage();
                chatMessage.setSender(sender);
                chatMessage.setReceiver(member.getUser());
                Optional<GroupChat> groupChatOptional = groupChatRepository.findByIdAndIsActiveTrue(messageDto.getGroupId());
                groupChatOptional.ifPresent(chatMessage::setGroupChat);
                chatMessage.setTimestamp(LocalDateTime.now());
                chatMessage.setReadFlag(false);
                encryptMessage(messageDto.getContent(), chatMessage);
                groupChatMessageRepository.save(chatMessage);
            }
        }
        messageDto.setTimestamp(LocalDateTime.now());
        return messageDto;
    }

    @Override
    public SuccessResponse<List<MessageDto>> getGroupChatMessages(Long groupId) {
        SuccessResponse<List<MessageDto>> successResponse = new SuccessResponse<>();

        List<GroupChatMessage> chatMessages = groupChatMessageRepository.findByGroupId(groupId);
        List<MessageDto> messages = new ArrayList<>();

        for (GroupChatMessage chat : chatMessages) {
            MessageDto messageDto = new MessageDto();
            messageDto.setId(chat.getId());
            messageDto.setSenderId(chat.getSender().getId());
            messageDto.setGroupId(chat.getGroupChat().getId());
            messageDto.setTimestamp(chat.getTimestamp());
            decryptMessage(chat, messageDto);
            messages.add(messageDto);
        }

        successResponse.setData(messages);
        return successResponse;
    }

    private static void encryptMessage(String messageDto, GroupChatMessage chatMessage) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKey secretKey = new SecretKeySpec(SECRET_KEY, ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(messageDto.getBytes());
            String encryptedContent = Base64.getEncoder().encodeToString(encryptedData);
            chatMessage.setContent(encryptedContent);
        } catch (Exception e) {
            throw new CustomValidationExceptions("Error while encrypting");
        }
    }

    private static void decryptMessage(GroupChatMessage chatMessage, MessageDto messageDto) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            SecretKey secretKey = new SecretKeySpec(SECRET_KEY, ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(chatMessage.getContent()));
            messageDto.setContent(new String(decryptedData));
        } catch (Exception e) {
            throw new CustomValidationExceptions("Error while decrypting");
        }
    }
}
