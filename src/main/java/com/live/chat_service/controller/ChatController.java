package com.live.chat_service.controller;
import com.live.chat_service.model.ChatMessage;
import com.live.chat_service.service.ChatMessageService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final ChatMessageService chatMessageService;

    public ChatController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/sendMessage") // Receive messages
    @SendTo("/topic/messages") // Broadcast messages
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        return chatMessageService.saveMessage(chatMessage);
    }
}

