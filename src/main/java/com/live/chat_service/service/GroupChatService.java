package com.live.chat_service.service;

import com.live.chat_service.dto.groupchat.CreateGroupDto;
import com.live.chat_service.dto.groupchat.GetGroupByIdDto;
import com.live.chat_service.dto.groupchat.GroupChatSaveDto;
import com.live.chat_service.dto.groupchat.MessageDto;
import com.live.chat_service.response.SuccessResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupChatService {

    CreateGroupDto createGroup(CreateGroupDto createGroupDto);

    GetGroupByIdDto groupById(Long getGroupByIdDto);

    GroupChatSaveDto saveGroupMessage(GroupChatSaveDto messageDto);

    SuccessResponse<List<MessageDto>> getGroupChatMessages(Long groupId);
}
