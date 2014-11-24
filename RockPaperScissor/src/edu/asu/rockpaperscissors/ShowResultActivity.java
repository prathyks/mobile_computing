package edu.asu.rockpaperscissors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.asu.rockpaperscissors.databases.DBAdapter;

public class ShowResultActivity extends Activity{

	private static String TAG=ShowResultActivity.class.getName();
	private static final int PROGRESS = 0x1;

	private ProgressBar mProgress;
	private int mProgressStatus = 0;
	Button bt_play_again;
	Button bt_exit;
	private Handler mHandler = new Handler();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_result);
		bt_exit= (Button) findViewById(R.id.bt_exit);
		bt_play_again = (Button) findViewById(R.id.bt_play_again);
		//launchDummyRingDialog(getCurrentFocus());		
		Log.d(TAG,"In ShowResultActivity");
		Session session = new Session(this);
		Boolean result = session.getResult();
		String userChoice = session.getUserChoice();
		String compChoice = session.getCompChoice();
		String userName = session.getusename();
		String resultStr = null;
		DBAdapter dba = new DBAdapter(this);
		ResultInfo rinfo = dba.getResultForUser(userName,"computer");
		if(result==null)
			resultStr="ITS A DRAW!!!";
		else if(result==true)
			resultStr="YOU WIN!!!";
		else if(result==false)
			resultStr="YOU LOSE!!!";
		((TextView)findViewById(R.id.key_result)).setText(resultStr);
		((TextView)findViewById(R.id.key_your_option)).setText("You Selected "+userChoice);
		((TextView)findViewById(R.id.key_comp_option)).setText("Computer Selected "+compChoice);
		((TextView)findViewById(R.id.key_stats)).setText("Stats for "+userName+" vs Computer");
		((TextView)findViewById(R.id.key_wins)).setText("Total Wins: "+rinfo.getWins());
		((TextView)findViewById(R.id.key_loss)).setText("Total Loses: "+rinfo.getLoses());
		((TextView)findViewById(R.id.key_draw)).setText("Total Draws: "+rinfo.getDraws());	
		
		bt_play_again.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				Intent i = new Intent(ShowResultActivity.this, PlayAreaActivity.class);
//				startActivity(i);
				
				finish();
			}
		});
		
		bt_exit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent startMain = new Intent(ShowResultActivity.this,GameModeActivity.class);
				startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		        startActivity(startMain);
			}
		});
	}

	public void launchDummyRingDialog(View view) {
		final ProgressDialog ringProgressDialog = ProgressDialog.show(ShowResultActivity.this, "Computer Playing...", 
				"Computer Playing...", true);
		ringProgressDialog.setCancelable(true);		
				try {
					Thread.sleep(200);
				} catch (Exception e) {
					System.out.println("exception : "+e.getLocalizedMessage());
				}
				ringProgressDialog.dismiss();			
	}

}

