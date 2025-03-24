package com.live.chat_service.dto.privatechat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatMessageDto {

    private Long id;

    private Long senderId;

    private Long receiverId;

    private String content;

    private LocalDateTime timestamp;
}
