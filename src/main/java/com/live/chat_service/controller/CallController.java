package com.live.chat_service.controller;

import com.live.chat_service.dto.CallEndDto;
import com.live.chat_service.dto.CallRequestDto;
import com.live.chat_service.dto.CallResponseDto;
import com.live.chat_service.dto.ICERequestDto;
import com.live.chat_service.dto.SDPRequestDto;
import com.live.chat_service.response.SuccessResponse;
import com.live.chat_service.service.CallService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/call")
public class CallController {

    private final CallService callService;

    public CallController(CallService callService) {
        this.callService = callService;
    }

    @PostMapping("/call/initiate")
    public ResponseEntity<SuccessResponse<Long>> initiateCall(@RequestBody CallRequestDto callRequest) {
        return callService.initiateCall(callRequest);
    }

    @PostMapping("/call/end")
    public ResponseEntity<SuccessResponse<String>> endCall(@RequestBody CallEndDto callEnd) {
        return callService.endCall(callEnd);
    }

    @PostMapping("/call/respond")
    public ResponseEntity<SuccessResponse<String>> respondToCall(@RequestBody CallResponseDto responseDto) {
        return callService.respondToCall(responseDto);
    }

    @PostMapping("/call/ice")
    public ResponseEntity<SuccessResponse<String>> exchangeICE(@RequestBody ICERequestDto iceRequest) {
        return callService.exchangeICE(iceRequest);
    }

    @PostMapping("/call/sdp")
    public ResponseEntity<SuccessResponse<String>> exchangeSDP(@RequestBody SDPRequestDto sdpRequest) {
        return callService.exchangeSDP(sdpRequest);

    }


}
