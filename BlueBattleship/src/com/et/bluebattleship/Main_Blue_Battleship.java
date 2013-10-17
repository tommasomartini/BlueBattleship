package com.et.bluebattleship;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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
	
	// Debugging
    private static final String TAG = "BlueBluetooth";
    private static final boolean D = true;

	private static final long UUID_MOST_SIG_BIT = 1;
	private static final long UUID_LEAST_SIG_BIT = 1;
	private static final String BLUETOOTH_APP_NAME = "blue_battleship";
	//	private static final UUID BLUETOOTH_UUID = new UUID(UUID_MOST_SIG_BIT, UUID_LEAST_SIG_BIT);
	private static final UUID BLUETOOTH_UUID = UUID.randomUUID();
	
	// Unique UUID for this application
    private static final UUID MY_UUID_SECURE =
        UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID MY_UUID_INSECURE =
        UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");

	private ServerThread serverThread;

	private BluetoothAdapter bluetoothAdapter;

	private Button btActBT;
	private Button btDeactBT;
	private Button btSearchDevices;

	private List<BluetoothDevice> bluetoothDevices;

	private ListView lvDevices;

	private DeviceAdapter deviceAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		if (bluetoothAdapter == null) { // bluetooth supported
			Toast.makeText(this, "Ops!\n Il tuo dispositivo non supporta il Bluetooth!", Toast.LENGTH_LONG).show();
			return;
		}
		
		

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

				ClientThread clientThread = new ClientThread(bluetoothDevice);
				clientThread.run();
			}

		});
		//			lvDevices.setAdapter(deviceAdapter);

		btActBT = (Button)findViewById(R.id.btActBT);
		btDeactBT = (Button)findViewById(R.id.btDeactBT);
		btSearchDevices = (Button)findViewById(R.id.btSearchDevices);

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
				Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				startActivity(discoverableIntent);
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
		
		serverThread = new ServerThread();
		serverThread.run();
	}


	private void refreshBluetoothDevices() {
		deviceAdapter.notifyDataSetChanged();
	}

	private void manageConnectedSocket(BluetoothSocket socket) {
		Toast.makeText(this, "Connesso!", Toast.LENGTH_SHORT).show();
	}

	public void runCampo(){
		Intent intent = new Intent(this, Campo.class);
		startActivity(intent);
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

	private class ServerThread extends Thread {

		private final BluetoothServerSocket mmServerSocket;

		public ServerThread() {

			BluetoothServerSocket tmp = null;
			try {
				tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(BLUETOOTH_APP_NAME, BLUETOOTH_UUID);
				
			} catch (IOException e) {
				Log.i("BBS", "errore dove sai tu!");
			}
			mmServerSocket = tmp;
		}

		public void run() {
			Toast.makeText(Main_Blue_Battleship.this, "fino a qui!", Toast.LENGTH_SHORT).show();
			BluetoothSocket socket = null;
			//	        while (true) {
			try {
				socket = mmServerSocket.accept();
			} catch (IOException e) {
				//                break;
			}
			//	            if (socket != null) {
			//	                manageConnectedSocket(socket);
			//	                try {
			//						mmServerSocket.close();
			//					} catch (IOException e) {
			//						// TODO Auto-generated catch block
			//						e.printStackTrace();
			//					}
			//	                break;
			//	            }
			//	        }
		}

		public void cancel() {
			try {
				mmServerSocket.close();
			} catch (IOException e) { }
		}
	}

	private class ClientThread extends Thread {
		private final BluetoothSocket mmSocket;
		private final BluetoothDevice mmDevice;

		public ClientThread(BluetoothDevice device) {
			BluetoothSocket tmp = null;
			mmDevice = device;

			try {
				tmp = device.createRfcommSocketToServiceRecord(MY_UUID_SECURE);
			} catch (IOException e) { }
			mmSocket = tmp;
		}

		public void run() {
			bluetoothAdapter.cancelDiscovery();

			try {
				mmSocket.connect();
			} catch (IOException connectException) {
				try {
					mmSocket.close();
				} catch (IOException closeException) { }
				return;
			}

			manageConnectedSocket(mmSocket);
		}

		/** Will cancel an in-progress connection, and close the socket */
		public void cancel() {
			try {
				mmSocket.close();
			} catch (IOException e) { }
		}
	}
	
	private void toastDebug(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
}



