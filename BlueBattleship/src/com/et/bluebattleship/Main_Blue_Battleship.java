package com.et.bluebattleship;



import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
		}
	}

	private void refreshBluetoothDevices() {
		deviceAdapter.notifyDataSetChanged();
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
}



