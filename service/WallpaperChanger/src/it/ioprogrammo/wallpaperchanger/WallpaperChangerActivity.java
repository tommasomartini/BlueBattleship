package it.ioprogrammo.wallpaperchanger;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WallpaperChangerActivity extends Activity {
	
	private Button bStartService;
	
	private Button bStopService;
	
	private Button bFinish;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// Recupera i riferimenti al pulsanti
		bStartService = (Button) findViewById(R.idBottoni.avvia);
		bStopService = (Button) findViewById(R.idBottoni.arresta);
		bFinish = (Button) findViewById(R.idBottoni.esci);
		// Imposta lo stato in base al servizio.
		bStartService.setEnabled(!WallpaperChangerService.STARTED);
		bStopService.setEnabled(WallpaperChangerService.STARTED);
		// Aggiunge i gestori degli eventi.
		bStartService.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startWallpaperChangerService();
			}
		});
		bStopService.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopWallpaperChangerService();
			}
		});
		bFinish.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void startWallpaperChangerService() {
		bStartService.setEnabled(false);
		bStopService.setEnabled(true);
		startService(new Intent(this, WallpaperChangerService.class));
	}

	private void stopWallpaperChangerService() {
		bStartService.setEnabled(true);
		bStopService.setEnabled(false);
		stopService(new Intent(this, WallpaperChangerService.class));
	}

}