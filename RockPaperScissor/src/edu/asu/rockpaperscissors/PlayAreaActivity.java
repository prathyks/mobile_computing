package edu.asu.rockpaperscissors;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

public class PlayAreaActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.multitouch_gesture_view);
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				PlayAreaActivity.this);
 
			// set title
			alertDialogBuilder.setTitle("Are you sure, you want to exit?");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Click yes to exit!")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						//PlayAreaActivity.this.finish();	
//						finish();
//						Intent i = new Intent(PlayAreaActivity.this, MainActivity.class);
//						startActivity(i);
						
						Intent startMain = new Intent(Intent.ACTION_MAIN);
				        startMain.addCategory(Intent.CATEGORY_HOME);
				        startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//***Change Here***
				        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				        startActivity(startMain);
				        finish();
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
			
		return super.onKeyDown(keyCode, event);
	}
	
	

}
