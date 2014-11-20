package main.java.meetup.dao;

import main.java.meetup.domain.Userinfo;
import main.java.meetup.exception.ApiArgumentException;

public interface UserinfoDao {

	public Userinfo insertUser(Userinfo user) throws ApiArgumentException;
	public Userinfo updateUser(int id, Userinfo user);
	public Userinfo getUserInfo();
	public boolean checkIfUserExists(String email);
	public Userinfo getUserByEmail(String email);
	public Userinfo getUser() throws ApiArgumentException;	
}
