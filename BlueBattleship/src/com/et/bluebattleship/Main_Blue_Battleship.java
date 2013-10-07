package com.et.bluebattleship;



import java.io.IOException;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

// Gongolo!

public class Main_Blue_Battleship extends Activity {
	public final int REQUEST_ENABLE_BT=1;
//	private static final UUID BBUUID=UUID.fromString("BlueBattleship");
	private EditText ed;
	private BluetoothAdapter mB = BluetoothAdapter.getDefaultAdapter();
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ed = (EditText)findViewById(R.id.edit1);
        
        
        
        Button act = (Button)findViewById(R.id.act);
        
        act.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		
                mB.enable();
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                		discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                		startActivity(discoverableIntent);
                		
//                AcceptThread at=new AcceptThread();
//                at.run();
                
                //Attiviamo il server		
                		
        	}
        	
        });
        
        
        Button deact = (Button)findViewById(R.id.deact);
        
        deact.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		BluetoothAdapter mB = BluetoothAdapter.getDefaultAdapter();
                
        		mB.disable();
        	}
        });
        
        Button ric = (Button)findViewById(R.id.ricerca);
        
        ric.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		
        		
        		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        
                        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                            
                            TextView v = (TextView)inflater.inflate(R.layout.text, null);
                            v.setText(device.getName()+ "\n" +device.getAddress());
                            v.setOnClickListener(new OnClickListener(){
                            	@Override
                            	public void onClick(View v){
                            		
                            		
                            		
                            	}
                            });
                            saveDevice(device);
                            LinearLayout base = (LinearLayout)findViewById(R.id.LinearLayout1);
                            base.addView(v);
                        }
                    }
        		};
        		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        		registerReceiver(mReceiver, filter);
        		if(mB.startDiscovery()){
        			
                	(Toast.makeText(getApplicationContext(), "started", Toast.LENGTH_LONG)).show();
        		}
        	}
        });
    }

    
    public void saveDevice(BluetoothDevice dev){
    	SQLHelper myDB=new SQLHelper(this);
    	SQLiteDatabase write=myDB.getWritableDatabase();
    	ContentValues values = new ContentValues();
        values.put("name", dev.getName()); 
        values.put("address", dev.getAddress());
        
        long id = write.insert("list", null, values);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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



