package com.kdoherty.androidchess;

import android.content.Context;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Piece;

public class OnPieceDrag implements OnDragListener {

	private Board board;
	private int targetRow;
	private int targetCol;
	private Context context;

	public OnPieceDrag(Context context, Board baord, int targetRow,
			int targetCol) {
		this.context = context;
		this.board = baord;
		this.targetRow = targetRow;
		this.targetCol = targetCol;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		int action = event.getAction();
		PieceImageView view = (PieceImageView) event.getLocalState();
		int startingRow = view.getRow();
		int startingCol = view.getCol();
		switch (action) {
		case DragEvent.ACTION_DRAG_STARTED:
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			break;
		case DragEvent.ACTION_DROP:
			Piece piece = board.getOccupant(startingRow, startingCol);
			if (piece.getColor() == board.getSideToMove()
					&& piece.moveTo(board, targetRow, targetCol)) {
				board.toggleSideToMove();
				MainActivity mainContext = (MainActivity) context;
				mainContext.refreshAdapter(board);
				return true;
			} else {
				view.setVisibility(View.VISIBLE);
			}
			break;
		default:
			return false;
		}
		return true;
	}

}
