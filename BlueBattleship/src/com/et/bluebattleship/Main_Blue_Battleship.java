package com.et.bluebattleship;

import java.util.List;
import java.util.Set;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main_Blue_Battleship extends Activity {

	//	 Debugging
	private static final String TAG = "BlueBattleship";
	private static final boolean D = true;	// set true if debugging

	//     Message types sent from the BlueBattleshipService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;

	//     Key names received from the BlueBattleshipService Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	//     Intent request codes
	private static final int REQUEST_CONNECT_DEVICE = 1;
	//    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	private static final int REQUEST_ENABLE_BT = 2;

	//     Name of the connected device
	private String connectedDeviceName = null;
	//     String buffer for outgoing messages
	private StringBuffer mOutStringBuffer;
	//     Local Bluetooth adapter
	private BluetoothAdapter bluetoothAdapter = null;
	//     Member object for the chat services
	private BlueBattleshipService blueBattleshipService = null;

	//	private static final String BLUETOOTH_APP_NAME = "blue_battleship";

//		 Unique UUID for this application
//	private static final UUID MY_UUID =
//			UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
//	private static final UUID MY_UUID_INSECURE =
//			UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

//	    GUI elements
//	private Button btActBT;
//	private Button btDeactBT;
	private Button btSearchDevices;
	private Button btSend;

	private ToolBox toolBox;

	//    visible devices
	private List<BluetoothDevice> bluetoothDevices;
	private ListView lvDevices;
	private DeviceAdapter deviceAdapter;

//	has the aim to add to the list of visible devices another bt device
	final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				 if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					 bluetoothDevices.add(device);
				 }
				refreshBluetoothDevices();
			}
			
			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//			TODO progress bar o qualcosa di simile da togliere
				bbToast("Ricerca finita");
			}
		}
	};

//	  The Handler that gets information back from the BluetoothChatService
	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
				switch (msg.arg1) {
				case BlueBattleshipService.STATE_CONNECTED:
				case BlueBattleshipService.STATE_CONNECTING:
				case BlueBattleshipService.STATE_LISTEN:
				case BlueBattleshipService.STATE_NONE:
				}
				break;
			case MESSAGE_WRITE:
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				switch (readBuf[0]) {
				case 2:
					boolean[] tmpEnemyField = new boolean[100];
					for (int i = 1; i < 101; i++) {
						if (readBuf[i] == 1) {
							tmpEnemyField[i - 1] = true;
						} else {
							tmpEnemyField[i - 1] = false;
						}
					}
					toolBox.enemy_field = tmpEnemyField;
					break;
					
				case 3:
					break;
					
				case 4:
//					 construct a string from the valid bytes in the buffer
					String readMessage = new String(readBuf, 0, msg.arg1);
					Log.i(TAG, readMessage);
					bbToast(readMessage);
					break;

				default:
					break;
				}
				
				break;
			case MESSAGE_DEVICE_NAME:
//				 save the connected device's name
				connectedDeviceName = msg.getData().getString(DEVICE_NAME);
				bbToast("Connected with " + connectedDeviceName);
				break;
			case MESSAGE_TOAST:
				bbToast(msg.getData().getString(TOAST));
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		toolBox=ToolBox.getInstance();
		if(D) Log.i(TAG, "ON CREATE");
		setContentView(R.layout.activity_main);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (bluetoothAdapter == null) { // bluetooth not supported
			bbToast("Ops! Il tuo dispositivo non supporta il Bluetooth!");
//			finish();	// faccio terminare l'applicazione?
			return;
		}

		lvDevices = (ListView)findViewById(R.id.lvDevices);
		lvDevices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
				bluetoothAdapter.cancelDiscovery();
				BluetoothDevice bluetoothDevice = bluetoothDevices.get(position);

				

				blueBattleshipService.connect(bluetoothDevice);

				toolBox.primo=true;
			}

		});
		//			lvDevices.setAdapter(deviceAdapter);

//		btActBT = (Button)findViewById(R.id.btActBT);
//		btDeactBT = (Button)findViewById(R.id.btDeactBT);
		btSearchDevices = (Button)findViewById(R.id.btSearchDevices);
		btSend = (Button)findViewById(R.id.btSend);

		//vado al campo
		Button Campo=(Button)findViewById(R.id.startCampo);
		Campo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				runCampo();	
			}
		});

//		btActBT.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v){
//				ensureDiscoverable();
//				//				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//				//				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//				//				startActivityForResult(discoverableIntent, REQUEST_ENABLE_BT);
//				btActBT.setEnabled(false);
//				btDeactBT.setEnabled(true);
//			}
//
//		});

//		btDeactBT.setOnClickListener(new OnClickListener(){
//			@Override
//			public void onClick(View v){
//				bluetoothAdapter.cancelDiscovery();
//				bluetoothAdapter.disable();
//				btActBT.setEnabled(true);
//				btDeactBT.setEnabled(false);
//			}
//		});
		
//		btActBT.setEnabled(false);
//		btDeactBT.setEnabled(false);

		btSearchDevices.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				bluetoothDevices = new Vector<BluetoothDevice>();
				deviceAdapter = new DeviceAdapter(Main_Blue_Battleship.this);
				lvDevices.setAdapter(deviceAdapter);
				
		        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
