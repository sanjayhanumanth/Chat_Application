package com.live.chat_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserOtpValidationDto {
    private String email;
    private String otp;
}
