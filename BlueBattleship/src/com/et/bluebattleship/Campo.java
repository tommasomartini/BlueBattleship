package com.et.bluebattleship;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class Campo extends Activity {
	
	//Drawable imm=getResources().getDrawable(R.drawable.ic_launcher);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campo);
		//LinearLayout principale=(LinearLayout)findViewById(R.id.principale);
		
		/*
		Button[][] Matrix=new Button[3][3];
		for(int i=0;i<3;i++){
			LinearLayout madre=(LinearLayout)principale.getChildAt(i);
			for(int j=0;j<3;j++){
				Matrix[i][j]=(Button)madre.getChildAt(j);
			}
		}
		
		for(int i=0;i<3;i++){
			
			for(int j=0;j<3;j++){
				
				Matrix[i][j].setText(""+i+j);
			}
		}

		*/
		GridView grid = (GridView)findViewById(R.id.grid);
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		MyAdapter adapter = new MyAdapter(this,inflater);
		grid.setAdapter(adapter);
		
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				TextView v =(TextView)findViewById(R.id.text);
				v.setText("posizione "+position+" id= "+id);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.campo, menu);
		return true;
	}

}

	

 class MyAdapter extends BaseAdapter {

	private Context context;
	public String pr;
	public LayoutInflater ff;
	//private Button imm;
	private int[] button=new int[100];
	private int cont=0;

	public MyAdapter(Context context, LayoutInflater ff) {
		this.context = context;
		this.ff=ff;
	}

	
	@Override
	public int getCount() {
		return button.length;
	}

	@Override
	public Object getItem(int position) {
		return button[position];
	}

	@Override
	public long getItemId(int position) {
		return ((Integer)position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		
        TextView a = (TextView)ff.inflate(R.layout.text, null);
		
		return a;
	}
	
	

}
	

