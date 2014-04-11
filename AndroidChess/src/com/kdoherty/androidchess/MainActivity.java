package com.kdoherty.androidchess;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.androidchess.R;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Piece;

public class MainActivity extends Activity {

	Board board;

	GridView myGrid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		board = new Board();
		board.fillWithDefaultPieces();
		GridLayout gridLayout = (GridLayout) findViewById(R.id.board_layout);		
		gridLayout.removeAllViews();
		gridLayout.setColumnCount(Board.NUMCOLS);
	    gridLayout.setRowCount(Board.NUMROWS);
		ImageView pieceView;
		Piece piece;
		int resourceId;
		for (int i = 0; i < Board.NUMROWS; i++) {
			for (int j = 0; j < Board.NUMCOLS; j++) {
				if (board.isOccupied(i, j)) {
					piece = board.getOccupant(i, j);
					pieceView = new ImageView(this);
					resourceId = PieceImages.getId(piece);
					pieceView.setImageResource(resourceId);
					GridLayout.LayoutParams params = new GridLayout.LayoutParams();
					params.height = LayoutParams.WRAP_CONTENT;
					params.width = LayoutParams.WRAP_CONTENT;
					params.rightMargin = 5;
					params.topMargin = 5;
					params.setGravity(Gravity.CENTER);
					params.rowSpec = GridLayout.spec(i);
					params.columnSpec = GridLayout.spec(j);
					pieceView.setLayoutParams(params);
					gridLayout.addView(pieceView);
				}
			}
		}
	}
}
