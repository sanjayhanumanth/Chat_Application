package com.live.chat_service.service;

import com.live.chat_service.model.ChatCall;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendCallNotification(Long receiverId, ChatCall call) {
        messagingTemplate.convertAndSend("/topic/call/" + receiverId, call);
    }

    public void notifyCallAccepted(Long senderId, Long callId) {
        messagingTemplate.convertAndSend("/topic/call/accepted/" + senderId, callId);
    }

    public void notifyCallRejected(Long senderId, Long callId) {
        messagingTemplate.convertAndSend("/topic/call/rejected/" + senderId, callId);
    }

    public void sendSDP(Long receiverId, String sdp) {
        messagingTemplate.convertAndSend("/topic/call/sdp/" + receiverId, sdp);
    }

    public void sendICECandidate(Long receiverId, String iceCandidate) {
        messagingTemplate.convertAndSend("/topic/call/ice/" + receiverId, iceCandidate);
    }

    public void notifyCallEnded(Long senderId, Long receiverId) {
        messagingTemplate.convertAndSend("/topic/call/end/" + senderId, "Call Ended");
        messagingTemplate.convertAndSend("/topic/call/end/" + receiverId, "Call Ended");
    }
}

