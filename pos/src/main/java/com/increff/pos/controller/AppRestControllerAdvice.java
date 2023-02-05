package com.increff.pos.controller;

import com.increff.pos.model.data.MessageData;
import com.increff.pos.api.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppRestControllerAdvice {

	@ExceptionHandler(ApiException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageData handle(ApiException e) {
		MessageData data = new MessageData();
		data.setMessage(e.getMessage());
		return data;
	}

	@ExceptionHandler(ResourceAccessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageData handleConnectionError(ResourceAccessException e) {
		MessageData data = new MessageData();
		data.setMessage("Connection to Invoice App refused");
		return data;
	}

	@ExceptionHandler(Throwable.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public MessageData handle(Throwable e) {
		MessageData data = new MessageData();
		data.setMessage("An unknown error has occurred - " + e.getMessage());
		return data;
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public MessageData handle(HttpMessageNotReadableException e) {
		MessageData data = new MessageData();
		data.setMessage("Invalid Input");
		return data;
	}


	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public final MessageData handleConstraintViolation(ConstraintViolationException ex) {
		List<String> details = ex.getConstraintViolations()
				.parallelStream()
				.map(e -> e.getPropertyPath() +" " + e.getMessage())
				.collect(Collectors.toList());
		MessageData data = new MessageData();
		data.setMessage(details.toString());
		return data;
	}
}