package com.increff.invoice_app.controller;

import com.increff.invoice_app.model.MessageData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppRestControllerAdvice {

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageData handle(Throwable e) {
		MessageData data = new MessageData();
		data.setMessage("An unknown error has occurred - " + e.getMessage());
		return data;
	}

	@ExceptionHandler(ConnectException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageData handleConnectionError(ConnectException e) {
		MessageData data = new MessageData();
		data.setMessage("Connection refused");
		return data;
	}
}