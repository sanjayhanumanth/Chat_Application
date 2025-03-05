package com.live.chat_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTokenDto {

    private Long id;
    private String email;
    private String username;
    private String sub;
    private String role;
    private long iat;
    private long exp;

    @Override
    public String toString() {
        return "UserTokenDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", sub='" + sub + '\'' +
                ", role='" + role + '\'' +
                ", iat=" + iat +
                ", exp=" + exp +
                '}';
    }
}
