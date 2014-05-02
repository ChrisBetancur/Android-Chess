package com.kdoherty.chess;

/**
 * @author Kevin Doherty
 * @version 10/14/2013 This represents a chess move. A move has a piece and a
 *          Square to move the piece to
 */
public class Move {

	/** The Piece that is moving */
	private Piece piece;

	/** The row this piece is moving to */
	private int row;

	/** The column this piece is moving to */
	private int col;

	private int startingRow;
	private int startingCol;
	private Piece taken;
	private Castle castle;

	public enum Castle {
		WHITE_LONG, WHITE_SHORT, BLACK_LONG, BLACK_SHORT;
	}

	/**
	 * A Move consists of A Piece and a coordinate where the piece is moving to
	 * 
	 * @param piece
	 *            The Piece that is moving
	 * @param row
	 *            The row this piece is moving to
	 * @param col
	 *            The column this Piece is moving to
	 */
	public Move(Piece piece, int row, int col) {
		this(piece, row, col, null);
	}

	/**
	 * Overloaded constructor to take in a Square instead of row/column if its
	 * more convenient
	 * 
	 * @param piece
	 *            The Piece that is moving
	 * @param square
	 *            The Square this Move's Piece is moving to
	 */
	public Move(Piece piece, Square square) {
		this(piece, square.row(), square.col());
	}

	public Move(Piece piece, int row, int col, Castle castle) {
		this.piece = piece;
		this.startingRow = piece.getRow();
		this.startingCol = piece.getCol();
		this.row = row;
		this.col = col;
		this.castle = castle;
	}

	/**
	 * Gets the piece from this Move
	 * 
	 * @return The Piece that is moving
	 */
	public Piece getPiece() {
		return piece;
	}

	/**
	 * Gets the coordinate of this move represented as a Square
	 * 
	 * @return the coordinate of this move represented as a Square
	 */
	public Square getSq() {
		return new Square(row, col);
	}

	public int row() {
		return row;
	}

	public int col() {
		return col;
	}

	/**
	 * Does this Move equal that object? A Move is equal to an object if: 1. The
	 * object is a Move 2. The two moves contain the same Piece 3. The two moves
	 * are moving that piece to the same Square
	 * 
	 * @return true if this Move equals the input object
	 */
	public boolean equals(Object obj) {
		return obj instanceof Move && (((Move) obj).getSq()).equals(getSq())
				&& ((Move) obj).getPiece().equals(piece);
	}

	/**
	 * Gets the hashCode for this Move
	 * 
	 * @return An integer representation of this Move
	 */
	public int hashCode() {
		return piece.hashCode() + getSq().hashCode();
	}

	/**
	 * Makes this move on the input Board
	 * 
	 * @param b
	 *            The Board to make this move on
	 * @return The piece captured by making this move if there was one otherwise
	 *         null
	 */
	public Piece makeMove(Board b) {
		if (castle != null) {
			((King) piece).moveTo(b, row, col);
			return null;
		}
		b.remove(piece.getRow(), piece.getCol());
		this.taken = b.setPiece(row, col, piece);
		return taken;
	}

	/**
	 * EFFECT: Takes back this move
	 * 
	 * @param b
	 *            The Board to undo this Move on
	 * @param prevRow
	 *            The row from which Piece came from
	 * @param prevCol
	 *            The column from which Piece came from
	 * @param taken
	 *            The Piece which was taken after this Move was made
	 */
	public void undo(Board b, int prevRow, int prevCol, Piece taken) {
		b.remove(row, col);
		b.setPiece(prevRow, prevCol, getPiece());
		if (taken != null) {
			b.setPiece(row, col, taken);
		}
	}

	public void undo(Board b) {
		if (castle == null) {
			b.remove(row, col);
			b.setPiece(startingRow, startingCol, piece);
			if (taken != null) {
				b.setPiece(row, col, taken);
			}
		} else {
			undoCastling(b);
		}
	}

	public void undoCastling(Board b) {
		if (castle == null) {
			throw new NullPointerException("Can't undo castling because this move was not castling");
		}
		b.setPiece(startingRow, startingCol, b.remove(row, col)); // Remove white king and put it back
		switch (castle) {
		case WHITE_LONG:
			b.setPiece(7, 0, b.remove(row, col + 1)); // Remove rook and put it back
			break;
		case WHITE_SHORT:
			b.setPiece(7, 7, b.remove(row, col - 1));
			break;
		case BLACK_LONG:
			b.setPiece(0, 0, b.remove(row, col + 1));
			break;
		case BLACK_SHORT:
			b.setPiece(0, 7, b.remove(row, col - 1));
			break;
		}
		((King) piece).setHasMoved(false);
	}

	/**
	 * Represents this Move as a String Chess accepted notation for castling is
	 * used
	 * 
	 * @return A String representation of this Move
	 */
	public String toString() {
		if (piece instanceof King) {
			if ((getSq().equals(new Square(7, 6)) || getSq().equals(
					new Square(0, 6)))) {
				return "0-0";
			}
			if ((getSq().equals(new Square(7, 2)) || getSq().equals(
					new Square(0, 2)))) {
				return "0-0-0 ";
			}
		}

		return piece.toString() + getSq();
	}
}
