package com.live.chat_service.controller;

import com.live.chat_service.dto.CreateGroupDto;
import com.live.chat_service.service.GroupChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @MessageMapping("/createGroupMessage")
//    @SendTo("/topic/group/message")
//    public CreateGroupDto createGroupMessage(@Payload CreateGroupDto createGroupDto) {
//        return groupChatService.createGroupMessage(createGroupDto);
//    }

}
