package com.learn.interview.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.learn.interview.dto.ErrorMessageDto;
import com.learn.interview.exception.NotFoundException;
import com.learn.interview.exception.ValueNotPresentException;

@RestControllerAdvice
public class ControllerAdvice {

	@ExceptionHandler({NotFoundException.class})
	public ResponseEntity<Object> notFoundException(NotFoundException e) {
		ErrorMessageDto error = new ErrorMessageDto(e.getMessage(), HttpStatus.NOT_FOUND);
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
		
	}
	
	@ExceptionHandler({ValueNotPresentException.class})
	public ResponseEntity<Object> valueNotPresentException(ValueNotPresentException e) {
		ErrorMessageDto error = new ErrorMessageDto(e.getMessage(), HttpStatus.BAD_REQUEST);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
		
	}
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> recordNotFoundException(Exception e) {
		ErrorMessageDto emsg = new ErrorMessageDto(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(emsg);
	}
	
}
