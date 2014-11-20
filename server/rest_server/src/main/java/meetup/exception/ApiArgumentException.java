package main.java.meetup.exception;

public class ApiArgumentException extends Exception{

	private static final long serialVersionUID = 1L;
	private ApiErrorCode err;
	public ApiArgumentException(ApiErrorCode err, String message) {
		super(message);
		this.setErr(err);
	}
	public ApiArgumentException(String message) {
		super(message);
		this.err = ApiErrorCode.BAD_REQUEST;
	}
	public ApiErrorCode getErr() {
		return err;
	}
	public void setErr(ApiErrorCode err) {
		this.err = err;
	}
	
	

}
