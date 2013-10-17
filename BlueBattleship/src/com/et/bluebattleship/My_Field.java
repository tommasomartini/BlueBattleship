package com.et.bluebattleship;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class My_Field extends Activity {
	public boolean[] colpito;
	public boolean[] mioCampo;
	public Resources res;
	public Drawable drawable; 
	public Drawable drawable2;
	public GridView grid;
	private MyAdapter adapter;
	int n=0;
	public LayoutInflater layoutInflater;
	DisplayMetrics metrics;
	ToolBox toolBox;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my__field);
		
		toolBox=ToolBox.getInstance();
		colpito=toolBox.colpite;
		mioCampo=toolBox.my_field;
		metrics = new DisplayMetrics();
		 getWindowManager().getDefaultDisplay().getMetrics(metrics);
		 TH u = new TH();
		layoutInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		adapter=new MyAdapter(this,layoutInflater,metrics.widthPixels);
		grid = (GridView)findViewById(R.id.g);
		
		grid.setAdapter(adapter);
		grid.setHorizontalSpacing(2);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
			}
				
		});
		if(mioCampo[toolBox.nemicoVirtuale]){
			colpito[toolBox.nemicoVirtuale++]=true;
			adapter.notifyDataSetChanged();
			//u.run();
		}else{
			toolBox.nemicoVirtuale++;
			//u.run();
		}
		
		
		
		Button b=(Button)findViewById(R.id.but);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rr();
				
			}
		});
		

	}
	
	public void rr(){
		Intent i=new Intent(this,Match_Enemy.class);
		toolBox.colpite=colpito;
		startActivity(i);
		finish();
	}
	
	private class TH extends Thread{
		
		public void run(){
			try{
			Thread.sleep(5000);
			}catch(Exception e){}
		}
		
	}
	public class ricezione extends Thread{
		//ricevi dati dal service 
		//controllo in mioCampo se ha colpito setto statoCampo(position)=true
		//adapter.notifyDataSetChanged();
		//se ha colpito continuo a seguire
		//se ha macato toolBox.colpite=statoCampo;
		//startActivity(Match_enemy);
		//onDestroy();
		
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
				return colpito.length;
			}

			@Override
			public Object getItem(int position) {
				return colpito[position];
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
				Drawable grey = getResources().getDrawable(R.drawable.grey);

		       if(colpito[position]&&mioCampo[position]) squareImageView.setImageDrawable(burn);
		       else {
		    	   if(mioCampo[position])squareImageView.setImageDrawable(grey);
		    	   else squareImageView.setImageDrawable(blue);
		       }
				return squareImageView;
			}
			
		}
	 
	 
	 
	 private class Utils {

	        public void showDummyWaitingDialog(final Context context, final Intent startingIntent) {
	            // ...
	            final ProgressDialog progressDialog = ProgressDialog.show(context, "Please wait...", "Loading data ...", true);

	            new Thread() {
	                public void run() {
	                    try{
	                        // Do some work here
	                        sleep(5000);
	                    } catch (Exception e) {
	                    }
	                    // start next intent
	                    new Thread() {
	                        public void run() {
	                        // Dismiss the Dialog 
	                        progressDialog.dismiss();
	                        // start selected activity
	                        if ( startingIntent != null) context.startActivity(startingIntent);
	                        }
	                    }.start();
	                }
	            }.start();  

	        }

	    } 

}
