package br.com.donadel.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.donadel.exception.account.AccountException;

@RestControllerAdvice
public class RestRequestHandler {
	
	@ExceptionHandler(AccountException.class)
	public RequestError AccountExceptionHandler( AccountException accountException ) {
		return RequestError.buildRequestError(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", accountException.getMessage());
	}

}
