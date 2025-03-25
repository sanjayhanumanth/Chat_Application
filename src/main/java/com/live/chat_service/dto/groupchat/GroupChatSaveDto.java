package com.live.chat_service.dto.groupchat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupChatSaveDto {

    private Long id;

    private Long senderId;

    private List<Long> receiversId;

    private String content;

    private LocalDateTime timestamp;

    private Long groupChatId;
}
