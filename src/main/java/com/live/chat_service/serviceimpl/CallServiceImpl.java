package com.live.chat_service.serviceimpl;

import com.live.chat_service.dto.CallEndDto;
import com.live.chat_service.dto.CallRequestDto;
import com.live.chat_service.dto.CallResponseDto;
import com.live.chat_service.dto.ICERequestDto;
import com.live.chat_service.dto.SDPRequestDto;
import com.live.chat_service.exception.CustomValidationExceptions;
import com.live.chat_service.model.ChatCall;
import com.live.chat_service.model.User;
import com.live.chat_service.repository.ChatCallRepository;
import com.live.chat_service.repository.UserRepository;
import com.live.chat_service.response.SuccessResponse;
import com.live.chat_service.service.CallService;
import com.live.chat_service.service.WebSocketService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CallServiceImpl implements CallService {

    private final UserRepository userRepository;
    private final ChatCallRepository callRepository;
    private final WebSocketService webSocketService;

    public CallServiceImpl(UserRepository userRepository, ChatCallRepository callRepository, WebSocketService webSocketService) {
        this.userRepository = userRepository;
        this.callRepository = callRepository;
        this.webSocketService = webSocketService;
    }

    @Override
    public ResponseEntity<SuccessResponse<Long>> initiateCall(CallRequestDto callRequest) {
        User sender = userRepository.findById(callRequest.getSenderId())
                .orElseThrow(() -> new CustomValidationExceptions("Sender not found"));

        User receiver = userRepository.findById(callRequest.getReceiverId())
                .orElseThrow(() -> new CustomValidationExceptions("Receiver not found"));

        ChatCall call = new ChatCall();
        call.setSender(sender);
        call.setReceiver(receiver);
        call.setStatus("RINGING");
        call.setTimestamp(LocalDateTime.now());
        callRepository.save(call);

        webSocketService.sendCallNotification(receiver.getId(), call);

        SuccessResponse<Long> response = new SuccessResponse<>();
        response.setStatusMessage("Call initiated successfully");
        response.setData(call.getId());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SuccessResponse<String>> endCall(CallEndDto callEnd) {
        ChatCall call = callRepository.findById(callEnd.getCallId())
                .orElseThrow(() -> new CustomValidationExceptions("Call not found"));

        call.setStatus("ENDED");
        callRepository.save(call);

        webSocketService.notifyCallEnded(call.getSender().getId(), call.getReceiver().getId());

        SuccessResponse<String> response = new SuccessResponse<>();
        response.setStatusMessage("Call ended successfully");
        response.setData(null);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SuccessResponse<String>> respondToCall(CallResponseDto responseDto) {
        ChatCall call = callRepository.findById(responseDto.getCallId())
                .orElseThrow(() -> new CustomValidationExceptions("Call not found"));

        if (responseDto.getIsAccepted()) {
            call.setStatus("ACCEPTED");
            webSocketService.notifyCallAccepted(call.getSender().getId(), call.getId());
        } else {
            call.setStatus("REJECTED");
            webSocketService.notifyCallRejected(call.getSender().getId(), call.getId());
        }

        callRepository.save(call);
        SuccessResponse<String> response = new SuccessResponse<>();
        response.setStatusMessage("Call status updated");
        response.setData(null);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SuccessResponse<String>> exchangeICE(ICERequestDto iceRequest) {
        webSocketService.sendICECandidate(iceRequest.getReceiverId(), iceRequest.getIceCandidate());

        SuccessResponse<String> response = new SuccessResponse<>();
        response.setStatusMessage("ICE Candidate Sent");
        response.setData(null);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SuccessResponse<String>> exchangeSDP(SDPRequestDto sdpRequest) {
        webSocketService.sendSDP(sdpRequest.getReceiverId(), sdpRequest.getSdp());

        SuccessResponse<String> response = new SuccessResponse<>();
        response.setStatusMessage("SDP Sent");
        response.setData(null);
        return ResponseEntity.ok(response);
    }
}
