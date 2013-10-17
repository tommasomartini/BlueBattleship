package com.et.bluebattleship;

import android.app.Activity;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class Match extends Activity {
	public boolean[] statoCampo;
	public Resources res;
	public Drawable drawable; 
	public Drawable drawable2;
	public GridView grid;
	private MyAdapter adapter;
	public LayoutInflater layoutInflater;
	DisplayMetrics metrics;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match);
		Intent intent=getIntent();
		metrics = new DisplayMetrics();
		 getWindowManager().getDefaultDisplay().getMetrics(metrics);
		statoCampo=intent.getBooleanArrayExtra("MiaGriglia");
		layoutInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		adapter=new MyAdapter(this,layoutInflater,metrics.widthPixels);
		grid = (GridView)findViewById(R.id.GridView1);
		grid.setAdapter(adapter);
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
	         //final int width = getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec);
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
			 private int mItemHeight = 0;
			 private int pixelW;
			public LayoutInflater ff;
			private GridView.LayoutParams mImageViewLayoutParams;

			public MyAdapter(Context context,LayoutInflater ff,int pixel) {
				super();
				
				pixelW=(pixel/10)-(9/2);
				this.context = context;
				this.ff=ff;
				
			}
			
			

			
			@Override
			public int getCount() {
				return statoCampo.length;
			}

			@Override
			public Object getItem(int position) {
				return statoCampo[position];
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			
			public View getView(int position, View convertView, ViewGroup parent) {
				res=getResources();
				drawable = res.getDrawable(R.drawable.grey);
				//ImageView v=(ImageView)ff.inflate(R.layout.image, null);
				//v.setScaleType(ImageView.ScaleType.CENTER_CROP);
				
				SquareImageView siv=new SquareImageView(context,pixelW);
			
            	
				Drawable blue = getResources().getDrawable(R.drawable.blue);
				Drawable grey = getResources().getDrawable(R.drawable.grey);
		       if(statoCampo[position]) siv.setImageDrawable(grey);
		       else siv.setImageDrawable(blue);
				return siv;
			}
			
		}

}
