package com.live.chat_service.service;

import com.live.chat_service.dto.CreateGroupDto;
import com.live.chat_service.dto.GetGroupByIdDto;
import org.springframework.stereotype.Service;

@Service
public interface GroupChatService {

    CreateGroupDto createGroup(CreateGroupDto createGroupDto);

    GetGroupByIdDto groupById(Long getGroupByIdDto);
}
