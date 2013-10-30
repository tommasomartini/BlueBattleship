package com.et.bluebattleship;

import java.util.List;
import java.util.UUID;
import java.util.Vector;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Main_Blue_Battleship extends Activity {
	
	// Debugging
    private static final String TAG = "BlueBattleship";
    private static final boolean D = true;
    private ToolBox toolBox;
    // Message types sent from the BlueBattleshipService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BlueBattleshipService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
//    private ArrayAdapter<String> mConversationArrayAdapter;
    // String buffer for outgoing messages
    private StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    private BluetoothAdapter bluetoothAdapter = null;
    // Member object for the chat services
    private BlueBattleshipService blueBattleshipService = null;

	private static final String BLUETOOTH_APP_NAME = "blue_battleship";
	
	// Unique UUID for this application
    private static final UUID MY_UUID_SECURE =
        UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE =
        UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

	private Button btActBT;
	private Button btDeactBT;
	private Button btSearchDevices;
	private Button btSend;

	private List<BluetoothDevice> bluetoothDevices;

	private ListView lvDevices;

	private DeviceAdapter deviceAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		toolBox=ToolBox.getInstance();
		if (D) Log.i(TAG, "ON CREATE");
		setContentView(R.layout.activity_main);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (bluetoothAdapter == null) { // bluetooth supported
			Toast.makeText(this, "Ops! Il tuo dispositivo non supporta il Bluetooth!", Toast.LENGTH_LONG).show();
			return;
		}
		
		blueBattleshipService = new BlueBattleshipService(this, mHandler);

		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();

				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					bluetoothDevices.add(device);
					refreshBluetoothDevices();
				}
			}
		};

		//			bluetoothDevices = new Vector<BluetoothDevice>();
		//			deviceAdapter = new DeviceAdapter(this);
		lvDevices = (ListView)findViewById(R.id.lvDevices);
		lvDevices.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
				BluetoothDevice bluetoothDevice = bluetoothDevices.get(position);
				blueBattleshipService.connect(bluetoothDevice, false);
				toolBox.primo=true;
			}

		});
		//			lvDevices.setAdapter(deviceAdapter);

		btActBT = (Button)findViewById(R.id.btActBT);
		btDeactBT = (Button)findViewById(R.id.btDeactBT);
		btSearchDevices = (Button)findViewById(R.id.btSearchDevices);
		btSend = (Button)findViewById(R.id.btSend);
		
		if (bluetoothAdapter.isEnabled()) {
			btActBT.setEnabled(false);
		} else {
			btDeactBT.setEnabled(false);
		}

		//vado al campo
		Button Campo=(Button)findViewById(R.id.startCampo);
		Campo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				runCampo();	
			}
		});

		btActBT.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				ensureDiscoverable();
//				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
//				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
//				startActivityForResult(discoverableIntent, REQUEST_ENABLE_BT);
				btActBT.setEnabled(false);
				btDeactBT.setEnabled(true);
			}

		});

		btDeactBT.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				bluetoothAdapter.cancelDiscovery();
				bluetoothAdapter.disable();
				btActBT.setEnabled(true);
				btDeactBT.setEnabled(false);
			}
		});

		btSearchDevices.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){

				bluetoothDevices = new Vector<BluetoothDevice>();
				deviceAdapter = new DeviceAdapter(Main_Blue_Battleship.this);
				lvDevices.setAdapter(deviceAdapter);

				IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
				registerReceiver(mReceiver, filter);
				bluetoothAdapter.startDiscovery();
			}
		});
		
		btSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String totCuloCane = "Sti cazzi! Enea, ora fammi sta bocca che ti fecondo l'intestino tenue!";
				byte[] bytes = totCuloCane.getBytes();
				blueBattleshipService.write(bytes);
			}
		});
	}

	private void refreshBluetoothDevices() {
		deviceAdapter.notifyDataSetChanged();
	}

	public void runCampo(){
		Intent intent = new Intent(this, Campo.class);
		startActivity(intent);
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
        // Stop the Bluetooth chat services
        if (blueBattleshipService != null) blueBattleshipService.stop();
        if(D) Log.e(TAG, "ON DESTROY");
    }	

    @Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "ON START");

