package com.live.chat_service.dto.groupmessage;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetGroupByIdDto {

    private Long id;

    private String groupName;

    private byte[] image;

    private byte[] coverImage;

    private List<GroupUserDto> userGetDTOList;

    @Getter
    @Setter
    public static class GroupUserDto {
        private Long id;

        private String userName;

        private byte[] image;
    }

}
