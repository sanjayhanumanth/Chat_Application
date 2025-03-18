package com.live.chat_service.serviceimpl;

import com.live.chat_service.dto.CreateGroupDto;
import com.live.chat_service.exception.CustomValidationExceptions;
import com.live.chat_service.model.GroupChat;
import com.live.chat_service.model.GroupChatUser;
import com.live.chat_service.model.User;
import com.live.chat_service.repository.GroupChatMessageRepository;
import com.live.chat_service.repository.GroupChatUserRepository;
import com.live.chat_service.repository.UserRepository;
import com.live.chat_service.response.UserContextHolder;
import com.live.chat_service.service.GroupChatService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupChatServiceImpl implements GroupChatService {

    private final GroupChatMessageRepository groupChatMessageRepository;

    private final UserRepository userRepository;

    private final GroupChatUserRepository groupChatUserRepository;

    public GroupChatServiceImpl(GroupChatMessageRepository groupChatMessageRepository, UserRepository userRepository, GroupChatUserRepository groupChatUserRepository) {
        this.groupChatMessageRepository = groupChatMessageRepository;
        this.userRepository = userRepository;
        this.groupChatUserRepository = groupChatUserRepository;
    }

    @Transactional
    @Override
    public CreateGroupDto createGroup(CreateGroupDto createGroupDto) {
        Long userId= UserContextHolder.getUserTokenDto().getId();
        GroupChat groupChat =new GroupChat();
        groupChat.setGroupName(createGroupDto.getGroupName());
        groupChat.setActive(true);
        groupChat.setDeletedFlag(false);
        groupChat.setCreatedAt(Timestamp.from(Instant.now()));
        groupChat.setCreatedBy(userId);
        groupChatMessageRepository.save(groupChat);
        List<GroupChatUser> groupChatUserList= new ArrayList<>(createGroupDto.getGroupMemberIds().
                stream().map(users -> {
                    GroupChatUser groupChatUser = new GroupChatUser();
                    User user = userRepository.findByIdIsActive(users).
                            orElseThrow(() -> new CustomValidationExceptions("User not found with id: " + users));
                    groupChatUser.setUser(user);
                    groupChatUser.setGroupChat(groupChat);
                    groupChatUser.setActive(true);
                    return groupChatUser;
                }).toList());
        GroupChatUser grChat=new GroupChatUser();
        User usr=userRepository.findByIdIsActive(userId).
                orElseThrow(() -> new CustomValidationExceptions("User not found with id: " + userId));
        grChat.setUser(usr);
        grChat.setGroupChat(groupChat);
        grChat.setActive(true);
        groupChatUserList.add(grChat);
        groupChatUserRepository.saveAll(groupChatUserList);
        createGroupDto.setId(groupChat.getId());
        return createGroupDto;
    }
}
