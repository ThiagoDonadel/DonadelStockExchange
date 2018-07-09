package br.com.donadel.exception.handler;

import org.springframework.http.HttpStatus;

public class RequestError {

	private HttpStatus status;
	private String errorCode;
	private String message;

	public RequestError(HttpStatus status, String errorCode, String message) {
		super();
		this.status = status;
		this.errorCode = errorCode;
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static RequestError buildRequestError(HttpStatus status, String errorCode, String message) {
		return new RequestError(status, errorCode, message);
	}
	
	

}