//         If BT is not on, request that it be enabled.
//         setupChat() will then be called during onActivityResult
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (blueBattleshipService == null) setupChat();
        }
    }

    @Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "ON RESUME");

//         Performing this check in onResume() covers the case in which BT was
//         not enabled during onStart(), so we were paused to enable it...
//         onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (blueBattleshipService != null) {
//             Only if the state is STATE_NONE, do we know that we haven't started already
            if (blueBattleshipService.getState() == BlueBattleshipService.STATE_NONE) {
//               Start the Bluetooth services
              blueBattleshipService.start();
            }
        }
    }

    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
//        mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
//        mConversationView = (ListView) findViewById(R.id.in);
//        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
//        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
//        mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the send button with a listener that for click events
//        mSendButton = (Button) findViewById(R.id.button_send);
//        mSendButton.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                // Send a message using content of the edit text widget
//                TextView view = (TextView) findViewById(R.id.edit_text_out);
//                String message = view.getText().toString();
//                sendMessage(message);
//            }
//        });

        // Initialize the BluetoothChatService to perform bluetooth connections
        blueBattleshipService = new BlueBattleshipService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    private void ensureDiscoverable() {
        if(D) Log.d(TAG, "ensure discoverable");
        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
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
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            blueBattleshipService.write(send);
        }
    }

    // The action listener for the EditText widget, to listen for the return key
    private TextView.OnEditorActionListener mWriteListener =
        new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            if(D) Log.i(TAG, "END onEditorAction");
            return true;
        }
    };

//    questa variabile prima non era statica!
//     The Handler that gets information back from the BluetoothChatService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BlueBattleshipService.STATE_CONNECTED:
//                    mConversationArrayAdapter.clear();
                    break;
                case BlueBattleshipService.STATE_CONNECTING:
//                    setStatus(R.string.title_connecting);
                    break;
                case BlueBattleshipService.STATE_LISTEN:
                case BlueBattleshipService.STATE_NONE:
//                    setStatus(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
//                mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                toastDebug(readMessage);
//                mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
                break;
            case MESSAGE_DEVICE_NAME:
                // save the connected device's name
//                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
//                Toast.makeText(getApplicationContext(), "Connected to "
//                               + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                break;
            case MESSAGE_TOAST:
//                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
//                               Toast.LENGTH_SHORT).show();
                break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
        case REQUEST_CONNECT_DEVICE_SECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, true);
            }
            break;
//        case REQUEST_CONNECT_DEVICE_INSECURE:
//            // When DeviceListActivity returns with a device to connect
//            if (resultCode == Activity.RESULT_OK) {
//                connectDevice(data, false);
//            }
//            break;
        case REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
//                 Bluetooth is now enabled, so set up a chat session
                setupChat();
            } else {
                // User did not enable Bluetooth or an error occurred
                Log.d(TAG, "Bluetooth not enabled");
                toastDebug("Bluetooth non attivo, l'applicazione verrà chiusa!");
//                forse chiamare finish è un po' drastico...
//                finish();
            }
        }
    }

    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
//        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice("address");
        // Attempt to connect to the device
        blueBattleshipService.connect(device, secure);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Intent serverIntent = null;
//        switch (item.getItemId()) {
//        case R.id.secure_connect_scan:
//            // Launch the DeviceListActivity to see devices and do scan
//            serverIntent = new Intent(this, DeviceListActivity.class);
//            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
//            return true;
//        case R.id.insecure_connect_scan:
//            // Launch the DeviceListActivity to see devices and do scan
//            serverIntent = new Intent(this, DeviceListActivity.class);
//            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
//            return true;
//        case R.id.discoverable:
//            // Ensure this device is discoverable by others
//            ensureDiscoverable();
//            return true;
//        }
//        return false;
//    }
    
    private void toastDebug(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
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



