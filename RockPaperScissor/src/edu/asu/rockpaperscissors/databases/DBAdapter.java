package edu.asu.rockpaperscissors.databases;

import java.io.File;

import edu.asu.rockpaperscissors.MultiTouchGestureView.Choice;
import edu.asu.rockpaperscissors.ResultInfo;
import edu.asu.rockpaperscissors.Session;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class DBAdapter {

	Context context = null;
	private DatabaseHelper dbHelper = null;
	private SQLiteDatabase db = null;
	private static final String TAG=DBAdapter.class.getName();

	static final String className = DBAdapter.class.getName();
	static final String DATABASE_NAME = "rock_paper_scissors_db.db";
	static final String MOTHER_DATABASE_TABLE = "userInfo";
	static final int DATABASE_VERSION = 2;
	public static final String APPFOLDER=Environment.getExternalStorageDirectory()+"/RockPaperScissors/";
	Session session;

	public DBAdapter(Context context) {
		super();
		this.context = context;
		dbHelper = new DatabaseHelper(context);
		session = new Session(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, APPFOLDER+DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(UserRegistrationRegFields.USER_DETAILS_CREATE_TABLE_STATEMENT);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS '"+ UserRegistrationRegFields.USER_DETAILS_DATABASE_TABLE+"'");
			db.execSQL("DROP TABLE IF EXISTS '"+ UserRegistrationRegFields.GAME_DETAILS_DATABASE_TABLE+"'");
			db.execSQL(UserRegistrationRegFields.USER_DETAILS_CREATE_TABLE_STATEMENT);
			db.execSQL(UserRegistrationRegFields.GAME_DETAILS_CREATE_TABLE_STATEMENT);
		}

	}

	public boolean user_details_insert(String name, String user_name,
			String age, String gender, String win_count, String lost_count,
			String draw_count) {
		// TODO Auto-generated method stub

		boolean isInserted = false;

		try {
			db = dbHelper.getWritableDatabase();
			System.out.println("Database Opened");

			ContentValues initialValues = new ContentValues();

			initialValues.put(UserRegistrationRegFields.KEY_NAME, name);
			initialValues
			.put(UserRegistrationRegFields.KEY_USERNAME, user_name);
			initialValues.put(UserRegistrationRegFields.KEY_AGE, age);
			initialValues.put(UserRegistrationRegFields.KEY_GENDER, gender);

			long rows = db.insert(
					UserRegistrationRegFields.USER_DETAILS_DATABASE_TABLE,
					null, initialValues);
			Log.d(DBAdapter.class.getName(),"No of rows Inserted:" + rows);
			session.setusename(user_name);
			if (rows != -1) {
				System.out.println("values inserted to mother table");
				isInserted = true;
			}
			
		} catch (Exception e) {

			System.out.println("Error" + e.getLocalizedMessage());

		} finally {
			close(db);
		}
		return isInserted;
	}

	// a utility open method to open the database in writable mode
	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		System.out.println("Database Opened");
	}

	// a utility readDatabase method to open the database in readable mode
	public SQLiteDatabase getReadableDatabase() throws SQLException {
		db = dbHelper.getReadableDatabase();
		System.out.println("Database Opened in readable mode");
		return db;
	}

	// to close the DB
	public void close() {
		dbHelper.close();
		System.out.println("Database Closed");
	}

	public int getNumberOfUserByUsername(String username) {		
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM "
				+ UserRegistrationRegFields.USER_DETAILS_DATABASE_TABLE
				+ " WHERE " + UserRegistrationRegFields.KEY_USERNAME + " = ? ",new String[] { username });
		System.out.println("cursorGeneralDetails " + mCursor.getColumnCount());
		System.out.println("cursorGeneralDetails " + mCursor.getCount());
		close(db);
		return mCursor.getCount();
	}
	
	public int getNumberOfRowsByOpponent(String opponent){
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM "+
		UserRegistrationRegFields.GAME_DETAILS_DATABASE_TABLE
		+" WHERE "+UserRegistrationRegFields.KEY_OPPNAME
		+" = ?", new String[]{opponent});
		int res =mCursor.getCount(); 
		close(db);
		return res;
	}
	
	public int getNumberofUserByUsernameAtLogin(String username){
		db = dbHelper.getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM "
				+ UserRegistrationRegFields.USER_DETAILS_DATABASE_TABLE
				+ " WHERE " + UserRegistrationRegFields.KEY_USERNAME + " = ? ",new String[] { username });
		System.out.println("cursorGeneralDetails " + mCursor.getColumnCount());
		System.out.println("cursorGeneralDetails " + mCursor.getCount());
		close(db);
		if(mCursor.getCount() > 0)
			session.setusename(username);
		return mCursor.getCount();
	}

	public void updateResult(Choice userChoice,Boolean result,Choice comp,String opponent) {
		session.setResult(result);
		session.setCompChoice(comp.getStr());
		session.setUserChoice(userChoice.getStr());
		String username = session.getusename();
		Log.d(TAG,"result:"+result+"..compChoice:"+comp.getStr()+"..username:"+username+"..opponent:"+opponent);
		int rows =getNumberOfRowsByOpponent(opponent); 
		db = dbHelper.getWritableDatabase();
		if(rows == 0){
			String query = "INSERT INTO "+UserRegistrationRegFields.GAME_DETAILS_DATABASE_TABLE+
					" values(?,?,0,0,0)";
			db.execSQL(query,new String[]{username,opponent});
		}
		String updateField="";
		if(result == null)
			updateField=UserRegistrationRegFields.KEY_DRAW_COUNT;
		else if(result==true)
			updateField=UserRegistrationRegFields.KEY_WIN_COUNT;			
		else if(result == false)
			updateField=UserRegistrationRegFields.KEY_LOST_COUNT;
			
		String query="update "+UserRegistrationRegFields.GAME_DETAILS_DATABASE_TABLE
				+" set "+updateField+"="+updateField+"+1 where "+UserRegistrationRegFields.KEY_OPPNAME+"=?"
				+" and "+UserRegistrationRegFields.KEY_USERNAME+"=?;";
		db.execSQL(query,new String[]{opponent,username});
		close(db);
	}

	private void close(SQLiteDatabase db2) {
		db2.close();
		dbHelper.close();
	}

	public ResultInfo getResultForUser(String userName,String opponent) {
		String sql = "select "+UserRegistrationRegFields.KEY_WIN_COUNT+","+UserRegistrationRegFields.KEY_LOST_COUNT
				+","+UserRegistrationRegFields.KEY_DRAW_COUNT+" from "+UserRegistrationRegFields.GAME_DETAILS_DATABASE_TABLE
				+" where "+UserRegistrationRegFields.KEY_USERNAME+"=?"
				+" AND "+UserRegistrationRegFields.KEY_OPPNAME+"=?";
		Log.d(TAG,"getResultForUser:"+sql);
		db=dbHelper.getReadableDatabase();
		Cursor cur = db.rawQuery(sql,new String[]{userName,opponent});
		ResultInfo result = new ResultInfo();
		cur.moveToFirst();
		while(cur.isAfterLast()==false){
			result.setWins(cur.getInt(cur.getColumnIndex(UserRegistrationRegFields.KEY_WIN_COUNT)));
			result.setLoses(cur.getInt(cur.getColumnIndex(UserRegistrationRegFields.KEY_LOST_COUNT)));
			result.setDraws(cur.getInt(cur.getColumnIndex(UserRegistrationRegFields.KEY_DRAW_COUNT)));
			cur.moveToNext();
		}
		return result;
	}

}
