package com.live.chat_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserListDTO {

    private Long id;
    private String name;
    private String email;
    private Long roleId;
    private byte[] image;
}
