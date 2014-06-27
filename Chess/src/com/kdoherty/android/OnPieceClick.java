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
		Piece piece = board.getOccupant(row, col);
		Piece activePiece = chessContext.getActivePiece();
		boolean firstClick = chessContext.isFirstClick();

		if (firstClick
				|| (piece != null && activePiece != null && piece
						.sameColor(activePiece))) {
			if (piece != null
					&& (!chessContext.isCpuPlayer() && board.getSideToMove() == piece.getColor()
					|| (chessContext.isCpuPlayer() && piece.getColor() != chessContext
							.getCpuColor()))) {
				chessContext.setActivePiece(piece);
				chessContext.refreshAdapter(board);
				if (firstClick) {
					chessContext.toggleClick();
				}
			}
		} else {
			if (activePiece != null
					&& activePiece.getColor() == board.getSideToMove()
					&& activePiece.canMove(board, row, col)) {
				chessContext.passTurn(new Move(board, activePiece, row, col));
			}
			chessContext.toggleClick();
		}
	}

}
