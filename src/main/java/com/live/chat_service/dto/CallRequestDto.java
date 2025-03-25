package com.live.chat_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallRequestDto {

    private Long id;
    private Long senderId;
    private Long receiverId;
}
