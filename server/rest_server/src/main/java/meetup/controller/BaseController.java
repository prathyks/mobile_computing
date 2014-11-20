package main.java.meetup.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import main.java.meetup.domain.Userinfo;

public class BaseController {
	
	protected boolean validateSession(HttpServletRequest request) {
		if(request.getSession().getAttribute("email") == null)
			return false;
		return true;		
	}
	
	protected void createSession(Userinfo user, HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.setAttribute("id", user.getId());
		session.setAttribute("email", user.getEmail());
	}
	
	protected void killSession(HttpServletRequest request){
		if(request.getSession() != null){
			request.getSession().invalidate();
		}			
	}

}
