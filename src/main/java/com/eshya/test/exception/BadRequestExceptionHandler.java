package com.eshya.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class BadRequestExceptionHandler {

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponseEntity> handleBadRequestException(BadRequestException exception, WebRequest request) {
    
    ErrorResponseEntity response = new ErrorResponseEntity();
    response.setError_Code(HttpStatus.BAD_REQUEST);
//    response.setField(exception.getFieldName());
//    response.setValue(exception.getFieldValue() != null ? exception.getFieldValue().toString() : null);
    response.setMessage(exception.getMessage());
//    response.setTimestamp(new Date());
    
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }
  
}
