package com.eshya.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class NotFoundExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ErrorResponseEntity> handleServerErrorException(NotFoundException exception, WebRequest request) {
		
		ErrorResponseEntity response = new ErrorResponseEntity();
		response.setError_Code(HttpStatus.NOT_FOUND);
		response.setMessage(exception.getMessage());
		response.setTimestamp(new Date());
		
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
}
