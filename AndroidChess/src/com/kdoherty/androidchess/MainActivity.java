package com.kdoherty.androidchess;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;

import com.example.androidchess.R;
import com.kdoherty.chess.Board;

public class MainActivity extends Activity {

	private SquareAdapter adapter;

	private SquareGridView boardView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_main);
		initBoard();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void initBoard() {
		adapter = new SquareAdapter(this);
		boardView = (SquareGridView) findViewById(R.id.chessboard);
		boardView.setAdapter(adapter);
	}
	
	public void refreshAdapter(Board board) {
		boardView.setAdapter(new SquareAdapter(this, board));
	}
}
