package com.et.bluebattleship;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class My_Field extends Activity {
	private boolean[] colpito;
	private boolean[] mioCampo;
	private int[] shipType; 
	private Resources res;
	private Drawable drawable; 
	private Drawable drawable2;
	private GridView grid;
	private MyAdapter adapter;
	private LayoutInflater layoutInflater;
	private DisplayMetrics metrics;
	private ToolBox toolBox;
	private ProgressDialog progressDialog;
	private ProgressDialog progressDialog1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my__field);
		
		toolBox=ToolBox.getInstance();
		colpito=toolBox.colpite;
		mioCampo=toolBox.my_field;
		
		
		progressDialog1 = ProgressDialog.show(My_Field.this, "", "Waiting For Enemy...Get Ready For the BATTLE!");
		progressDialog1.setCancelable(true);
		progressDialog1.setOnCancelListener(new OnCancelListener(){

            @Override
            public void onCancel(DialogInterface arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(progressDialog.getContext());
                builder.setMessage( "Are you sure you want to cancel?")
                       .setCancelable(false)
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface diag, int id) {
                               diag.dismiss();
                               progressDialog1.dismiss();
                               finish();

                           }
                       })
                       .setNegativeButton("No", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface diag, int id) {
                                diag.cancel();
                                
                           }
                       });
                AlertDialog alert = builder.create();
                alert.show();               
            }

        });
        new Thread() {
        public void run() {
        try{
        while(!toolBox.data_change){
        	sleep(500);
        }
        } catch (Exception e) {
        Log.e("tag", e.getMessage());
        }
        progressDialog.dismiss();
        
        }
        }.start();
		
		
		
		progressDialog=new ProgressDialog(getApplicationContext());
		//progressDialog = ProgressDialog.show(My_Field.this, "", "Waiting For Enemy...Get Ready For the BATTLE!");
		progressDialog.setCancelable(true);
		progressDialog.setOnCancelListener(new OnCancelListener(){

            @Override
            public void onCancel(DialogInterface arg0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(progressDialog.getContext());
                builder.setMessage( "Are you sure you want to cancel?")
                       .setCancelable(false)
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface diag, int id) {
                               diag.dismiss();
                               progressDialog.dismiss();
                               finish();

                           }
                       })
                       .setNegativeButton("No", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface diag, int id) {
                                diag.cancel();
                                
                           }
                       });
                AlertDialog alert = builder.create();
                alert.show();               
            }

        });
        new Thread() {
        public void run() {
        try{
        while(!toolBox.report_hit){
        	sleep(500);
        }
        } catch (Exception e) {
        Log.e("tag", e.getMessage());
        }
        progressDialog.dismiss();
        if(toolBox.my_field[toolBox.hit]){
        	toolBox.colpite[toolBox.hit]=true;
        	toolBox.report_hit=false;
        	//ma.notifyDataSetChanged();
        	Intent i=new Intent(getApplicationContext(), My_Field.class);
    		startActivity(i);
    		finish();
        }else{
        	toolBox.report_hit=false;
        	//ma.notifyDataSetChanged();
        	Intent i=new Intent(getApplicationContext(), Match_Enemy.class);
    		startActivity(i);
    		finish();
        }
        }
        }.start();
        
		
		metrics = new DisplayMetrics();
		 getWindowManager().getDefaultDisplay().getMetrics(metrics);
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

	}
	

	public void restart(){
		Intent i=new Intent(this,My_Field.class);
		startActivity(i);
		finish();
	}
	
	public void build(){
		
		        (Toast.makeText(getApplicationContext(), "boh", Toast.LENGTH_SHORT)).show();
		
	}
	
	
	public void rr(){
		Intent i=new Intent(this,Match_Enemy.class);
		toolBox.colpite=colpito;
		startActivity(i);
		finish();
	}
	
	public class newThread{
		public MyAdapter ma;
		public newThread(MyAdapter ma){
			this.ma=ma;
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
				Drawable blue = getResources().getDrawable(R.drawable.vuoto);
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

}
