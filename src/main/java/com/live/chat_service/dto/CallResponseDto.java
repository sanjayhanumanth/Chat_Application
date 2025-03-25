package com.live.chat_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CallResponseDto {

    private Long callId;

    private Boolean isAccepted;
}
