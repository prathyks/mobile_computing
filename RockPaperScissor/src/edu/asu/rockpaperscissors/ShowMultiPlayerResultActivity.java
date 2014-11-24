package edu.asu.rockpaperscissors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import edu.asu.rockpaperscissors.MultiTouchGestureView.Choice;
import edu.asu.rockpaperscissors.databases.DBAdapter;

public class ShowMultiPlayerResultActivity extends Activity{
	private static String TAG=ShowResultActivity.class.getName();
	Button bt_play_again;
	Button bt_exit;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		bt_exit= (Button) findViewById(R.id.bt_exit);
		bt_play_again = (Button) findViewById(R.id.bt_play_again);		
		Log.d(TAG,"In ShowMultiPlayerResultActivity");
		Bundle extras = getIntent().getExtras();
		Session session = new Session(this);
		Choice myChoice = setChoice(extras.getInt("myChoice"));
		Choice opponentChoice = setChoice(extras.getInt("opponentChoice"));
		Boolean result = winOrLose(myChoice, opponentChoice);		
		String userName = session.getusename();
		String opponentName= extras.getString("opponentName");	
		DBAdapter dba = new DBAdapter(this);
		dba.updateResult(myChoice, result, opponentChoice, opponentName);
		ResultInfo rinfo = dba.getResultForUser(userName,opponentName);
		String resultStr = null;
		if(result==null)
			resultStr="ITS A DRAW!!!";
		else if(result==true)
			resultStr="YOU WIN!!!";
		else if(result==false)
			resultStr="YOU LOSE!!!";
		((TextView)findViewById(R.id.key_result)).setText(resultStr);
		((TextView)findViewById(R.id.key_your_option)).setText("You Selected "+myChoice);
		((TextView)findViewById(R.id.key_comp_option)).setText(opponentName+" Selected "+opponentChoice);
		((TextView)findViewById(R.id.key_stats)).setText("Stats for "+userName+" vs "+opponentName);
		((TextView)findViewById(R.id.key_wins)).setText("Total Wins: "+rinfo.getWins());
		((TextView)findViewById(R.id.key_loss)).setText("Total Loses: "+rinfo.getLoses());
		((TextView)findViewById(R.id.key_draw)).setText("Total Draws: "+rinfo.getDraws());
		
		bt_play_again.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent();
				setResult(RESULT_OK,i);
				finish();
			}
		});
		
		bt_exit.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Intent startMain = new Intent(ShowMultiPlayerResultActivity.this,GameModeActivity.class);
				//startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        //startActivity(startMain);
				Intent intent = new Intent();
				setResult(RESULT_CANCELED,intent);
				finish();
			}
		});
	}
	
	public Choice setChoice(int i){
		Log.d(TAG, "setChoice:"+i);
		if(i==1)
			return Choice.ROCK;
		else if(i==2)
			return Choice.PAPER;
		else if(i==3)
			return Choice.SCISSOR;
		else return null;
	}
	
	private Boolean winOrLose(Choice c,Choice opp){		
		if(c==Choice.ROCK){
			if(opp==Choice.PAPER)
				return false;
			if(opp==Choice.SCISSOR)
				return true;
			else return null;
		}else if(c==Choice.PAPER){
			if(opp==Choice.ROCK)
				return true;
			if(opp==Choice.SCISSOR)
				return false;
			else return null;
		}else if(c==Choice.SCISSOR){
			if(opp==Choice.PAPER)
				return true;
			if(opp==Choice.ROCK)
				return false;
			else return null;
		}
		return null;
	}
}
