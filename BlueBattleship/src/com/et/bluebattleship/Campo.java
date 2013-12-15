package com.et.bluebattleship;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Campo extends Activity {
	
	private static final byte ID_MSG_ENEMY_FIELD = 2;
	
	public int lunghezzaNave;
	private int[] shipType;
	public Context context;
	public Resources res;
	public Display display;
	public Drawable drawable; 
	public Drawable nave1;
	public Drawable nave2a;
	public Drawable nave2b;
	public Drawable nave3a;
	public Drawable nave3b;
	public Drawable nave3c;
	public Drawable nave2av;
	public Drawable nave2bv;
	public Drawable nave3av;
	public Drawable nave3bv;
	public Drawable nave3cv; 
	private boolean[] pres;
	private boolean naveAttiva;
	private ToggleButton orizzontale;
	private ToggleButton nave_1;
	private ToggleButton nave_2;
	private ToggleButton nave_3;
	public LayoutInflater layoutInflater;
	public GridView grid; 
	public Nave naveDaUno;
	public Nave naveDaDue;
	public Nave naveDaTre;
	public Nave nave;
	public TextView nomeNave;
	public Button eliminaNave;
	public LinearLayout layout;
	private MyAdapter adapter;
	public ToolBox toolBox;
	DisplayMetrics metrics;
	public boolean naveAttiva_1;
	public boolean naveAttiva_2;
	public boolean naveAttiva_3;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_campo);
		shipType=new int[100];
		grid = (GridView)findViewById(R.id.grid);
		layout=(LinearLayout)findViewById(R.id.principale);
		layoutInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
		metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		adapter = new MyAdapter(this,layoutInflater,metrics.widthPixels);
		pres=new boolean[100];
		naveAttiva=false;
		grid.setAdapter(adapter);
		orizzontale=(ToggleButton)findViewById(R.id.toggleButton2);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				if(naveAttiva){
					switch(lunghezzaNave){
					case 1:
						final LinearLayout fill1=(LinearLayout)layoutInflater.inflate(R.layout.layout_per_nave, null);
						naveDaUno=new Nave(1);
						naveAttiva=false;
						nave_1.setClickable(false);
						naveDaUno.settaNave(position);
						
						layout.addView(fill1, 1);
						nomeNave=(TextView)fill1.getChildAt(0);
						nomeNave.setText(naveDaUno.getPosizioni());
						Button eliminaNave1=(Button)fill1.getChildAt(1);
						eliminaNave1.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								naveDaUno.elimina();
								layout.removeViewInLayout(fill1);
								nave_1.setClickable(true);
							}
						});
						
						break;
					case 2:
						
						final LinearLayout fill2=(LinearLayout)layoutInflater.inflate(R.layout.layout_per_nave, null);
						naveDaDue=new Nave(2);
						
						naveAttiva=false;
						nave_2.setClickable(false);
						naveDaDue.settaNave(position);
						try{
						
						}catch(Exception e){}
						nomeNave=(TextView)fill2.getChildAt(0);
						nomeNave.setText(naveDaDue.getPosizioni());
						layout.addView(fill2, 1);
						Button eliminaNave2=(Button)fill2.getChildAt(1);
						eliminaNave2.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								naveDaDue.elimina();
								layout.removeViewInLayout(fill2);
								nave_2.setClickable(true);
							}
						});
						
						break;
					case 3:
						
						final LinearLayout fill3=(LinearLayout)layoutInflater.inflate(R.layout.layout_per_nave, null);
						naveDaTre=new Nave(3);
						naveAttiva=false;
						nave_3.setClickable(false);
						naveDaTre.settaNave(position);
						
						nomeNave=(TextView)fill3.getChildAt(0);
						nomeNave.setText(naveDaTre.getPosizioni());
						layout.addView(fill3, 1);
						Button eliminaNave3=(Button)fill3.getChildAt(1);
						eliminaNave3.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								naveDaTre.elimina();
								layout.removeViewInLayout(fill3);
								nave_3.setClickable(true);
							}
						});
						
						break;
					}
				}
			}
		});
		
		nave_1=(ToggleButton)findViewById(R.id.toggleButton1);
		nave_1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					naveAttiva=true;
					lunghezzaNave=1;
				}	else{
					naveAttiva=false;
				}
				
			}
		});
		
		nave_2=(ToggleButton)findViewById(R.id.toggleButton3);
		nave_2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					naveAttiva=true;
					lunghezzaNave=2;
				}	else{
					naveAttiva=false;
				}
				
			}
		});
		
		nave_3=(ToggleButton)findViewById(R.id.toggleButton4);
		nave_3.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					naveAttiva=true;
					lunghezzaNave=3;
				}	else{
					naveAttiva=false;
					
				}
				
			}
		});	
		
		
		
		Button GO = (Button)findViewById(R.id.bu);
		GO.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toolBox=ToolBox.getInstance();
				toolBox.my_field=pres;
				runMatch();
				finish();
			}
		});
		}
	
	
	public void runMatch(){
		Intent Match=new Intent(this, Match_Enemy.class);
		Intent MyField=new Intent(this, My_Field.class);
		toolBox.campoVirtuale=new boolean[100];
		toolBox.colpite=new boolean[100];
		toolBox.campoVirtuale[0]=true;
		toolBox.mancato_enemy=new boolean[100];

		toolBox.state_enemy_field=new boolean[100];

		
		byte[] sendField = new byte[101];
		for (int i = 1; i < pres.length; i++) {
			if (pres[i - 1] == true) {
				sendField[i] = 1;
			} else {
				sendField[i] = 0;
			}
		}
		sendField[0] = ID_MSG_ENEMY_FIELD;
		toolBox.mBlueBattleshipService.write(sendField);
		if(toolBox.primo) startActivity(Match);
		else startActivity(MyField);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.campo, menu);
		return true;
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
				return pres.length;
			}

			@Override
			public Object getItem(int position) {
				return pres[position];
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			
			public View getView(int position, View convertView, ViewGroup parent) {
				res=getResources();
				drawable = res.getDrawable(R.drawable.grey);
				Drawable blue = getResources().getDrawable(R.drawable.vuoto);
				nave1=getResources().getDrawable(R.drawable.nave_1);
				nave2a=getResources().getDrawable(R.drawable.nave_2_a);
				nave2b=getResources().getDrawable(R.drawable.nave_2_b);
				nave3a=getResources().getDrawable(R.drawable.nave_3_a);
				nave3b=getResources().getDrawable(R.drawable.nave_3_b);
				nave3c=getResources().getDrawable(R.drawable.nave_3_c);
				nave2av=getResources().getDrawable(R.drawable.nave_2_a_v);
				nave2bv=getResources().getDrawable(R.drawable.nave_2_b_v);
				nave3av=getResources().getDrawable(R.drawable.nave_3_a_v);
				nave3bv=getResources().getDrawable(R.drawable.nave_3_b_v);
				nave3cv=getResources().getDrawable(R.drawable.nave_3_c_v);
				//Drawable grey = getResources().getDrawable(R.drawable.grey);
				SquareImageView siv=new SquareImageView(context,pixelW);
		       if(pres[position]) {
		    	   switch (shipType[position]){
		    	   case 1:
		    		   siv.setImageDrawable(nave1);
		    		   break;
		    	   case 21:
		    		   
		    		   siv.setImageDrawable(nave2a);
		    		   
		    		   break;
		    	   case 22:
		    		   
			    		   siv.setImageDrawable(nave2b);
			    		    
		    		   break;
		    	   case 23:
		    		   siv.setImageDrawable(nave2av);
		    		   break;
		    	   case 24:
		    		   siv.setImageDrawable(nave2bv);
		    		   break;
		    	   case 30:
			    		   siv.setImageDrawable(nave3a);
			    		   
		    		   break;
		    	   case 31:
		    		   
			    		   siv.setImageDrawable(nave3b);
			    		    
		    		   break;
		    	   case 32:
		    		  
			    		   siv.setImageDrawable(nave3c);
			    		   
		    		   break;
		    	   case 33:
		    		   siv.setImageDrawable(nave3av);
		    		   break;
		    	   case 34:
		    		   siv.setImageDrawable(nave3bv);
		    		   break;
		    	   case 35:
		    		   siv.setImageDrawable(nave3cv);
		    		   break;
		    	   }
		       }
		       else siv.setImageDrawable(blue);
				return siv;
			}
			
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
	 
	 
	 class Nave{
		 public int grandezza;
		 public int[] posizioni;
		 public Nave(int gra){
			grandezza=gra;
		 }
		 
		 public boolean checkFree(int position,int grandezza){
			 if(pres[position]) return false;
			 switch(grandezza){
			 
			 case 2:
				 if(!orizzontale.isChecked()){
					 if(pres[position+1]) return false;
				 }else{
					 if(pres[position-10]) return false;
				 }
				 break;
			 case 3:
				 if(!orizzontale.isChecked()){
					 if(pres[position+1] || pres[position-1]) return false;
				 }else{
					 if(pres[position-10] || pres[position+10]) return false;
				 }
				 break;
			 }
			 return true;
		 }
		 
		 public void aggiungiPulsanteNave(LayoutInflater layoutInflater,LinearLayout layoutPrincipale){
				LinearLayout layoutDaInserire=(LinearLayout)layoutInflater.inflate(R.layout.layout_per_nave, null);
				layoutPrincipale.addView(layoutDaInserire);
				}
		 
		 
		 public String getPosizioni(){
			 String ret="";
			 for(int i=0;i<grandezza;i++){
				 ret=ret + posizioni[i] + " ";
			 }
			 
			 return ret;
		 }
		 
		 public int[] intGetPosizioni(){
			 return posizioni;
		 }
		 
		 public void elimina(){
			 for(int i=0;i<grandezza;i++){
				 pres[posizioni[i]]=false;
			 }
			 adapter.notifyDataSetChanged();
			 
		 }
		 
		 
		 
		 public void settaNave(int position){
			 
			 posizioni = new int[grandezza];
			 
			 
			 try{
				 switch(grandezza){
					case 1:
						if(checkFree(position,grandezza)){
						pres[position]=true;
						posizioni[0]=position;
						shipType[position]=1;
						adapter.notifyDataSetChanged();
						nave_1.setChecked(false);
						}
						break;
					case 2:
						if(!orizzontale.isChecked()) {
							if((position/10)==((position+1)/10) && checkFree(position, grandezza)){
							pres[position]=true;
							posizioni[0]=position;
							shipType[position]=21;
							pres[position+1]=true;
							posizioni[1]=position+1;
							shipType[position+1]=22;
							}else {
								throw new Exception();
							}
						}
						else {
							if(checkFree(position, grandezza)){
							try{
								
							pres[position-10]=true;
							posizioni[0]=position-10;
							shipType[position-10]=23;
							}catch(Exception e){break;}
							
							pres[position]=true;
							shipType[position]=24;
							posizioni[1]=position;
							}else throw new Exception();
						}
						nave_2.setChecked(false);
						adapter.notifyDataSetChanged();
						
						break;
					case 3:
						if(!orizzontale.isChecked()) {
							if((position/10)==((position+1)/10) && (position/10)==((position-1)/10) && checkFree(position, grandezza)){
								posizioni[0]=position;
								pres[position]=true;
								posizioni[1]=position+1;
							pres[1+position]=true;
							posizioni[2]=position-1;
							pres[position-1]=true;
							shipType[position]=31;
							shipType[position +1]=32;
							shipType[position-1]=30;
							}else {
								throw new Exception();
							}
						}
						else {
							
							if(checkFree(position, grandezza)){
							try{
							pres[position-10]=true;
							posizioni[2]=position-10;
							shipType[position-10]=33;
							}catch(Exception e ){
								break;
							}
							try{
							pres[position+10]=true;
							posizioni[1]=position+10;
							shipType[position+10]=35;
							}
							catch(Exception e){
								
								pres[position-10]=false;
								posizioni[2]=-1;
								shipType[position-10]=33;
								break;}
							pres[position]=true;
							posizioni[0]=position;
							shipType[position]=34;
							}else throw new Exception();
							
							}
						
						nave_3.setChecked(false);
						adapter.notifyDataSetChanged();
						break;
					case 4:
						break;
					case 5:
						break;
					}
				 }catch(Exception e){(Toast.makeText(getApplicationContext(), "Non puoi mettere la nave li", Toast.LENGTH_SHORT)).show();}
		 }
	 }
	 
	
	 
}


