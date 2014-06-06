package com.kdoherty.chess;

import java.util.ArrayList;
import java.util.List;

import com.kdoherty.engine.BishopEval;

/**
 * @author Kevin Doherty
 * 
 */
public class Bishop extends Piece {

	/**
	 * Constructor for Bishop. Sets this Bishop's color
	 * 
	 * @param color
	 *            The color to set this Bishop's color to
	 */
	public Bishop(Color color) {
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
	
	 * @param testCheck boolean
	 * @return true if the Piece can move to the input square and false
	 *         otherwise */
	public boolean canMove(Board b, int r, int c, boolean testCheck) {
		return Board.isInbounds(r, c) && Board.sameDiagonal(row, col, r, c)
				&& (b.isEmpty(r, c) || isTaking(b, r, c))
				&& !isBlocked(b, r, c)
				&& (!testCheck || !stillInCheck(b, r, c));
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
	 *         Board and false otherwise */
	public boolean isDefending(Board b, int r, int c) {
		return Board.isInbounds(r, c) && Board.sameDiagonal(row, col, r, c)
				&& !isTaking(b, r, c) && !isBlocked(b, r, c)
				&& !stillInCheck(b, r, c);
	}

	/**
	 * This will represent the piece as a String If the piece is white a
	 * lowerCase letter will be used If the piece is black an upperCase letter
	 * will be used
	 * 
	
	 * @return A String representation of this Piece */
	public String toString() {
		return color == Color.WHITE ? "b" : "B";
	}

	/**
	 * Finds all moves this Piece can make on the input Board
	 * 
	 * @param b
	 *            The Board on which we are getting all moves of this Piece
	
	 * @return All moves this Piece can make on the input Board */
	@Override
	public List<Move> getMoves(Board b) {
		List<Move> moves = new ArrayList<Move>();
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			for (int j = 0; j < Board.NUM_COLS; j++) {
				if (Board.sameDiagonal(row, col, i, j) && canMove(b, i, j)) {
					moves.add(new Move(b, this, i, j, Move.Type.NORMAL));
				}
			}
		}
		return moves;
	}

	@Override
	public int evaluate(Board board) {
		return new BishopEval(board, this).evaluate();
	}

	@Override
	public int getStartingValue() {
		return BishopEval.START_VALUE;
	}
	
	public Piece clone() {
		Bishop clone = new Bishop(color);
		clone.row = row;
		clone.col = col;
		clone.moveCount = moveCount;
		return clone;
	}
}