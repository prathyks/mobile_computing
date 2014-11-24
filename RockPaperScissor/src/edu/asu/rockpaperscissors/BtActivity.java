package edu.asu.rockpaperscissors;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import edu.asu.rockpaperscissors.BluetoothGameService;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class BtActivity extends Activity{

	String TAG = BtActivity.class.getName();
	SensorManager sensorManager = null;
	BluetoothAdapter btAdp = BluetoothAdapter.getDefaultAdapter();
	Set<BluetoothDevice> pairedDevices;
	ArrayList<BluetoothDevice> pairedDeviceArray = new ArrayList<BluetoothDevice>();
	ArrayList<BluetoothDevice> nearbyDevices = new ArrayList<BluetoothDevice>();
	private BluetoothGameService mGameService = null;
	private static final int REQUEST_ENABLE_BT = 2;
	ArrayAdapter<String> btDeviceArrayAdapter;
	ArrayAdapter<String> btPairedDevicesAdapter;
	//DeviceListAdapter btDeviceArrayAdapter;
	//DeviceListAdapter btPairedDevicesAdapter;
	
	// Message types sent from the BluetoothGameService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    private String mConnectedDeviceName = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(btAdp == null){
			Toast.makeText(BtActivity.this, "No BlueTooth adapter found", Toast.LENGTH_LONG).show();			
		}
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		setContentView(R.layout.bt_find_connect);		
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (!btAdp.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
		Button discoverBtn = (Button) findViewById(R.id.discover_btn);
		discoverBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				find(v);
			}
		});
		
		ListView btDeviceList = (ListView) findViewById(R.id.bt_device_list);
		btDeviceArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		//btDeviceArrayAdapter = new DeviceListAdapter(this, R.id.bt_device_list);
		btDeviceList.setAdapter(btDeviceArrayAdapter);
		
		ListView btPairedList = (ListView) findViewById(R.id.bt_paired_list);
		btPairedDevicesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		//btPairedDevicesAdapter = new DeviceListAdapter(this,R.id.bt_paired_list);
		btPairedList.setAdapter(btPairedDevicesAdapter);
		
		Log.d(TAG,"Registering List item onclick listener");
		
		btDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg) {
				BluetoothDevice device = nearbyDevices.get(position);
				String str = "Pairing with "+device.getName();
				Log.d(TAG, str);
				pairDevice(device);
			}
		});
		
		btPairedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position,
					long arg) {
				BluetoothDevice device = pairedDeviceArray.get(position);
				String str = "Connect to "+device.getName();
				Log.d(TAG, str);
				Intent intent = new Intent();
				intent.putExtra("device", device);
				setResult(RESULT_OK,intent);
				finish();
				//mGameService = new BluetoothGameService(getApplicationContext(), mHandler);
				//mGameService.connect(device);
			}
		});
		
		Button pairedListBtn = (Button) findViewById(R.id.list_paired_btn);
		pairedListBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listPaired(v);
			}
		});
	}

	protected void listPaired(View v) {
		pairedDevices = btAdp.getBondedDevices();
		btPairedDevicesAdapter.clear();
		pairedDeviceArray.clear();
		for(BluetoothDevice device : pairedDevices){
			btPairedDevicesAdapter.add(device.getName() + '\n' + device.getAddress());
			pairedDeviceArray.add(device);
		}			
		//btPairedDevicesAdapter.addDevice(device);		
		Toast.makeText(getApplicationContext(), "Showing Paired devices", Toast.LENGTH_SHORT).show();
	}

	protected void find(View v) {
		Button discoverBtn = (Button) findViewById(R.id.discover_btn);
		if(btAdp.isDiscovering()){
			btAdp.cancelDiscovery();
			discoverBtn.setText("Discover");
		}else{
			btDeviceArrayAdapter.clear();
			nearbyDevices.clear();
			btAdp.startDiscovery();
			discoverBtn.setText("Stop Discovery");
			registerReceiver(bReceiver,new IntentFilter(BluetoothDevice.ACTION_FOUND));
			Toast.makeText(getApplicationContext(), "Shwoing Nearby devices", Toast.LENGTH_SHORT).show();;
		}		
	}
	
	BroadcastReceiver bReceiver = new BroadcastReceiver() {		
		@Override
		public void onReceive(Context context, Intent intent) {	
			String action = intent.getAction();
			if(BluetoothDevice.ACTION_FOUND.equals(action)){
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				nearbyDevices.add(device);
				btDeviceArrayAdapter.add(device.getName() + '\n' + device.getAddress());
				//btDeviceArrayAdapter.addDevice(device);
				btDeviceArrayAdapter.notifyDataSetChanged();
			}
		}
	};
	
	private void pairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("createBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	// The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                switch (msg.arg1) {
                case BluetoothGameService.STATE_CONNECTED:
                    break;
                case BluetoothGameService.STATE_CONNECTING:
                    break;
                case BluetoothGameService.STATE_LISTEN:
                	break;
                case BluetoothGameService.STATE_NONE:
                    break;
                }
                break;
            case MESSAGE_WRITE:
            	byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                Toast.makeText(getApplicationContext(), "You played "
                        + writeMessage, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_READ:
            	byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
            	Toast.makeText(getApplicationContext(), "Opponent played "
                        + readMessage, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                Toast.makeText(getApplicationContext(), "Connected to "
                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };
}
