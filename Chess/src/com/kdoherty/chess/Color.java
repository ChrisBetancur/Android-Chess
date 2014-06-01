package com.kdoherty.chess;

/**
 * Enumeration for the colors White and Black used to represent the Colors of
 * the Pieces
 * 
 * @author Kevin Doherty
 * 
 */
public enum Color {

	WHITE, BLACK;

	/**
	 * Gets the opposite Color of this Color
	 * 
	 * @return The opposite Color of this Color
	 */
	public Color opp() {
		return this == WHITE ? BLACK : WHITE;
	}
}
