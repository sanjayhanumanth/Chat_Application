package com.live.chat_service.service;

import com.live.chat_service.dto.CreateGroupDto;
import org.springframework.stereotype.Service;

@Service
public interface GroupChatService {

    CreateGroupDto createGroup(CreateGroupDto createGroupDto);
}
