package com.et.bluebattleship;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Match_Win extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match__win);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.match__win, menu);
		return true;
	}

}
