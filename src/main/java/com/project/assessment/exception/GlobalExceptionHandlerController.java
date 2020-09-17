package com.project.assessment.exception;

import java.io.IOException;
import java.security.InvalidParameterException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.project.assessment.response.BaseResponse;
import com.project.assessment.type.ErrorType;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler  {

	

	@ExceptionHandler(SystemErrorException.class)
	public final ResponseEntity<BaseResponse> handleSystemErrorExceptions(SystemErrorException ex, WebRequest request) {
		log.error("ERROR  :", ex);
		BaseResponse exceptionResponse = new BaseResponse("GENERAL_SYSTEM_ERROR", ex.getMessage());
		return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(InvalidParameterException.class)
	public final ResponseEntity<BaseResponse> handleNotValidArgumentException(InvalidParameterException ex, WebRequest request) {
		log.error("ERROR  :", ex);
		BaseResponse response = new BaseResponse("INVALID_PARAMETER", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<Object> handleAccessDeniedException(HttpServletResponse res, AccessDeniedException e) throws IOException {
		log.error("ERROR  :", e);
		BaseResponse response = new BaseResponse("UNAUTHORIZED", "Unauthorized");
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public final ResponseEntity<BaseResponse> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
		log.error("ERROR UNAUTHORIZED : ", ex);
		BaseResponse exceptionResponse = new BaseResponse("UNAUTHORIZED", "Unauthorized");
		return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleException(HttpServletResponse res, Exception e) throws IOException {
		log.error("ERROR :", e);
		BaseResponse response = new BaseResponse("GENERAL_SYSTEM_ERROR", "General System Error");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

}
