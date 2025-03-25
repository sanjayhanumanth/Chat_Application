package com.live.chat_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ICERequestDto {
    private Long receiverId;
    private String iceCandidate;
}

