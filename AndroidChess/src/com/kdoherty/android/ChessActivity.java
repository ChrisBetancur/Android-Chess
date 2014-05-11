package com.kdoherty.android;

import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.kdoherty.androidchess.R;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;
import com.kdoherty.chess.Piece;
import com.kdoherty.chess.Queen;
import com.kdoherty.engine.CpuPlayer;

public class ChessActivity extends Activity implements Observer {

	// TODO: Pawn Promotion
	// TODO: Better than refreshing adapter?

	private SquareAdapter adapter;

	private SquareGridView boardView;
	
	private CpuPlayer player = new CpuPlayer();
	
	private static boolean IS_CPU_PLAYER = true;
	private static final Color CPU_COLOR = Color.BLACK;
	private static final int DEPTH = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_chess);
		initBoard();
		if (IS_CPU_PLAYER && CPU_COLOR == Color.WHITE) {
			makeCpuMove();
		}
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
		adapter.getBoard().addObserver(this);
	}

	public void refreshAdapter(Board board) {
		adapter = new SquareAdapter(this, board);
		boardView.setAdapter(adapter);
	}

	public Piece askPromotion(Color color) {
		//TODO: Need pop up to determine type of Piece to promote to
		return new Queen(color);
	}

	public void showGameOver() {
		Board board = adapter.getBoard();
		Color sideToMove = board.getSideToMove();
		if (board.isCheckMate(sideToMove)) {
			Toast.makeText(this, "CHECKMATE! " + sideToMove.opp() + " WINS",
					Toast.LENGTH_LONG).show();
		} else if (board.isDraw(sideToMove)) {
			Toast.makeText(this, "DRAW!", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		if (IS_CPU_PLAYER && data instanceof Color) {
			Color sideToMove = (Color) data;
			if (sideToMove == CPU_COLOR) {
				makeCpuMove();
			}
		}
	}
	
	public void makeCpuMove() {
		Board board = adapter.getBoard();
		Move move = player.negaMaxMove(board, CPU_COLOR, DEPTH);
		if (move == null) {
			Toast.makeText(this, "YOU WIN, GOOD GAME!",
					Toast.LENGTH_LONG).show();
			return;
		}
		move.makeMove(board);
		refreshAdapter(board);
		board.toggleSideToMove();
		if (board.isGameOver()) {
			showGameOver();
		}
	}
}
