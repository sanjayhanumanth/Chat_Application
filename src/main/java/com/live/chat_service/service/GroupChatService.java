package com.live.chat_service.service;

import com.live.chat_service.dto.groupmessage.CreateGroupDto;
import com.live.chat_service.dto.groupmessage.GetGroupByIdDto;
import com.live.chat_service.dto.groupmessage.GroupChatSaveDto;
import com.live.chat_service.dto.groupmessage.MessageDto;
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
