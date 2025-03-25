package com.live.chat_service.service;

import com.live.chat_service.dto.CallEndDto;
import com.live.chat_service.dto.CallRequestDto;
import com.live.chat_service.dto.CallResponseDto;
import com.live.chat_service.dto.ICERequestDto;
import com.live.chat_service.dto.SDPRequestDto;
import com.live.chat_service.response.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface CallService {
    ResponseEntity<SuccessResponse<Long>> initiateCall(CallRequestDto callRequest);

    ResponseEntity<SuccessResponse<String>> endCall(CallEndDto callEnd);

    ResponseEntity<SuccessResponse<String>> respondToCall(CallResponseDto responseDto);

    ResponseEntity<SuccessResponse<String>> exchangeICE(ICERequestDto iceRequest);

    ResponseEntity<SuccessResponse<String>> exchangeSDP(SDPRequestDto sdpRequest);
}
