package edu.asu.rockpaperscissors.databases;



public class UserRegistrationRegFields {
	
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	//public static final String OPPONENT_NAME = "name";
	public static final String KEY_USERNAME = "user_name";
	public static final String KEY_OPPNAME = "opp_name";
	public static final String KEY_AGE = "age";
	public static final String KEY_GENDER = "gender";
	public static final String KEY_WIN_COUNT = "win_count";
	public static final String KEY_LOST_COUNT = "lost_count";
	public static final String KEY_DRAW_COUNT = "draw_count";
	
	static final String USER_DETAILS_DATABASE_TABLE = "USER_DETAILS";
	static final String GAME_DETAILS_DATABASE_TABLE = "GAME_DETAILS";
	
//	+ KEY_ROWID
//	+ " integer primary key autoincrement not null, "
//	
	static final String USER_DETAILS_CREATE_TABLE_STATEMENT = "create table "
			+ USER_DETAILS_DATABASE_TABLE
			+ "("
			+ KEY_NAME
			+ " text not null, "
			+ KEY_USERNAME
			+ " text primary key not null, "
			+ KEY_AGE
			+ " text not null, "
			+ KEY_GENDER
			+ " text not null "
			+ "); ";
	
	static final String GAME_DETAILS_CREATE_TABLE_STATEMENT = "create table "
			+GAME_DETAILS_DATABASE_TABLE
			+" ("
			+KEY_USERNAME
			+" text not null,"
			+KEY_OPPNAME
			+" text not null,"
			+KEY_WIN_COUNT
			+" integer,"
			+KEY_LOST_COUNT
			+" integer,"
			+KEY_DRAW_COUNT
			+" integer"
			+ ");";
	
}
