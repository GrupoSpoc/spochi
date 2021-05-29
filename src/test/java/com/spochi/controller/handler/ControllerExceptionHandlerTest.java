package com.spochi.controller.handler;

import com.spochi.service.UserServiceException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class ControllerExceptionHandlerTest {
    final String message = "for coverage propose";
    final ControllerExceptionHandler handler = new ControllerExceptionHandler();
    ResponseEntity<String> responseEntity;

    @Test
    void handlerBadRequestExceptionWorks(){
        UserServiceException userServiceException = new UserServiceException(message);
        responseEntity = handler.handleBadRequestException(userServiceException);

        assertEquals("The Services fail because : " + message,responseEntity.getBody());
    }

    @Test
    void handlerExceptionWorks(){
        Exception exception = new Exception(message);
        responseEntity = handler.handleException(exception);

        assertEquals(message,responseEntity.getBody());
    }

    @Test
    void handlerRuntimeExceptionWorks(){
        RuntimeException runtimeException = new RuntimeException(message);
        responseEntity =handler.handleRuntimeException(runtimeException);

        assertEquals(message,responseEntity.getBody());
    }
}