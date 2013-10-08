package com.et.bluebattleship;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class Campo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campo);
		
		GridView grid = (GridView)findViewById(R.id.grid);
		grid.setNumColumns(10);
		ArrayAdapter<Button> adapter = new ArrayAdapter<Button>(this,R.layout.activity_campo,R.id.vvv2);
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        Button b = (Button)inflater.inflate(R.layout.cas, null);
		Button c=new Button(this);

		adapter.add(b);
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
