package main.java.meetup.exception;

import java.sql.SQLException;

import org.json.JSONObject;
import org.springframework.boot.json.JsonParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(value = { java.sql.SQLException.class })
	protected ResponseEntity<Object> handleSqlConflict(SQLException ex, WebRequest request){
		String bodyOfResponse = "Something went wrong";
		return handleExceptionInternal(ex, bodyOfResponse, 
		          new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);		
	}
	
	@ExceptionHandler(value = {ApiArgumentException.class})
	protected ResponseEntity<Object> handleApiArgument(ApiArgumentException ex, WebRequest request){
		JSONObject json = new JSONObject();
		json.append("message", ex.getMessage());
		if(ex.getErr().equals(ApiErrorCode.LOGIN_ERROR)){
			return handleExceptionInternal(ex, json.toString(), 
			          new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);	
		}
		return handleExceptionInternal(ex, json.toString(), 
		          new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	
}
