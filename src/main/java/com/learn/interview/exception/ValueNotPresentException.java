package com.learn.interview.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class ValueNotPresentException extends RuntimeException{
	public ValueNotPresentException(String message) {
		super(message);
	}
	
	public ValueNotPresentException(String message, Throwable t) {
		super(message, t);
	}
}