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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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
	
	public void runMyField(){
		Intent intent=new Intent(this, My_Field.class);
		toolBox.enemy_field=naviPrese;
		toolBox.mancato_enemy=mancato;
		startActivity(intent);
		finish();
	}
	
	public boolean COLPISCI(int posizione){
		return toolBox.campoVirtuale[posizione];
		//richiedi via bluetooth
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
	 
	

}
