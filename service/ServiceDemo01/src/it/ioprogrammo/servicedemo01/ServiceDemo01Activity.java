package it.ioprogrammo.servicedemo01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ServiceDemo01Activity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Button bottoneAvvia = new Button(this);
		bottoneAvvia.setText("Avvia il servizio");
		bottoneAvvia.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				avviaServizio();
			}
		});
		Button bottoneArresta = new Button(this);
		bottoneArresta.setText("Arresta il servizio");
		bottoneArresta.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				arrestaServizio();
			}
		});
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(bottoneAvvia);
		layout.addView(bottoneArresta);
		setContentView(layout);
		Log.i("ServiceDemo01", "Attivita creata");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("ServiceDemo01", "Attivita distrutta");
	}

	private void avviaServizio() {
		startService(new Intent(this, MioServizio.class));
	}

	private void arrestaServizio() {
		stopService(new Intent(this, MioServizio.class));
	}

}