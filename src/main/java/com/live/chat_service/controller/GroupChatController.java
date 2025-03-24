package com.live.chat_service.controller;

import com.live.chat_service.dto.groupmessage.CreateGroupDto;
import com.live.chat_service.dto.groupmessage.GetGroupByIdDto;
import com.live.chat_service.dto.groupmessage.GroupChatSaveDto;
import com.live.chat_service.dto.groupmessage.MessageDto;
import com.live.chat_service.response.SuccessResponse;
import com.live.chat_service.service.GroupChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/groupById")
    public GetGroupByIdDto groupById(@RequestParam Long id) {
        return groupChatService.groupById(id);
    }

    @MessageMapping("/sendGroupMessage")
    @SendTo("/topic/group/{groupId}")
    public GroupChatSaveDto sendGroupMessage(@Payload GroupChatSaveDto messageDto) {
        return groupChatService.saveGroupMessage(messageDto);
    }

    @GetMapping("/group/{groupId}/messages")
    public SuccessResponse<List<MessageDto>> getGroupChatMessages(@PathVariable Long groupId) {
        return groupChatService.getGroupChatMessages(groupId);
    }
}
