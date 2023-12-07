package com.eshya.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ForbiddenExceptionHandler {

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ErrorResponseEntity> handleServerErrorException(ForbiddenException exception, WebRequest request) {
		
		ErrorResponseEntity response = new ErrorResponseEntity();
		response.setError_Code(HttpStatus.FORBIDDEN);
		response.setMessage(exception.getMessage());
		response.setTimestamp(new Date());
		
		return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}
}
