package com.kdoherty.chess;

/**
 * @author Kevin Doherty
 * @version 10/14/2013 A Square is an immutable representation of a coordinate
 *          on the chessBoard
 */
public final class Square {

	/** Row coordinate of this Square */
	private final int row;

	/** Column coordinate of this Square */
	private final int col;

	/**
	 * Constructor for Square
	 * 
	 * @param row
	 *            Sets this squares row to this value
	 * @param col
	 *            Sets this squares column to this value
	 */
	public Square(int row, int col) {
		this.row = row;
		this.col = col;
	}

	/**
	 * A user friendly constructor for Square using chess coordinate notation
	 * instead of 2D array notation
	 * 
	 * @param col
	 *            The char representing the column
	 * @param row
	 *            The integer representing this row
	 */
	public Square(char col, int row) {
		if (row > 8 || row < 1) {
			throw new IllegalArgumentException(
					"Input row input. Should be between 1 and 8 but is " + row);
		}
		this.row = 8 - row;
		switch (col) {
		case 'a':
			this.col = 0;
			break;
		case 'b':
			this.col = 1;
			break;
		case 'c':
			this.col = 2;
			break;
		case 'd':
			this.col = 3;
			break;
		case 'e':
			this.col = 4;
			break;
		case 'f':
			this.col = 5;
			break;
		case 'g':
			this.col = 6;
			break;
		case 'h':
			this.col = 7;
			break;
		default:
			throw new IllegalArgumentException(
					"Invalid column input. Should be between 'a' and 'h' but is "
							+ col);
		}
	}

	/**
	 * getter for the row coordinate of this Square
	 * 
	 * @return The row coordinate of this Square
	 */
	public int row() {
		return row;
	}

	/**
	 * getter for the column coordinate of this Square
	 * 
	 * @return The column coordinate of this Square
	 */
	public int col() {
		return col;
	}

	/**
	 * Does this square equal that object? A square is equal to an object if 1.
	 * The object is a square 2. They contain the same chess board coordinate
	 * 
	 * @return true if this Square equals the input object
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Square)) {
			return false;
		}
		Square that = (Square) obj;
		return that.row == row && that.col == col;
	}

	/**
	 * Gets the hashCode for this Square
	 * 
	 * @return An integer representation of this Square
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	/**
	 * Forms a String to represent this Square
	 * 
	 * @return A String representation of this Square
	 */
	@Override
	public String toString() {
		String s = "";
		switch (col) {
		case (0):
			s += "a";
			break;
		case (1):
			s += "b";
			break;
		case (2):
			s += "c";
			break;
		case (3):
			s += "d";
			break;
		case (4):
			s += "e";
			break;
		case (5):
			s += "f";
			break;
		case (6):
			s += "g";
			break;
		case (7):
			s += "h";
			break;
		}
		s += (8 - row);
		return s;
	}
	
	public int toNum() {
		return row * 8 + col;
	}
}
