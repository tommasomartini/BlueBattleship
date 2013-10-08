package com.et.bluebattleship;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.Toast;

public class Campo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campo);
		
		GridView grid = (GridView)findViewById(R.id.grid);
		grid.setNumColumns(10);
		MyAdapter adapter = new MyAdapter(this);
		BaseAdapter bb = new BaseAdapter();
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        Button b = (Button)inflater.inflate(R.layout.cas, null);
		Button c=new Button(this);

		//adapter.(b);
		adapter.add(c);

		c.setText("ciao");
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos, long id){
				(Toast.makeText(getApplicationContext(), "cliccato #"+id+" in posizione "+pos, Toast.LENGTH_LONG)).show();
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

	private Button[] button;
	private int cont=0;

	public MyAdapter(Context context) {
		this.context = context;
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
		return button[position].hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Button button;
		if (convertView == null) {
			button = new Button(context);
			button.setText("ciao");
		} else {
			button = (Button) convertView;
		}
		button.setText("boh");
		return button;
	}

}
	

