package com.kdoherty.androidchess;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.kdoherty.androidchess.R;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;

public class ChessActivity extends Activity {
	
	// TODO: Pawn Promotion
	// TODO: Better than refreshing adapter?

	private SquareAdapter adapter;

	private SquareGridView boardView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_chess);
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
		adapter = new SquareAdapter(this, board);
		boardView.setAdapter(adapter);
	}
	
//	public String askPawnPromotion() {
//		
//	}

	public void showGameOver() {
		Board board = adapter.getBoard();
		Color sideToMove = board.getSideToMove();
		if (board.isCheckMate(sideToMove)) {
			Toast.makeText(this, "CHECKMATE! " + sideToMove.opp() + " WINS", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, "DRAW!", Toast.LENGTH_LONG).show();
		}
		
	}
}
