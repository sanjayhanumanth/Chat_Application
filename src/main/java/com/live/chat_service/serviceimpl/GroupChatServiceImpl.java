package com.live.chat_service.serviceimpl;

import com.live.chat_service.dto.CreateGroupDto;
import com.live.chat_service.exception.CustomValidationExceptions;
import com.live.chat_service.model.GroupChatMessage;
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
        GroupChatMessage groupChatMessage=new GroupChatMessage();
        groupChatMessage.setGroupName(createGroupDto.getGroupName());
        groupChatMessage.setActive(true);
        groupChatMessage.setDeletedFlag(false);
        groupChatMessage.setCreatedAt(Timestamp.from(Instant.now()));
        groupChatMessage.setCreatedBy(userId);
        groupChatMessageRepository.save(groupChatMessage);
        List<GroupChatUser> groupChatUserList=createGroupDto.getUserIds().
                stream().map(users ->{
                    GroupChatUser groupChatUser=new GroupChatUser();
                    User user=userRepository.findByIdIsActive(users).
                            orElseThrow(() -> new CustomValidationExceptions("User not found with id: " + users));
                    groupChatUser.setUser(user);
                    groupChatUser.setGroupChatMessage(groupChatMessage);
                    groupChatUser.setActive(true);
                    return groupChatUser;
                }).toList();
        groupChatUserRepository.saveAll(groupChatUserList);
        createGroupDto.setId(groupChatMessage.getId());
        return createGroupDto;
    }
}
