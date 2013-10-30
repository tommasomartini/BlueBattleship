package com.et.bluebattleship;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Match_Enemy extends Activity {
	public boolean[] naviPrese;
	public boolean[] mancato;
	public boolean[] campoAvversario;
	public Resources res;
	public Drawable drawable; 
	public Drawable drawable2;
	public GridView grid;
	private MyAdapter adapter;
	int n=0;
	public LayoutInflater layoutInflater;
	DisplayMetrics metrics;
	public ToolBox toolBox;
	
	
	
	//per service
		private static final String TAG = "BluetoothChat";
	    private static final boolean D = true;

	    // Message types sent from the BluetoothChatService Handler
	    public static final int MESSAGE_STATE_CHANGE = 1;
	    public static final int MESSAGE_READ = 2;
	    public static final int MESSAGE_WRITE = 3;
	    public static final int MESSAGE_DEVICE_NAME = 4;
	    public static final int MESSAGE_TOAST = 5;

	    // Key names received from the BluetoothChatService Handler
	    public static final String DEVICE_NAME = "device_name";
	    public static final String TOAST = "toast";

	    // Intent request codes
	    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
	    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
	    private static final int REQUEST_ENABLE_BT = 3;

	    // Layout Views
	    private ListView mConversationView;
	    private EditText mOutEditText;
	    private Button mSendButton;

	    // Name of the connected device
	    private String mConnectedDeviceName = null;
	    // Array adapter for the conversation thread
	    private ArrayAdapter<String> mConversationArrayAdapter;
	    // String buffer for outgoing messages
	    private StringBuffer mOutStringBuffer;
	    // Local Bluetooth adapter
	    private BluetoothAdapter mBluetoothAdapter = null;
	    // Member object for the chat services
	    private BlueBattleshipService mChatService = null;
		//
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match);
		toolBox=ToolBox.getInstance();
		naviPrese=toolBox.enemy_field;
		mancato=toolBox.mancato_enemy;

		metrics = new DisplayMetrics();
		 getWindowManager().getDefaultDisplay().getMetrics(metrics);
		layoutInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		adapter=new MyAdapter(this,layoutInflater,metrics.widthPixels);
		grid = (GridView)findViewById(R.id.GridView1);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				
				if(COLPISCI(position)){
					naviPrese[position]=true;
					adapter.notifyDataSetChanged();
					Toast.makeText(getApplicationContext(), "COLPITO!", Toast.LENGTH_SHORT).show();
					controllaCampo();
				}else {
					Toast.makeText(getApplicationContext(), "MANCATO!", Toast.LENGTH_SHORT).show();
					mancato[position]=true;
					adapter.notifyDataSetChanged();
					runMyField();
				}
			}
		});
		
		
	}
	
	@Override
    public void onStart() {
        super.onStart();
        if(D) Log.e(TAG, "++ ON START ++");

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        // Otherwise, setup the chat session
        } else {
            if (mChatService == null) setupChat();
        }
    }
	
	@Override
    public synchronized void onResume() {
        super.onResume();
        if(D) Log.e(TAG, "+ ON RESUME +");

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BlueBattleshipService.STATE_NONE) {
              // Start the Bluetooth chat services
              mChatService.start();
            }
        }
    }
	
	private void setupChat() {
        Log.d(TAG, "setupChat()");

       

        // Initialize the compose field with a listener for the return key
//        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
//        mOutEditText.setOnEditorActionListener(mWriteListener);

       
       

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BlueBattleshipService(this, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }
	
	private final void setStatus(int resId) {
//    	getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        final ActionBar actionBar = getActionBar();
//        actionBar.setSubtitle(resId);
    }

    private final void setStatus(CharSequence subTitle) {
//    	getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        final ActionBar actionBar = getActionBar();
//        actionBar.setSubtitle(subTitle);
    }
	
	private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MESSAGE_STATE_CHANGE:
                if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                switch (msg.arg1) {
                case BlueBattleshipService.STATE_CONNECTED:
                    setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                    mConversationArrayAdapter.clear();
                    break;
                case BlueBattleshipService.STATE_CONNECTING:
                    setStatus(R.string.title_connecting);
                    break;
                case BlueBattleshipService.STATE_LISTEN:
                case BlueBattleshipService.STATE_NONE:
                    setStatus(R.string.title_not_connected);
                    break;
                }
                break;
            case MESSAGE_WRITE:
                byte[] writeBuf = (byte[]) msg.obj;
                // construct a string from the buffer
                String writeMessage = new String(writeBuf);
                mConversationArrayAdapter.add("Me:  " + writeMessage);
                break;
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                // construct a string from the valid bytes in the buffer
                String readMessage = new String(readBuf, 0, msg.arg1);
                mConversationArrayAdapter.add(mConnectedDeviceName+":  " + readMessage);
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
	
	public void runMyField(){
		Intent intent=new Intent(this, My_Field.class);
		toolBox.enemy_field=naviPrese;
		toolBox.mancato_enemy=mancato;
		startActivity(intent);
		finish();
	}
	
	public boolean COLPISCI(int posizione){
		return toolBox.campoVirtuale[posizione];
		//sendMessage(""+posizione);
		
		//ritorna true o false
		
	}
	
	public void controllaCampo(){
		
		if(--toolBox.numero_navi==0) {
			Intent Match=new Intent(this, Match_Win.class);
			//invia messaggio che hai vitno e lui ha perso (looser)
			startActivity(Match);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.match, menu);
		return true;
	}
	
	
	 public class SquareImageView extends ImageView
	 {
		 public int width;
	     public SquareImageView(final Context context,int width)
	     {
	         super(context);
	         this.width=width;
	     }

	     public SquareImageView(final Context context, final AttributeSet attrs)
	     {
	         super(context, attrs);
	     }

	     public SquareImageView(final Context context, final AttributeSet attrs, final int defStyle)
	     {
	         super(context, attrs, defStyle);
	     }


	     @Override
	     public void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
	     {
	         setMeasuredDimension(width, width);
	     }

	     @Override
	     protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh)
	     {
	         super.onSizeChanged(w, w, oldw, oldh);
	     }
	 }
	
	 class MyAdapter extends BaseAdapter {
		 
			public Context context;
			public String pr;
			 private int pixelW;
			public LayoutInflater ff;

			public MyAdapter(Context context,LayoutInflater ff,int pixel) {
				super();
				pixelW=(pixel/10)-(9/2);
				this.context = context;
				this.ff=ff;
			}
			
			@Override
			public int getCount() {
				return naviPrese.length;
			}

			@Override
			public Object getItem(int position) {
				return naviPrese[position];
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			
			public View getView(int position, View convertView, ViewGroup parent) {
				res=getResources();
				drawable = res.getDrawable(R.drawable.grey);
				SquareImageView squareImageView=new SquareImageView(context,pixelW);
				Drawable blue = getResources().getDrawable(R.drawable.blue);
				Drawable burn = getResources().getDrawable(R.drawable.burn);
				
				Drawable mancato_imm = getResources().getDrawable(R.drawable.colpito);
		       if(naviPrese[position]) squareImageView.setImageDrawable(burn);
		       else {
		    	   if(mancato[position])squareImageView.setImageDrawable(mancato_imm);
		    	   else squareImageView.setImageDrawable(blue);
		       }
				return squareImageView;
			}
			
		}
	 
	 
	 private void sendMessage(String message) {
	        // Check that we're actually connected before trying anything
	        if (mChatService.getState() != BlueBattleshipService.STATE_CONNECTED) {
	            Toast.makeText(this, "non connesso", Toast.LENGTH_SHORT).show();
	            return;
	        }

	        // Check that there's actually something to send
	        if (message.length() > 0) {
	            // Get the message bytes and tell the BluetoothChatService to write
	            byte[] send = message.getBytes();
	            mChatService.write(send);

	            // Reset out string buffer to zero and clear the edit text field
	            mOutStringBuffer.setLength(0);
	            mOutEditText.setText(mOutStringBuffer);
	        }
	    }
	

}
