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
	//private List<OggettiUtili> gestisciGrid;
	private boolean[] pres;
	private ToggleButton orientazione;
	GridView grid; 
	private MyAdapter adapter;
	//Drawable imm=getResources().getDrawable(R.drawable.ic_launcher);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campo);
		grid = (GridView)findViewById(R.id.grid);
		adapter = new MyAdapter(this);
		//gestisciGrid=new Vector<OggettiUtili>();
		pres=new boolean[100];
		
		grid.setAdapter(adapter);
		orientazione=(ToggleButton)findViewById(R.id.toggleButton2);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				try{
				TextView v =(TextView)findViewById(R.id.text);
				v.setText("posizione "+position+" id= "+id);
				switch(lunghezzaNave){
				case 1:
					pres[position]=true;
					adapter.notifyDataSetChanged();
					break;
				case 2:
					pres[position]=true;
					if(!orientazione.isChecked()) pres[++position]=true;
					else pres[position-10]=true;
					adapter.notifyDataSetChanged();
					break;
				case 3:
					break;
				case 4:
					break;
				case 5:
					break;
				}
				}catch(Exception e){
					(Toast.makeText(getApplicationContext(), "Non puoi mettere la nave li", Toast.LENGTH_SHORT)).show();
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
	private void refresh() {
		adapter.notifyDataSetChanged();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.campo, menu);
		return true;
	}
	
	
	 class MyAdapter extends BaseAdapter {
		 
			private Context context;
			public String pr;
			public LayoutInflater ff;
			private int cont=0;

			public MyAdapter(Context context) {
				super();
				this.context = context;
				
			}

			
			@Override
			public int getCount() {
				return pres.length;
			}

			@Override
			public Object getItem(int position) {
				return pres[position];
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
		       TextView a=new TextView(context);
		       if(pres[position]) a.setText("*");
		       else a.setText("-");
				return a;
			}
			
		}
	 
	 class OggettiUtili{
		 public int pos;
		 public boolean pres;
		 public OggettiUtili(){
			 pres=false;
		 }
	 }
			

}

	



