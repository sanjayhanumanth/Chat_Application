package com.live.chat_service.exception;
import java.io.Serial;

public class CustomValidationExceptions extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;
    public CustomValidationExceptions(String msg) {
        super(msg);
    }
}

