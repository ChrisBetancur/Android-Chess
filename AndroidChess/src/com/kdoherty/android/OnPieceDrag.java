package com.kdoherty.android;

import android.content.Context;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Move;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Piece;

public class OnPieceDrag implements OnDragListener {

	private Board board;
	private int targetRow;
	private int targetCol;
	private Context context;
	private boolean moved = false;

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
			view.setVisibility(View.VISIBLE);
			if (moved) {
				board.toggleSideToMoveUpdate();
				Log.d("kdoherty", "Toggling side to move");
			}
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			break;
		case DragEvent.ACTION_DROP:
			Piece piece = board.getOccupant(startingRow, startingCol);
			Move move = new Move(piece, targetRow, targetCol);
			if (piece.getColor() == board.getSideToMove()
					&& piece.moveTo(board, targetRow, targetCol)) {
				ChessActivity chessContext = (ChessActivity) context;
				board.setLastMove(move);
				if (piece instanceof Pawn) {
					Pawn pawn = (Pawn) piece;
					if (pawn.isPromoting()) {
						Piece promotedTo = chessContext.askPromotion(piece
								.getColor());
						board.setPiece(targetRow, targetCol, promotedTo);
					}
				}
				board.setLastMove(new Move(piece, targetRow, targetCol));
				if (board.isGameOver()) {
					chessContext.showGameOver();
				}
				chessContext.refreshAdapter(board);
				
				moved = true;
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
