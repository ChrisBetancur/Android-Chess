package com.kdoherty.android;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Piece;

public class OnPieceDrag implements OnDragListener {

	private Board board;
	private int targetRow;
	private int targetCol;
	private ChessActivity context;
	private boolean moved = false;

	public OnPieceDrag(ChessActivity context, Board baord, int targetRow,
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
				Color sideToMove = board.getSideToMove();
				//context.toggleTimer(sideToMove);
				board.toggleSideToMove();
				context.makeCpuMove();
			}
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			break;
		case DragEvent.ACTION_DROP:
			Piece piece = board.getOccupant(startingRow, startingCol);
			if (piece.getColor() == board.getSideToMove()
					&& piece.canMove(board, targetRow, targetCol)) {
				piece.moveTo(board, targetRow, targetCol);
				ChessActivity chessContext = (ChessActivity) context;
				if (piece instanceof Pawn) {
					Pawn pawn = (Pawn) piece;
					if (pawn.isPromoting()) {
						Piece promotedTo = chessContext.askPromotion(piece
								.getColor());
						board.setPiece(targetRow, targetCol, promotedTo);
					}
				}
				board.addMove(new Move(piece, targetRow, targetCol));
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
