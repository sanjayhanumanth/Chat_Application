package com.live.chat_service.dto;

import com.live.chat_service.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatDTO {
     private Long id;

    private String content;

    private Long sender;

    private Long receiver;

    private LocalDateTime timestamp;
}
