package com.live.chat_service.exception;
import org.springframework.http.ResponseEntity;

public class ResponseEntityBuilder {

    public static ResponseEntity<Object> build(ApiError apiError){

        return new ResponseEntity<>( apiError, apiError.getStatus( ) );
    }

    private ResponseEntityBuilder() {
    }
}


