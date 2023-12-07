package com.eshya.test.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedRequestException  extends RuntimeException{

private static final long serialVersionUID = 1L;
	
	private String fieldName;
	
	private Object fieldValue;

	public UnauthorizedRequestException(String message, String fieldName, Object fieldValue) {
		super(message);
		
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}

	public String getFieldName() {
		return fieldName;
	}

	public Object getFieldValue() {
		return fieldValue;
	}
	
	
}