//		         If there are paired devices, add each one to the ArrayAdapter
		        if (pairedDevices.size() > 0) {
		            for (BluetoothDevice device : pairedDevices) {
		                bluetoothDevices.add(device);
		            }
		        }
				
//				mi rendo visibile...
				ensureDiscoverable();
//				...e comincio a cercare
				startDiscovery();
//				FIXME non dovrei fare questo nell'accept thread del service???
			}
		});

		btSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				String totCuloCane = "Sti cazzi! Enea, ora fammi sta bocca che ti fecondo l'intestino tenue!";
				String totCuloCane = "hello";
//				byte[] bytes = totCuloCane.getBytes();
//				blueBattleshipService.write(bytes);
				sendMessage(totCuloCane);
			}
		});
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(D) Log.e(TAG, "ON START");

		if (!bluetoothAdapter.isEnabled()) { //	setupChat() will then be called during onActivityResult
			Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		} else {
			if (blueBattleshipService == null) 
				setupBluetoothServices();
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		if(D) Log.e(TAG, "ON RESUME");

//		Performing this check in onResume() covers the case in which BT was
//		not enabled during onStart(), so we were paused to enable it...
//		onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
		if (blueBattleshipService != null) {
//			Only if the state is STATE_NONE, do we know that we haven't started already
			if (blueBattleshipService.getState() == BlueBattleshipService.STATE_NONE) {
//				Start the Bluetooth services
				blueBattleshipService.start();
			}
		}
	}
	
	@Override
	public synchronized void onPause() {
		super.onPause();
		if(D) Log.e(TAG, "ON PAUSE");
	}

	@Override
	public void onStop() {
		super.onStop();
		if(D) Log.e(TAG, "ON STOP");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(D) Log.e(TAG, "ON DESTROY");
//		Stop the Bluetooth services
		if (blueBattleshipService != null) 
			blueBattleshipService.stop();
	}	

	private void refreshBluetoothDevices() {
		deviceAdapter.notifyDataSetChanged();
	}

	public void runCampo(){
		Intent intent = new Intent(this, Campo.class);
		startActivity(intent);
	}

	private void setupBluetoothServices() {
		Log.d(TAG, "setupBluetoothServices()");

//		Initialize the BluetoothChatService to perform bluetooth connections
		blueBattleshipService = new BlueBattleshipService(this, handler);
		toolBox.mBlueBattleshipService = blueBattleshipService;
		// Initialize the buffer for outgoing messages
		mOutStringBuffer = new StringBuffer("");
	}

//	make the device visible to be seen by or to connect to another device
	private void ensureDiscoverable() {
		if(D) Log.d(TAG, "ENSURE DISCOVERABLE");
		if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
			Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
			startActivity(discoverableIntent);
		}
	}
	
	private void startDiscovery() {
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(broadcastReceiver, filter);
		bluetoothAdapter.startDiscovery();
	}
	
	/**
	 * Sends a message.
	 * @param message  A string of text to send.
	 */
	private void sendMessage(String message) {
		// Check that we're actually connected before trying anything
		if (blueBattleshipService.getState() != BlueBattleshipService.STATE_CONNECTED) {
			Toast.makeText(this, "not connected!", Toast.LENGTH_SHORT).show();
			return;
		}

		// Check that there's actually something to send
		if (message.length() > 0) {
			Log.i(TAG, "messaggio maggiore di zero. Vado a prendermi i byte!");
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = message.getBytes();
			Log.i(TAG, "ho preso i byte!");
			blueBattleshipService.write(send);
			
			 // Reset out string buffer to zero 
	        mOutStringBuffer.setLength(0);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(D) Log.d(TAG, "onActivityResult " + resultCode);
		switch (requestCode) {
//		case REQUEST_CONNECT_DEVICE:
////			When DeviceListActivity returns with a device to connect
//			if (resultCode == Activity.RESULT_OK) {
//				connectDevice(data, true);
//			}
//			break;
		case REQUEST_ENABLE_BT:
//			When the request to enable Bluetooth returns
			if (resultCode == Activity.RESULT_OK) {
//				Bluetooth is now enabled, so set up a chat session
				setupBluetoothServices();
			} else {
				// User did not enable Bluetooth or an error occurred
				Log.d(TAG, "Bluetooth not enabled");
				bbToast("Bluetooth non attivo, l'applicazione verrà chiusa!");
				//                forse chiamare finish è un po' drastico...
				//                finish();
			}
		}
	}

	private void connectDevice(Intent data) {
		// Get the device MAC address
		//        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//		Get the BluetoothDevice object
		BluetoothDevice device = bluetoothAdapter.getRemoteDevice("address");
//		Attempt to connect to the device
		blueBattleshipService.connect(device);
	}

	private void bbToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private class DeviceAdapter extends BaseAdapter {

		private Context context;

		public DeviceAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			return bluetoothDevices.size();
		}

		@Override
		public Object getItem(int position) {
			return bluetoothDevices.get(position);
		}

		@Override
		public long getItemId(int position) {
			return bluetoothDevices.get(position).hashCode();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tvDevice = new TextView(context);
			tvDevice.setText(bluetoothDevices.get(position).getName());
			return tvDevice;
		}
	}
}



