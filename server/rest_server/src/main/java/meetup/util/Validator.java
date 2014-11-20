package main.java.meetup.util;

import main.java.meetup.exception.ApiArgumentException;

import org.apache.commons.validator.routines.EmailValidator;

public class Validator {
	private static EmailValidator eval = EmailValidator.getInstance();
	private static int NAME_LENGHT_MAX = 128;
	
	public static void validateEmail(String email) throws ApiArgumentException{
		if(email == null || email.trim().isEmpty())
			throw new ApiArgumentException("Email cannot be empty");
		if(!eval.isValid(email.trim())){
			throw new ApiArgumentException("Invalid email");
		}
	}
	
	public static void validateName(String name) throws ApiArgumentException{
		if(name == null || name.trim().isEmpty())
			throw new ApiArgumentException("Name cannot be empty");
		if(name.trim().length() > NAME_LENGHT_MAX)
			throw new ApiArgumentException("Name cannot exceed "+NAME_LENGHT_MAX+" characters");
		if(!name.matches("[a-zA-Z ]+")){
			throw new ApiArgumentException("Name can have only alphabetic characters");
		}
	}
	
	public static void validatePassword(String pass) throws ApiArgumentException{
		if(pass == null || pass.length() < 6)
			throw new ApiArgumentException("Password cannot be shorted than 6 characters");
		if(pass.length() > 32)
			throw new ApiArgumentException("Password cannot be shorted than 32 characters");				
	}

	public static void validatePhone(String phone) throws ApiArgumentException {
		if(phone == null || phone.trim().length() < 10 || phone.trim().length() < 10)
			throw new ApiArgumentException("Phone number should be 10 digits long");
		if(!phone.matches("[0-9]+"))
			throw new ApiArgumentException("Phone number not valid");
				
	}
	
}
