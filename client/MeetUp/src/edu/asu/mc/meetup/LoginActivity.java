package edu.asu.mc.meetup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	EditText et_email;
	EditText et_password;
	Button bt_submit;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		et_email = (EditText) findViewById(R.id.et_email);
		et_password = (EditText) findViewById(R.id.et_password);
		bt_submit = (Button) findViewById(R.id.bt_submit);
		
		
		bt_submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				StringBuilder stringBuilder = new StringBuilder();
		        HttpClient httpClient = new DefaultHttpClient();
		        HttpPost httpGet = new HttpPost("");
		        try {
		            HttpResponse response = httpClient.execute(httpGet);
		            StatusLine statusLine = response.getStatusLine();
		            int statusCode = statusLine.getStatusCode();
		            if (statusCode == 200) {
		                HttpEntity entity = response.getEntity();
		                InputStream inputStream = entity.getContent();
		                BufferedReader reader = new BufferedReader(
		                        new InputStreamReader(inputStream));
		                String line;
		                while ((line = reader.readLine()) != null) {
		                    stringBuilder.append(line);
		                }
		                inputStream.close();
		            } else {
		                Log.d("JSON", "Failed to download file");
		            }
		        } catch (Exception e) {
		            Log.d("readJSONFeed", e.getLocalizedMessage());
		        }        
		        System.out.println(stringBuilder.toString()); 
			}
		});
 	}

}
