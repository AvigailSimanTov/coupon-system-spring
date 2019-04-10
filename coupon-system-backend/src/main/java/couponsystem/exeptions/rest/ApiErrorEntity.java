package couponsystem.exeptions.rest;

import org.springframework.http.HttpStatus;

public class ApiErrorEntity {

	private HttpStatus errorCode;
	private String message;
	public ApiErrorEntity(HttpStatus errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}
	public HttpStatus getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(HttpStatus errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "ApiError [errorCode=" + errorCode + ", message=" + message + "]";
	}
	
}
