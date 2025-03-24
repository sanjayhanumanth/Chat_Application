package com.live.chat_service.dto.groupmessage;

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
public class MessageDto {

    private Long id;

    private Long senderId;

    private List<MessageGroupMembersDto> receivers;

    private String content;

    private LocalDateTime timestamp;

    private Long groupChatId;

    @Getter
    @Setter
    public static class MessageGroupMembersDto {
        private Long receiverId;

        private boolean readFlag;
    }

}
