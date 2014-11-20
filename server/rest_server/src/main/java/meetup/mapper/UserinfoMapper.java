package main.java.meetup.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.meetup.domain.Userinfo;

import org.springframework.jdbc.core.RowMapper;

public class UserinfoMapper implements RowMapper<Userinfo>{

	public Userinfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		Userinfo user = new Userinfo();
		user.setEmail(rs.getString("email"));
		user.setName(rs.getString("name"));
		user.setId(rs.getInt("id"));
		user.setPhone(rs.getString("phone"));				
		return user;
	}

}
