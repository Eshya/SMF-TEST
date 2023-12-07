package com.eshya.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.BadRequest;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Unauthorized.class)
	public ResponseEntity<?> handleUnauthorizedException(Exception exception, WebRequest request) {
		EntityError errorEntity = new EntityError(new Date(), HttpStatus.UNAUTHORIZED.toString(),
				"The user does not have valid authentication credentials for the target resource",
				request.getDescription(false));
		return new ResponseEntity<>(errorEntity, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(BadRequest.class)
	public ResponseEntity<?> handleBadRequestException(Exception exception, WebRequest request) {
		EntityError errorEntity = new EntityError(new Date(), HttpStatus.BAD_REQUEST.toString(),
				"The server cannot or will not process the request due to an apparent client error ",
				request.getDescription(false));
		return new ResponseEntity<>(errorEntity, HttpStatus.BAD_REQUEST);
	}

}
