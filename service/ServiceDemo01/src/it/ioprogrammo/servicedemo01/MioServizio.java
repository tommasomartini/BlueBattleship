package it.ioprogrammo.servicedemo01;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MioServizio extends Service {
	
	private Timer timer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("ServiceDemo01", "Servizio avviato");
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Log.i("ServiceDemo01", "Servizio in esecuzione");
			}
		};
		timer = new Timer();
		timer.schedule(task, 0, 5000);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		timer = null;
		Log.i("ServiceDemo01", "Servizio arrestato");
	}

}
