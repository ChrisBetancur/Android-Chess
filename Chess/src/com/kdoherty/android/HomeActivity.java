package com.kdoherty.android;

import com.kdoherty.androidchess.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
	}
	
	public void onOnePlayerClick(View view) {
		Intent intent = new Intent(getBaseContext(), ChessActivity.class);
		intent.putExtra("isCpuPlayer", true);
		startActivity(intent);
	}
	
	public void onTwoPlayerClick(View view) {
		Intent intent = new Intent(getBaseContext(), ChessActivity.class);
		intent.putExtra("isCpuPlayer", false);
		startActivity(intent);
	}
	
	
}
