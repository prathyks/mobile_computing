package main.java.meetup.dao;

import javax.servlet.http.HttpSession;

import main.java.meetup.domain.AppContext;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.mysql.jdbc.Driver;


public class BaseDao{
	
	private static String DB_NAME = "meetupdb";
	private static String DB_USER = "meetup";
	private static String DB_PASS = "pass";
	private static String DB_LOC = "loclhost";
	private static String DB_URL = "jdbc:mysql://localhost:3306/meetupdb";
	JdbcTemplate jdbcTemplate=null;
	private AppContext context;
	
	SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
	
	public BaseDao(HttpSession httpSession) {
		if(jdbcTemplate == null){
			this.dataSource.setDriverClass(Driver.class);
			dataSource.setUsername(DB_USER);
			dataSource.setUrl(DB_URL);
			dataSource.setPassword(DB_PASS);		
			this.jdbcTemplate = new JdbcTemplate(dataSource);	
		}
		if(getContext() == null){
			setContext(new AppContext());
			getContext().setId((Integer)httpSession.getAttribute("id"));
			getContext().setEmail((String)httpSession.getAttribute("email"));
		}
	}
	
	protected JdbcTemplate getJdbcTemplate() {
		if(this.jdbcTemplate == null){
			createJdbcTemplate();
			return this.jdbcTemplate;
		}			
		else
			return this.jdbcTemplate;
	}
	
	private void createJdbcTemplate(){
		this.dataSource.setDriverClass(Driver.class);
		dataSource.setUsername(DB_USER);
		dataSource.setUrl(DB_URL);
		dataSource.setPassword(DB_PASS);		
		this.jdbcTemplate = new JdbcTemplate(dataSource); 
	}

	public AppContext getContext() {
		return context;
	}

	public void setContext(AppContext context) {
		this.context = context;
	}

}
