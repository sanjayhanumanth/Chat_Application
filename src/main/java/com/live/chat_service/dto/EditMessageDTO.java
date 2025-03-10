package com.live.chat_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMessageDTO {
    private Long messageId;
    private String content;
}
