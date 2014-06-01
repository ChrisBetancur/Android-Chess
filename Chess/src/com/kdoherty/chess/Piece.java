package com.kdoherty.chess;

import java.util.List;

/**
 * This class represents a Chess Piece This is a superClass for Pawn, Knight,
 * Bishop, Rook, Queen, and King
 * 
 * @author Kevin Doherty
 * 
 */
public abstract class Piece {

	// TODO: Make Pieces singleton and not know their location or change the
	// board to a list of pieces

	/** The row where this Piece is located */
	protected int row;

	/** The column where this Piece is located */
	protected int col;

	/** The color of this Piece */
	protected final Color color;

	/** The number of times this Piece has moved */
	protected int moveCount = 0;

	public Piece(Color color, Square square) {
		this(color, square.row(), square.col());
	}

	public Piece(Color color, int row, int col) {
		this.color = color;
		this.row = row;
		this.col = col;
	}

	public Piece(Color color) {
		this(color, 0, 0);
	}

	/**
	 * Sets this piece's row to the input integer
	 * 
	 * @param row
	 *            The integer to set this pieces row to
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * Sets this piece's column to the input integer
	 * 
	 * @param col
	 *            The integer to set this pieces column to
	 */
	public void setCol(int col) {
		this.col = col;
	}

	public void incrementMoveCount() {
		this.moveCount++;
	}

	public void decrementMoveCount() {
		this.moveCount--;
	}

	public int getMoveCount() {
		return moveCount;
	}

	public boolean hasMoved() {
		return moveCount != 0;
	}

	/**
	 * Gets the row coordinate of this Piece
	 * 
	 * 
	 * @return The row coordinate of this Piece
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Gets the column coordinate of this Piece
	 * 
	 * 
	 * @return The column coordinate of this Piece
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Gets the Square which this Piece is on
	 * 
	 * 
	 * @return The Square which this Piece is on
	 */
	public Square getSq() {
		return new Square(row, col);
	}

	/**
	 * Gets the Color of this Piece
	 * 
	 * 
	 * @return The Color of this Piece
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Can this piece move on the input board to the input square?
	 * 
	 * @param b
	 *            The Board the piece is checking if it can move on
	 * @param r
	 *            The row we are checking if the piece can move to
	 * @param c
	 *            The column we are checking if the piece can move to
	 * 
	 * @param testCheck
	 *            boolean
	 * @return true if the Piece can move to the input square and false
	 *         otherwise
	 */
	public abstract boolean canMove(Board b, int r, int c, boolean testCheck);

	public boolean canMove(Board board, int r, int c) {
		return canMove(board, r, c, true);
	}

	/**
	 * Is this Piece attacking the input square? Note it is still attacking the
	 * square even if it is pinned to it's king
	 * 
	 * @param b
	 *            The Board we are checking if the Piece is attacking a square
	 *            on
	 * @param r
	 *            The row we are checking if this Piece is attacking
	 * @param c
	 *            The row we are checking if this Piece is attacking
	 * 
	 * @return true if this Piece is attacking the input square on the input
	 *         Board and false otherwise
	 */
	public boolean isAttacking(Board b, int r, int c) {
		return canMove(b, r, c, false);
	}

	/**
	 * Method evaluate.
	 * 
	 * @param b
	 *            Board
	 * @return int
	 */
	public abstract int evaluate(Board b);

	public abstract int getStartingValue();

	/**
	 * Moves this Piece to the input square if it can move there
	 * 
	 * @param b
	 *            The Board we are moving this Piece on
	 * @param r
	 *            The row we are moving this Piece to
	 * @param c
	 *            The column we are moving this Piece to
	 */
	public void moveTo(Board b, int r, int c) {
		b.movePiece(row, col, r, c);
		incrementMoveCount();
		b.setEnPoissantSq(null);
	}

	/**
	 * Is this Piece defending the input square? Note it is not defending the
	 * square if it is pinned to it's king, or if the input square contains a
	 * Piece of the opposite color
	 * 
	 * @param b
	 *            The Board we are checking if this Piece is defending a square
	 *            on
	 * @param r
	 *            The row we are checking if it is defended by this piece
	 * @param c
	 *            The column we are checking if it is defended by this piece
	 * 
	 * @return true if this Piece is defending the input square on the input
	 *         Board and false otherwise
	 */
	public abstract boolean isDefending(Board b, int r, int c);

	/**
	 * Finds all moves this Piece can make on the input Board
	 * 
	 * @param b
	 *            The Board on which we are getting all moves of this Piece
	 * 
	 * @return All moves this Piece can make on the input Board
	 */
	public abstract List<Move> getMoves(Board b);
	
	/**
	 * Generates a clone of this Piece such that this.equals(this.clone()) is true but
	 * this == this.clone() is false
	 * @return A clone of this Piece
	 */
	@Override
	public abstract Piece clone();

	/**
	 * A piece equals another Object if they are: 
	 * 1. Both Pieces of the same type of Piece 
	 * 2. Have the same color 
	 * 3. Are located on the same row
	 * 4. Are located on the same column 
	 * 5. Have the same moveCount
	 * @param obj
	 *            The object we are checking if it is equal to this
	 * @return true if this piece is equal to the input object
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Piece other = (Piece) obj;

		return color == other.color && row == other.row && col == other.col
				&& moveCount == other.moveCount;
	}

	/**
	 * Gets an integer representation of this Piece
	 * 
	 * 
	 * @return an integer representation of this Piece
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		result = prime * result + moveCount;
		result = prime * result + row;
		return result;
	}

	/**
	 * This will represent the piece as a String If the piece is white a
	 * lowerCase letter will be used If the piece is black an upperCase letter
	 * will be used
	 * 
	 * 
	 * @return A String representation of this Piece
	 */
	public abstract String toString();

	/**
	 * A piece is taking if the input square contains a Piece of the opposite
	 * color
	 * 
	 * @param b
	 *            The board to check if the piece is taking on
	 * @param r
	 *            The row coordinate of the square to check if this Piece is
	 *            taking another piece on
	 * @param c
	 *            The column coordinate of the square to check if this Piece is
	 *            taking another piece on
	 * 
	 * @return true if this Piece would take a piece if it moved to the
	 *         coordinate
	 */
	public boolean isTaking(Board b, int r, int c) {
		return b.isOccupied(r, c) && b.getOccupant(r, c).getColor() != color;
	}

	/**
	 * Is this piece blocked going from its current square to the input square
	 * on the input board
	 * 
	 * @param b
	 *            The board we are checking if this piece is blocked on
	 * @param r
	 *            The row coordinate of the intended Square
	 * @param c
	 *            The column coordinate of the intend Square
	 * 
	 * @return true if this piece is blocked going from its current square to
	 *         the input square
	 */
	public boolean isBlocked(Board b, int r, int c) {
		for (Square square : Board.getBtwnSqs(getSq(), new Square(r, c))) {
			if (b.isOccupied(square.row(), square.col())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Can this Piece move to the input coordinate without putting its King in
	 * check?
	 * 
	 * @param b
	 *            The Board this Piece is trying to move on
	 * @param r
	 *            The row this Piece is trying to move to
	 * @param c
	 *            The column this Piece is trying to move to
	 * 
	 * @return true if this Piece can move without putting its King in check
	 */

	public boolean stillInCheck(Board b, int r, int c) {
		boolean stillInCheck = true;
		Move m = new Move(this, r, c);
		m.make(b);
		if (!(b.kingInCheck(color))) {
			stillInCheck = false;
		}
		m.unmake(b);
		return stillInCheck;
	}
}
