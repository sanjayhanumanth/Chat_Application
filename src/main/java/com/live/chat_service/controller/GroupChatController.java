package com.live.chat_service.controller;

import com.live.chat_service.dto.CreateGroupDto;
import com.live.chat_service.dto.MessageDto;
import com.live.chat_service.response.SuccessResponse;
import com.live.chat_service.service.GroupChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/groupChat")
public class GroupChatController {

    private final GroupChatService groupChatService;


    public GroupChatController(GroupChatService groupChatService) {
        this.groupChatService = groupChatService;
    }

    @MessageMapping("/createGroup")
    @SendTo("/topic/group")
    public CreateGroupDto createGroup(@Payload CreateGroupDto createGroupDto) {
        return groupChatService.createGroup(createGroupDto);
    }

    @MessageMapping("/sendGroupMessage")
    @SendTo("/topic/group/{groupId}")
    public MessageDto sendGroupMessage(@Payload MessageDto messageDto) {
        return groupChatService.saveGroupMessage(messageDto);
    }

    @GetMapping("/group/{groupId}/messages")
    public SuccessResponse<List<MessageDto>> getGroupChatMessages(@PathVariable Long groupId) {
        return groupChatService.getGroupChatMessages(groupId);
    }
}
