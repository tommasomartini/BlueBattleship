package com.et.bluebattleship;



import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// Chi era il nano sempre incazzato?
// Brontolo

public class Main_Blue_Battleship extends Activity {

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

		if (bluetoothAdapter != null) { // bluetooth supported

			bluetoothDevices = new Vector<BluetoothDevice>();
			deviceAdapter = new DeviceAdapter(this);
			lvDevices = (ListView)findViewById(R.id.lvDevices);
			lvDevices.setAdapter(deviceAdapter);

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
					bluetoothDevices = new Vector<BluetoothDevice>();
					refreshBluetoothDevices();
				}
			});

			btSearchDevices.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					
					bluetoothDevices = new Vector<BluetoothDevice>();
					refreshBluetoothDevices();

					final BroadcastReceiver mReceiver = new BroadcastReceiver() {
						public void onReceive(Context context, Intent intent) {
							String action = intent.getAction();

							if (BluetoothDevice.ACTION_FOUND.equals(action)) {
								BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

								bluetoothDevices.add(device);
								refreshBluetoothDevices();
								//								LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
								//
								//								TextView v = (TextView)inflater.inflate(R.layout.text, null);
								//								v.setText(device.getName()+ "\n" +device.getAddress());
								//								saveDevice(device);
								//								LinearLayout base = (LinearLayout)findViewById(R.id.LinearLayout1);
								//								base.addView(v);
							}
						}
					};
					IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
					registerReceiver(mReceiver, filter);
					if(bluetoothAdapter.startDiscovery()){

						(Toast.makeText(getApplicationContext(), "started", Toast.LENGTH_LONG)).show();
					}
				}
			});
		}
	}

	private void refreshBluetoothDevices() {
		deviceAdapter.notifyDataSetChanged();
	}

	public void runCampo(){
		Intent intent = new Intent(this, Campo.class);
		startActivity(intent);
	}

	public void saveDevice(BluetoothDevice dev){
		SQLHelper myDB=new SQLHelper(this);
		SQLiteDatabase write=myDB.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", dev.getName()); 
		values.put("address", dev.getAddress());

		long id = write.insert("list", null, values);
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
	//    private class AcceptThread extends Thread {
	//        private final BluetoothServerSocket mmServerSocket;
	//     
	//        public AcceptThread() {
	//            // Use a temporary object that is later assigned to mmServerSocket,
	//            // because mmServerSocket is final
	//            BluetoothServerSocket tmp = null;
	//            try {
	//              
	//            	
	//                tmp = mB.listenUsingRfcommWithServiceRecord(ed.getText().toString(), BBUUID);
	//            } catch (IOException e) { }
	//            mmServerSocket = tmp;
	//        }
	//     
	//        public void run() {
	//            BluetoothSocket socket = null;
	//            // Keep listening until exception occurs or a socket is returned
	//            while (true) {
	//                try {
	//                    socket = mmServerSocket.accept();
	//                } catch (IOException e) {
	//                    break;
	//                }
	//                // If a connection was accepted
	//                if (socket != null) {
	//                    // Do work to manage the connection (in a separate thread)
	//                    manageConnectedSocket(socket);
	//                    try{
	//                    mmServerSocket.close();
	//                    break;
	//                    }catch(Exception e){}
	//                }
	//            }
	//        }
	//     
	//        public void manageConnectedSocket(BluetoothSocket socket){
	//        	
	//        }
	//        
	//        
	//        /** Will cancel the listening socket, and cause the thread to finish */
	//        public void cancel() {
	//            try {
	//                mmServerSocket.close();
	//            } catch (IOException e) { }
	//        }
	//    }

	//    private class ConnectThread extends Thread {
	//        private final BluetoothSocket mmSocket;
	//        private final BluetoothDevice mmDevice;
	//     
	//        public ConnectThread(BluetoothDevice device) {
	//            // Use a temporary object that is later assigned to mmSocket,
	//            // because mmSocket is final
	//            BluetoothSocket tmp = null;
	//            mmDevice = device;
	//     
	//            // Get a BluetoothSocket to connect with the given BluetoothDevice
	////            try {
	////                // MY_UUID is the app's UUID string, also used by the server code
	//////                tmp = device.createRfcommSocketToServiceRecord(BBUUID);
	////            } catch (IOException e) { }
	////            mmSocket = tmp;
	//        }

	//        public void run() {
	//            // Cancel discovery because it will slow down the connection
	//            mB.cancelDiscovery();
	//     
	//            try {
	//                // Connect the device through the socket. This will block
	//                // until it succeeds or throws an exception
	//                mmSocket.connect();
	//            } catch (IOException connectException) {
	//                // Unable to connect; close the socket and get out
	//                try {
	//                    mmSocket.close();
	//                } catch (IOException closeException) { }
	//                return;
	//            }
	//     
	//            // Do work to manage the connection (in a separate thread)
	//            manageConnectedSocket(mmSocket);
	//        }
	//        
	//        public void manageConnectedSocket(BluetoothSocket socket){
	//        	
	//        }
	//     
	//        /** Will cancel an in-progress connection, and close the socket */
	//        public void cancel() {
	//            try {
	//                mmSocket.close();
	//            } catch (IOException e) { }
	//        }
	//    }


}



