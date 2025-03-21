package com.live.chat_service.service;

import com.live.chat_service.dto.CreateGroupDto;
import com.live.chat_service.dto.MessageDto;
import com.live.chat_service.response.SuccessResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GroupChatService {

    CreateGroupDto createGroup(CreateGroupDto createGroupDto);

    MessageDto saveGroupMessage(MessageDto messageDto);

    SuccessResponse<List<MessageDto>> getGroupChatMessages(Long groupId);
}
