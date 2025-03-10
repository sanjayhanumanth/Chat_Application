package com.live.chat_service.controller;

import com.live.chat_service.dto.CreateGroupDto;
import com.live.chat_service.dto.MessageDto;
import com.live.chat_service.service.GroupChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groupChat")
public class GroupChatController {

    private final GroupChatService groupChatService;

    private final SimpMessagingTemplate messagingTemplate;

    public GroupChatController(GroupChatService groupChatService, SimpMessagingTemplate messagingTemplate) {
        this.groupChatService = groupChatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/createGroup")
    public CreateGroupDto createGroup(@Payload CreateGroupDto createGroupDto) {
        CreateGroupDto createGroup=groupChatService.createGroup(createGroupDto);
        List<Long> memberIds = createGroupDto.getUserIds();
        for (Long memberId : memberIds) {
            messagingTemplate.convertAndSend("/topic/group/" + memberId, createGroupDto);
        }
        return createGroup;
    }

}
