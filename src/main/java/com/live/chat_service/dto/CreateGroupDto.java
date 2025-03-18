package com.live.chat_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateGroupDto {

    private Long id;

    private String groupName;

    private Long senderId;

    private List<Long> groupMemberIds;

}
