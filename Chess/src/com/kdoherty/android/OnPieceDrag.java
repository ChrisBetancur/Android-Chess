package com.kdoherty.android;

import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Move;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Piece;

/**
 * This handles the dragging and dropping of Pieces on the Board view.
 * 
 * @author Kevin Doherty
 * 
 */
class OnPieceDrag implements OnDragListener {

	/** The Board the Piece is being dragged on */
	private Board board;

	/** The row this Piece was dropped on */
	private int targetRow;

	/** The column this Piece was dropped on */
	private int targetCol;

	/** The Context this listener was called from */
	private ChessActivity context;

	/** Did the Piece successfully move? */
	private boolean moved = false;

	/**
	 * Creates a new instance of this.
	 * 
	 * @param context
	 *            The Context this listener was called from
	 * @param baord
	 *            The Board the Piece is being dragged on
	 * @param targetRow
	 *            The row this Piece was dropped on
	 * @param targetCol
	 *            The column this Piece was dropped on
	 */
	OnPieceDrag(ChessActivity context, Board baord, int targetRow, int targetCol) {
		this.context = context;
		this.board = baord;
		this.targetRow = targetRow;
		this.targetCol = targetCol;
	}

	/**
	 * Handles the dragging and dropping of Pieces. Checks if the piece can move
	 * to the Square it is dropped on. If it can it handles passing the turn.
	 */
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
		case DragEvent.ACTION_DRAG_EXITED:
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			view.setVisibility(View.VISIBLE);
			if (moved) {
				board.toggleSideToMove();
				context.toggleTimer();
				context.makeCpuMove();
			}
			break;
		case DragEvent.ACTION_DROP:
			Piece piece = board.getOccupant(startingRow, startingCol);

			if (piece.getColor() == board.getSideToMove()
					&& piece.canMove(board, targetRow, targetCol)) {

				piece.moveTo(board, targetRow, targetCol);

				// Handles Pawn Promotion
				if (piece instanceof Pawn) {
					Pawn pawn = (Pawn) piece;
					if (pawn.isPromoting()) {
						Piece promotedTo = context.askPromotion(piece
								.getColor());
						board.setPiece(targetRow, targetCol, promotedTo);
					}
				}

				board.addMove(new Move(piece, targetRow, targetCol));
				if (board.isGameOver()) {
					context.showGameOver();
				}
				context.refreshAdapter(board);

				moved = true;

				return true;
			} else {
				// Piece could not move
				view.setVisibility(View.VISIBLE);
			}
			break;
		default:
			return false;
		}
		return true;
	}

}
