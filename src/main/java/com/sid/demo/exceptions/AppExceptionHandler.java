package com.sid.demo.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AppExceptionHandler {

	@ExceptionHandler(value = { AppException.class })
    @ResponseBody
    public ResponseEntity<ErrorRespo> handleAppException(AppException ex) {
        ErrorRespo errorRespo = new ErrorRespo(ex.getMessage());
        return new ResponseEntity<>(errorRespo, ex.getHttpStatus());
    }
}
