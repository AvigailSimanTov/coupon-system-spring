package couponsystem.exeptions.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import couponsystem.exeptions.CouponSystemException;

@ControllerAdvice
public class RestExceptionHandler {
	

	private HttpStatus httpStatus;

	@ExceptionHandler(Throwable.class)
	public ResponseEntity<Object> handleTrowable(Throwable throwable) {
		httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		ApiErrorEntity apiError = new ApiErrorEntity(httpStatus, "Internal server error");
		throwable.printStackTrace();
		return new ResponseEntity<Object>(apiError, httpStatus);
	}

	@ExceptionHandler(CouponSystemException.class)
	public ResponseEntity<Object> handleCouponSystemException(CouponSystemException exception) {

		httpStatus = exception.getHttpStatus();

		if (httpStatus == null) {
			httpStatus = HttpStatus.BAD_REQUEST;
		}

		ApiErrorEntity apiError = new ApiErrorEntity(httpStatus, exception.getMessage());
		return new ResponseEntity<Object>(apiError, httpStatus);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleApplicationException(MethodArgumentTypeMismatchException exception) {
		httpStatus = HttpStatus.BAD_REQUEST;
		ApiErrorEntity apiError = new ApiErrorEntity(httpStatus, exception.getValue() + " is an illegal value for "
				+ exception.getName() + ". A value of type " + exception.getRequiredType() + " is expected.");
		return new ResponseEntity<Object>(apiError, httpStatus);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
		httpStatus = HttpStatus.BAD_REQUEST;

		ApiErrorEntity apiError = new ApiErrorEntity(httpStatus, "Wrong or invalid data type in fields");
		return new ResponseEntity<Object>(apiError, httpStatus);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException exception) {
		httpStatus = HttpStatus.METHOD_NOT_ALLOWED;

		ApiErrorEntity apiError = new ApiErrorEntity(httpStatus,
				"Request method " + exception.getMethod()
						+ " is not supported by this service. The methods supported are : "
						+ exception.getSupportedHttpMethods());
		return new ResponseEntity<Object>(apiError, httpStatus);
	}

}
