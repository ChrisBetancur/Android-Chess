package com.kdoherty.chess;

import java.util.ArrayList;
import java.util.List;

import com.kdoherty.engine.RookEval;

/**
 * Represents a Rook. A Rook can only move vertically or horizontally.
 * 
 * @author Kevin Doherty
 * @version 10/14/2013
 * 
 */
public class Rook extends Piece {

	/**
	 * Constructor for a Rook sets the Color and initializes hasMoved to start
	 * at false
	 * 
	 * @param color
	 *            The color of this King
	 */
	public Rook(Color color) {
		super(color);
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
	 * @return true if the Piece can move to the input square and false
	 *         otherwise
	 */
	public boolean canMove(Board b, int r, int c) {
		return Board.isInbounds(r, c) && row == r ^ col == c
				&& (b.isEmpty(r, c) || isTaking(b, r, c))
				&& !isBlocked(b, r, c) && !stillInCheck(b, r, c);
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
	 * @return true if this Piece is attacking the input square on the input
	 *         Board and false otherwise
	 */
	public boolean isAttacking(Board b, int r, int c) {
		return Board.isInbounds(r, c) && row == r ^ col == c
				&& (b.isEmpty(r, c) || isTaking(b, r, c))
				&& !isBlocked(b, r, c);
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
	 * @return true if this Piece is defending the input square on the input
	 *         Board and false otherwise
	 */
	public boolean isDefending(Board b, int r, int c) {
		return Board.isInbounds(r, c) && row == r ^ col == c
				&& !isTaking(b, r, c) && !isBlocked(b, r, c)
				&& !stillInCheck(b, r, c);
	}

	/**
	 * This will represent the piece as a String If the piece is white a
	 * lowerCase letter will be used If the piece is black an upperCase letter
	 * will be used
	 * 
	 * @return A String representation of this Piece
	 */
	public String toString() {
		return color == Color.WHITE ? "r" : "R";
	}

	/**
	 * Finds all moves this Piece can make on the input Board
	 * 
	 * @param b
	 *            The Board on which we are getting all moves of this Piece
	 * @return All moves this Piece can make on the input Board
	 */
	public List<Move> getMoves(Board b) {
		List<Move> moves = new ArrayList<Move>();
		// TODO: Don't iterate through all 64 squares
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			for (int j = 0; j < Board.NUM_COLS; j++) {
				if ((i == getRow() || j == getCol()) && canMove(b, i, j)) {
					moves.add(new Move(this, i, j));
				}
			}
		}
		return moves;
	}

	@Override
	public int evaluate(Board board) {
		return RookEval.eval(board, this);
	}
}
