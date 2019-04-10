package couponsystem.exeptions;

import org.springframework.http.HttpStatus;

public class CouponSystemException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private HttpStatus httpStatus;

	public CouponSystemException() {
		super();
	}

	public CouponSystemException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CouponSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public CouponSystemException(String message) {
		super(message);
	}

	public CouponSystemException(Throwable cause) {
		super(cause);
	}

	public CouponSystemException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	
}
