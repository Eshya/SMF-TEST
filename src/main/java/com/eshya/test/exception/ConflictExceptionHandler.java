package com.eshya.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ConflictExceptionHandler {

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ErrorResponseEntity> handleBadRequestException(ConflictException exception,
			WebRequest request) {

		ErrorResponseEntity response = new ErrorResponseEntity();
		response.setError_Code(HttpStatus.CONFLICT);
		response.setMessage(exception.getMessage());

		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

}
