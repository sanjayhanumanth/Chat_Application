package com.live.chat_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {

    /**
     * Deal with ALL Other Exceptions
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex,WebRequest request){

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ApiError err = new ApiError(LocalDateTime.now(),HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage(),details);
        return ResponseEntityBuilder.build(err);
    }

    @ExceptionHandler({CustomValidationExceptions.class})
    public ResponseEntity<Object> customValidationException(CustomValidationExceptions ex){

        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ApiError err = new ApiError(LocalDateTime.now(),HttpStatus.BAD_REQUEST,ex.getMessage(),details);
        return ResponseEntityBuilder.build(err);
    }

}

