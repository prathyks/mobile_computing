package edu.asu.rockpaperscissors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class GameModeActivity extends Activity{
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_mode);
		Button singleButton = (Button) findViewById(R.id.single_button);
		Button multiButton = (Button) findViewById(R.id.multi_button);
		
		singleButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(GameModeActivity.this, PlayAreaActivity.class);
				startActivity(i);
			}
		});
		
		multiButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Intent i = new Intent(GameModeActivity.this, BtActivity.class);
				Intent i = new Intent(GameModeActivity.this, MultiPlayerPlayAreaActivity.class);
				startActivity(i);
			}
		});
	}
	
	

}
