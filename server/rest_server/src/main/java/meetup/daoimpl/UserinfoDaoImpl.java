package main.java.meetup.daoimpl;

import javax.servlet.http.HttpSession;

import main.java.meetup.dao.BaseDao;
import main.java.meetup.dao.UserinfoDao;
import main.java.meetup.domain.Userinfo;
import main.java.meetup.exception.ApiArgumentException;
import main.java.meetup.exception.ApiErrorCode;
import main.java.meetup.mapper.UserinfoMapper;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserinfoDaoImpl extends BaseDao implements UserinfoDao{

	Logger log = Logger.getLogger(UserinfoDaoImpl.class.getName());

	public UserinfoDaoImpl(HttpSession httpSession) {
		super(httpSession);	
	}
	
	public Userinfo insertUser(Userinfo user) throws ApiArgumentException {		
		if(checkIfUserExists(user.getEmail())){
			throw new ApiArgumentException("User already exists!");
		}				
		String sql = "insert into userinfo(name,email,password,phone) "
				+ "values ("
				+ "'"+user.getName()+"',"
				+ "'"+user.getEmail()+"',"
				+ "'"+user.getPassword()+"',"
				+ "'"+user.getPhone()+""
				+ "')";
		log.debug("insertUser:"+sql);
		JdbcTemplate jtemp = getJdbcTemplate();
		if(jtemp == null)
			log.debug("jtemp is null");
		getJdbcTemplate().update(sql);	
		return getUserByEmail(user.getEmail());
	}

	public Userinfo getUserByEmail(String email) {
		String sql = "select id, name, email, phone from userinfo where email = ?;";
		log.debug("getUserByEmail:"+sql);
		Userinfo user = getJdbcTemplate().queryForObject(sql, new UserinfoMapper() ,email);
		return user;
	}

	public boolean checkIfUserExists(String email) {
		String sql = "select count(email) from userinfo where email = ?";
		int rowCount = getJdbcTemplate().queryForObject(sql, Integer.class,email);
		if(rowCount > 0)
			return true;
		else return false;
	}

	public Userinfo updateUser(int id, Userinfo user) {
		// TODO Auto-generated method stub
		return null;
	}

	public Userinfo getUserInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public Userinfo getUser() throws ApiArgumentException {
		if(getContext()==null)
			throw new ApiArgumentException(ApiErrorCode.LOGIN_ERROR, "User not logged in");
		String email = getContext().getEmail();
		return getUserByEmail(email);
	}

}
