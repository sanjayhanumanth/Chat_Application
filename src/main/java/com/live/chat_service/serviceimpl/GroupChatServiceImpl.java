package com.live.chat_service.serviceimpl;

import com.live.chat_service.dto.CreateGroupDto;
import com.live.chat_service.dto.GetGroupByIdDto;
import com.live.chat_service.exception.CustomValidationExceptions;
import com.live.chat_service.model.GroupChat;
import com.live.chat_service.model.GroupChatUser;
import com.live.chat_service.model.User;
import com.live.chat_service.repository.GroupChatMessageRepository;
import com.live.chat_service.repository.GroupChatUserRepository;
import com.live.chat_service.repository.UserRepository;
import com.live.chat_service.service.GroupChatService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;

    public GroupChatServiceImpl(GroupChatMessageRepository groupChatMessageRepository, UserRepository userRepository, GroupChatUserRepository groupChatUserRepository, ModelMapper modelMapper) {
        this.groupChatMessageRepository = groupChatMessageRepository;
        this.userRepository = userRepository;
        this.groupChatUserRepository = groupChatUserRepository;
        this.modelMapper = modelMapper;
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
        groupChatMessageRepository.save(groupChat);
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
    public GetGroupByIdDto groupById(Long id) {
        GroupChat groupChat=groupChatMessageRepository.findByIdAndIsActiveTrue(id).orElseThrow
                (()->new CustomValidationExceptions("Group not found with Id : "+id));
        GetGroupByIdDto getGroupByIdDto=new GetGroupByIdDto();
        modelMapper.map(groupChat, getGroupByIdDto);
        List<GetGroupByIdDto.GroupUserDto> userGetDTOList;
        List<GroupChatUser> groupChatUserList=groupChatUserRepository.findByIsActiveTrue(id);
        userGetDTOList = groupChatUserList.stream()
                .map(groupChatUser -> {
                    GetGroupByIdDto.GroupUserDto userGetDTO = new GetGroupByIdDto.GroupUserDto();
                    userGetDTO.setId(groupChatUser.getUser().getId());
                    userGetDTO.setUserName(groupChatUser.getUser().getUserName());
                    userGetDTO.setImage(groupChatUser.getUser().getImage());
                    return userGetDTO;
                })
                .toList();
        getGroupByIdDto.setUserGetDTOList(userGetDTOList);
        return getGroupByIdDto;
    }
}
