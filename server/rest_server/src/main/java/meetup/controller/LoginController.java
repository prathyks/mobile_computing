package main.java.meetup.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.java.meetup.dao.UserinfoDao;
import main.java.meetup.daoimpl.UserinfoDaoImpl;
import main.java.meetup.domain.Userinfo;
import main.java.meetup.exception.ApiArgumentException;
import main.java.meetup.exception.ApiErrorCode;
import main.java.meetup.util.Validator;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController extends BaseController {

	static Logger log = Logger.getLogger(LoginController.class.getName());

	@RequestMapping(value="/signup",produces={MediaType.APPLICATION_JSON_VALUE},method=RequestMethod.POST)
	public ResponseEntity<Userinfo> signup(@RequestBody Userinfo user, HttpServletRequest request, 
			HttpServletResponse response) throws ApiArgumentException{		
		validateSignup(user);
		UserinfoDao udao = new UserinfoDaoImpl(request.getSession());
		user = udao.insertUser(user);
		createSession(user, request, response);
		return new ResponseEntity<Userinfo>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value="/login",produces={MediaType.APPLICATION_JSON_VALUE},method=RequestMethod.POST)
	public ResponseEntity<String> login(@RequestBody Userinfo user, HttpServletRequest request,
			HttpServletResponse response) throws ApiArgumentException{
		validateLogin(user, request.getSession());
		createSession(user, request, response);
		JSONObject json = new JSONObject();
		json.append("message", "Login Succesful");
		return new ResponseEntity<String>(json.toString(),HttpStatus.OK);
	}
	
	@RequestMapping(value="/session",produces={MediaType.APPLICATION_JSON_VALUE},method=RequestMethod.GET)
	public ResponseEntity<Userinfo> getSesstion(HttpServletRequest request,
			HttpServletResponse response) throws ApiArgumentException{
		if(!validateSession(request)){
			throw new ApiArgumentException(ApiErrorCode.LOGIN_ERROR, "User not logged in");
		}
		UserinfoDao udao = new UserinfoDaoImpl(request.getSession());
		Userinfo user = udao.getUser();
		return new ResponseEntity<Userinfo>(user,HttpStatus.OK);
	}
	
	@RequestMapping(value="/logout",produces={MediaType.APPLICATION_JSON_VALUE},method=RequestMethod.DELETE)
	public ResponseEntity<String> killSession(HttpServletRequest request,
			HttpServletResponse response){
		killSession(request);
		JSONObject json = new JSONObject();
		json.append("message", "Logout Succesful");
		return new ResponseEntity<String>(json.toString(),HttpStatus.OK);
	}

	private void validateLogin(Userinfo user, HttpSession httpSession) throws ApiArgumentException {
		Validator.validateEmail(user.getEmail());
		if(user.getPassword() == null || user.getPassword().length() == 0)
			throw new ApiArgumentException("Password cannot be empty");
		UserinfoDao udao = new UserinfoDaoImpl(httpSession);
		if(!udao.checkIfUserExists(user.getEmail())){
			throw new ApiArgumentException("User does not exist");
		}
	}

	private void validateSignup(Userinfo user) throws ApiArgumentException {
		Validator.validateEmail(user.getEmail());
		Validator.validateName(user.getName());
		Validator.validatePassword(user.getPassword());
		Validator.validatePhone(user.getPhone());
	}
}
