package com.kdoherty.engine;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Piece;

/**
 * Abstract Piece evaluation class. Determines a rating for a Piece on a Board
 * depending on its position on the Board and its position relative to to other
 * Pieces on the Board.
 * 
 * 
 * @author Kevin Doherty
 * 
 */
abstract class PieceEval {

	/** The Board to evaluate the input Piece on */
	protected Board board;

	/** The Piece to evaluate on the input Board */
	protected Piece piece;

	/** Cached value of the Color of the input Piece */
	protected Color color;

	/** Cached value of the row of the input Piece */
	protected int row;

	/** Cached value of the col of the input Piece */
	protected int col;

	/**
	 * Constructor for PieceEval. Initializes this PieceEval's Board and Piece
	 * and caches commonly used values.
	 * 
	 * @param board
	 *            The Board to evaluate the input Piece on
	 * @param piece
	 *            The Piece to evaluate on the input Board
	 */
	PieceEval(Board board, Piece piece) {
		this.board = board;
		this.piece = piece;
		this.color = piece.getColor();
		this.row = piece.getRow();
		this.col = piece.getCol();
	}

	/**
	 * Evaluates this PieceEval's Piece on its Board.
	 * 
	 * @return An integer rating of how good a Piece is on a Board. The higher
	 *         the rating the better the placement.
	 */
	public abstract int evaluate();

	/**
	 * A Piece is blocking a central Pawn if the Pawn is central, still on
	 * its home Square, and this PieceEval's Piece is on the Square directly in
	 * front of it. A Pawn is a central Pawn if it is on the "d" or "e" file
	 * (3rd and 4th column on the 2D Board array).
	 * 
	 * @return Is this PieceEval's Piece blocking a center Pawn?
	 */
	protected boolean isBlockingCenterPawn() {
		int blockingRow = color == Color.WHITE ? 5 : 2;
		int backwards = color == Color.WHITE ? 1 : -1;
		Piece piece = board.getOccupant(blockingRow + backwards, col);
		return row == blockingRow && piece instanceof Pawn
				&& piece.getColor() == color;
	}
}
