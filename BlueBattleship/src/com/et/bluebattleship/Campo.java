package com.et.bluebattleship;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

public class Campo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campo);
		LinearLayout principale=(LinearLayout)findViewById(R.id.principale);
		
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
	

