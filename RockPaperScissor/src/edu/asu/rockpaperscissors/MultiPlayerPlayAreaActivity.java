package edu.asu.rockpaperscissors;

import edu.asu.rockpaperscissors.BluetoothGameService;
import edu.asu.rockpaperscissors.MultiTouchGestureView.Choice;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

public class MultiPlayerPlayAreaActivity extends Activity{
	
	final int GET_CONNECTION=1;
	final int RETURN_SHOWRESULT=2;
	BluetoothAdapter btAdp;
	BluetoothGameService mGameService;
	// Message types sent from the BluetoothGameService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_CONN_LOST = 6;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    
    boolean myPlayComplete = false;
    boolean opponentPlayComplete = false;
    Choice myChoice;
    Choice opponentChoice;
    String opponentName;
    ProgressDialog joinWait;
    ProgressDialog playWait;
    String mConnectedDeviceName=null;
    final String TAG = MultiPlayerPlayAreaActivity.class.getName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BluetoothAdapter btAdp = BluetoothAdapter.getDefaultAdapter();
		setContentView(R.layout.multiplayer_multitouch_gesture_view);
		joinWait = new ProgressDialog(this);
		joinWait.setTitle("Waiting");
		joinWait.setMessage("Waiting for other player to join..");
		playWait = new ProgressDialog(this);
		playWait.setTitle("Waiting");
		playWait.setMessage("Waiting for other player to play..");
		joinWait.show();
		Intent i = new Intent(MultiPlayerPlayAreaActivity.this, BtActivity.class);
		startActivityForResult(i, GET_CONNECTION);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == GET_CONNECTION){
			if (resultCode == Activity.RESULT_OK) {
//				String address = data.getExtras().getString("deviceaddr");
//                BluetoothDevice device = btAdp.getRemoteDevice(address);
                BluetoothDevice device = (BluetoothDevice) data.getExtras().get("device");
                if (mGameService == null)
                	mGameService = new BluetoothGameService(getApplicationContext(), mHandler);
                mGameService.connect(device);
			}
		}else if(requestCode == RETURN_SHOWRESULT){
			if(resultCode == Activity.RESULT_OK){
				Log.d(TAG,"Play again");
				
			}else if(resultCode == Activity.RESULT_CANCELED){
				mGameService.stop();
				exitGame();
				Log.d(TAG,"Exit multiplayer game");
			}
		}
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MultiPlayerPlayAreaActivity.this);
 
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
	
	private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
        	Log.d(TAG, "mHandler msg:"+msg.what);
			switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
            	Log.d(TAG,"STATE_CHANGE:"+msg.arg1);
                switch (msg.arg1) {
                case BluetoothGameService.STATE_CONNECTED:
                    break;
                case BluetoothGameService.STATE_CONNECTING:
                    break;
                case BluetoothGameService.STATE_LISTEN:
                	Toast.makeText(getApplicationContext(), "Waiting for other device..", Toast.LENGTH_SHORT).show();
                	break;
                case BluetoothGameService.STATE_NONE:
                    break;
                }
                break;
            case MESSAGE_WRITE:
            	byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                //Toast.makeText(getApplicationContext(), "You played "+ writeMessage, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_READ:
            	byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
//            	Toast.makeText(getApplicationContext(), "Opponent played "
//                        + readMessage, Toast.LENGTH_SHORT).show();
                String[] parsed_mess = readMessage.split(",");
                int choice = Integer.parseInt(parsed_mess[1]);
                opponentName = parsed_mess[0];
                opponentChoice = setChoice(choice);
                opponentPlayComplete = true;
                if(opponentPlayComplete && myPlayComplete){
                	if(playWait.isShowing())
                		playWait.dismiss();
                	showResult();                    	
                }            	
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                joinWait.dismiss();
                break;
            case MESSAGE_CONN_LOST:
            	Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                        Toast.LENGTH_SHORT).show();
            	exitGame();
            	break;            	
            case MESSAGE_TOAST:
//                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
//                               Toast.LENGTH_SHORT).show();
            	Log.d(TAG,"NOT GOOD 5");
                break;
            }
        }
    };
    
    private void exitGame() {
		finish();			
	}
    
    public void sendMessage(String message) {
        // Check that we're actually connected before trying anything
    	Log.d(TAG, message);
        if (mGameService.getState() != BluetoothGameService.STATE_CONNECTED) {
            Toast.makeText(this, "Device not connected", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mGameService.write(send);
            myPlayComplete = true;
            Log.d(TAG,message);
            String[] parsed_mess = message.split(",");
            Log.d(TAG,parsed_mess[1]);
            int choice = Integer.parseInt(parsed_mess[1]);
            myChoice = setChoice(choice);
            if(opponentPlayComplete && myPlayComplete){
            	if(playWait.isShowing())
            		playWait.dismiss();
            	showResult();            	
            }            	
            else{
            	if(!playWait.isShowing())
            		playWait.show();
            }
            	
        }
    }

	private void showResult() {
		//String str = "Opponent "+opponentName+" played "+ opponentChoice.str +". You played "+ myChoice.str;		
		//Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();		
		opponentPlayComplete=false;
		myPlayComplete=false;
		Intent intent = new Intent(getApplicationContext(), ShowMultiPlayerResultActivity.class);
		intent.putExtra("opponentName", opponentName);
		intent.putExtra("myChoice", myChoice.val);
		intent.putExtra("opponentChoice", opponentChoice.val);
		startActivityForResult(intent, RETURN_SHOWRESULT);
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
}
