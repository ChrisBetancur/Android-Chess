package com.kdoherty.android;


import android.view.View;
import android.view.View.OnClickListener;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Move;
import com.kdoherty.chess.Piece;


public class OnPieceClick implements OnClickListener {

	private ChessActivity chessContext;
	private Board board;
	private int row;
	private int col;

	public OnPieceClick(ChessActivity context, Board board, int row, int col) {
		this.chessContext = context;
		this.board = board;
		this.row = row;
		this.col = col;
	}

	@Override
	public void onClick(View v) {
		if (chessContext.isGameOver() || chessContext.isCpuMove()) {
			return;
		}
		if (chessContext.isFirstClick()) {
			Piece piece = board.getOccupant(row, col);
			if (piece != null) {
				chessContext.setActivePiece(piece);
				chessContext.toggleClick();
			}
		} else {
			Piece activePiece = chessContext.getActivePiece();
			if (activePiece != null
					&& activePiece.getColor() == board.getSideToMove()
					&& activePiece.canMove(board, row, col)) {
				// TODO: Not passing move type here. This will break add to
				// taken Pieces for enPoissant
				// and undoing of all non-normal moves
				chessContext.passTurn(new Move(activePiece, row, col));
			}
			chessContext.toggleClick();
		}
	}

}
