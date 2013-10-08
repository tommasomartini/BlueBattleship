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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Campo extends Activity {
	public int lunghezzaNave;
	//Drawable imm=getResources().getDrawable(R.drawable.ic_launcher);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campo);
		//LinearLayout principale=(LinearLayout)findViewById(R.id.principale);
		
		
		GridView grid = (GridView)findViewById(R.id.grid);
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		MyAdapter adapter = new MyAdapter(this,inflater);
		grid.setAdapter(adapter);
		
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				TextView v =(TextView)findViewById(R.id.text);
				v.setText("posizione "+position+" id= "+id);
				switch(lunghezzaNave){
				case 1:
					((TextView)view).setText("*");
					break;
				case 2:
					((TextView)view).setText("*");
					setta(++position);
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				}
			}
		});
		
		ToggleButton nave=(ToggleButton)findViewById(R.id.toggleButton1);
		nave.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				(Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT)).show();
				lunghezzaNave=2;
			}
		});
	}
	public void setta(int i){
		
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
	

