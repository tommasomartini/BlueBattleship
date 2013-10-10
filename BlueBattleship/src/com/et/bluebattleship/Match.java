package com.et.bluebattleship;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Match extends Activity {
	public boolean[] statoCampo;
	public Resources res;
	public Drawable drawable; 
	public Drawable drawable2;
	public GridView grid;
	private MyAdapter adapter;
	public LayoutInflater layoutInflater;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match);
		Intent intent=getIntent();
		statoCampo=intent.getBooleanArrayExtra("MiaGriglia");
		//if(statoCampo[10]) (Toast.makeText(getApplicationContext(), "Non puoi mettere la nave li", Toast.LENGTH_SHORT)).show();
		layoutInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		adapter=new MyAdapter(this,layoutInflater);
		grid = (GridView)findViewById(R.id.gridView1);
		grid.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.match, menu);
		return true;
	}
	
	
	
	
	 class MyAdapter extends BaseAdapter {
		 
			private Context context;
			public String pr;
			public LayoutInflater ff;
			private int cont=0;

			public MyAdapter(Context context,LayoutInflater ff) {
				super();
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
				drawable2 = res.getDrawable(R.drawable.blue);
				
				ImageView v = (ImageView)ff.inflate(R.layout.image, null);
				
		       TextView a=new TextView(context);
		       if(statoCampo[position]) v.setImageDrawable(drawable);
		       else v.setImageDrawable(drawable2);
				return v;
			}
			
		}

}
