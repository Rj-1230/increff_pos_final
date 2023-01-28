package com.increff.pos.controller;

import com.increff.pos.model.MessageData;
import com.increff.pos.service.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppRestControllerAdvice {

	@ExceptionHandler(ApiException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageData handle(ApiException e) {
		MessageData data = new MessageData();
		data.setMessage(e.getMessage());
		return data;
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageData handle(Throwable e) {
		MessageData data = new MessageData();
		data.setMessage("An unknown error has occurred - " + e.getMessage());
		return data;
	}

//
//	@ExceptionHandler(Throwable.class)
//	@ResponseStatus(HttpStatus.FORBIDDEN)
//	public MessageData handleForbiddenException(Throwable accessDeniedException){
//		MessageData data = new MessageData();
//		data.setMessage("You are not authorized - " + accessDeniedException.getMessage());
//		System.out.println("hey not allowed");
//		return data;
//
//	}
//	@ExceptionHandler(CustomAccessDeniedHandler.class)
//	@ResponseStatus(HttpStatus.FORBIDDEN)
//	public MessageData handle(CustomAccessDeniedHandler e) {
//		MessageData data = new MessageData();
//		data.setMessage("The user is not given access. Please contact supervisor");
//		return data;
//	}
}