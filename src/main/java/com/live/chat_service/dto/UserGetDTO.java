package com.live.chat_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGetDTO {

    private Long id;

    private String userName;

    private String emailId;

    private Long roleId;

    private byte[] image;

    private String phoneNumber;

    private String title;

    private String displayName;

    private String status;

}
