package it.ioprogrammo.wallpaperchanger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

public class WallpaperChangerService extends Service {

	public static boolean STARTED = false;

	private String[] availableWallpapers;

	private int currentWallpaperIndex;

	private Timer timer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// Cambia il flag.
		STARTED = true;
		// Carica l'elenco dei wallpaper disponibili.
		AssetManager assets = getAssets();
		try {
			availableWallpapers = assets.list("wallpapers");
		} catch (IOException e) {
			Log.e("WallpaperChangerService", "Impossibile elencare i wallpaper disponibili", e);
		}
		currentWallpaperIndex = -1;
		// Ci sono wallpaper disponibili?
		if (availableWallpapers != null && availableWallpapers.length > 0) {
			// Crea ed avvia il task per il cambio ciclico del wallpaper.
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					nextWallpaper();
				}
			};
			timer = new Timer();
			timer.schedule(task, 0, 60000);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		STARTED = false;
		timer.cancel();
		timer = null;
	}

	private void nextWallpaper() {
		// Avanza l'indice del wallpaper corrente.
		currentWallpaperIndex++;
		// Controlla che sia nel limite.
		if (currentWallpaperIndex == availableWallpapers.length) {
			// Riparte dal primo.
			currentWallpaperIndex = 0;
		}
		// Carica il wallpaper da impostare.
		String currentWallpaper = "wallpapers/" + availableWallpapers[currentWallpaperIndex];
		Bitmap bitmap = null;
		InputStream inputStream = null;
		try {
			AssetManager assets = getAssets();
			inputStream = assets.open(currentWallpaper);
			bitmap = BitmapFactory.decodeStream(inputStream);
		} catch (IOException e) {
			Log.e("WallpaperChangerService", "Impossibile caricare il wallpaper " + currentWallpaper, e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Throwable t) {
				}
			}
		}
		if (bitmap != null) {
			// Imposta lo sfondo.
			WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
			try {
				wallpaperManager.setBitmap(bitmap);
			} catch (Throwable t) {
				Log.e("WallpaperChangerService", "Impossibile impostare il wallpaper " + currentWallpaper, t);
			}
		}
	}

}
