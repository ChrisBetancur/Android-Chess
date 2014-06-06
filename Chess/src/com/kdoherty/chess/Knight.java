package com.kdoherty.chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.kdoherty.engine.KnightEval;

/**
 * This class contains the representation of a Knight chess piece. A Knight can
 * move in an "L" in any direction. The long part of the "L" consists of 2
 * squares and the short part consists of 1. This is the only chess piece which
 * can jump pieces.
 * 
 * @author Kevin Doherty
 * 
 * 
 */
public final class Knight extends Piece {

	/**
	 * Maps a Square on a chess board to all possible Squares a Knight could
	 * potentially move to
	 */
	private HashMap<Square, List<Square>> posMovesCache = new HashMap<Square, List<Square>>();

	/**
	 * Constructor for a Knight Sets this Knight's color
	 * 
	 * @param color
	 *            The color to set this Knight's color to
	 */
	public Knight(Color color) {
		super(color);
	}

	public Knight(Color color, int row, int col) {
		super(color, row, col);
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
	public boolean canMove(Board b, int r, int c, boolean testCheck) {
		return Board.isInbounds(r, c) && (b.isEmpty(r, c) || isTaking(b, r, c))
				&& getPossibleSqs().contains(new Square(r, c))
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
	 * 
	 * @return true if this Piece is defending the input square on the input
	 *         Board and false otherwise
	 */
	public boolean isDefending(Board b, int r, int c) {
		return Board.isInbounds(r, c) && !isTaking(b, r, c)
				&& getPossibleSqs().contains(new Square(r, c))
				&& !stillInCheck(b, r, c);
	}

	/**
	 * This will represent the piece as a String If the piece is white a
	 * lowerCase letter will be used If the piece is black an upperCase letter
	 * will be used
	 * 
	 * 
	 * @return A String representation of this Piece
	 */
	public String toString() {
		return color == Color.WHITE ? "n" : "N";
	}

	/**
	 * This will get all potential Squares the Knight could move to. Does not
	 * check if the Squares are inbounds because this is done in canMove
	 * 
	 * 
	 * @return An List of all potential Squares this Knight could move to
	 */
	public List<Square> getPossibleSqs() {
		Square currentSquare = new Square(row, col);
		// TODO: Run perf test and see if helps or not
		if (posMovesCache.containsKey(currentSquare)) {
			return posMovesCache.get(currentSquare);
		}
		ArrayList<Square> possibleSqs = new ArrayList<Square>();
		possibleSqs.add(new Square(row - 2, col + 1));
		possibleSqs.add(new Square(row - 2, col - 1));

		possibleSqs.add(new Square(row - 1, col + 2));
		possibleSqs.add(new Square(row - 1, col - 2));

		possibleSqs.add(new Square(row + 1, col + 2));
		possibleSqs.add(new Square(row + 1, col - 2));

		possibleSqs.add(new Square(row + 2, col + 1));
		possibleSqs.add(new Square(row + 2, col - 1));
		posMovesCache.put(currentSquare, possibleSqs);
		return possibleSqs;
	}

	/**
	 * Finds all moves this Piece can make on the input Board
	 * 
	 * @param b
	 *            The Board on which we are getting all moves of this Piece
	 * 
	 * @return All moves this Piece can make on the input Board
	 */
	public List<Move> getMoves(Board b) {
		List<Move> moves = new ArrayList<Move>();
		for (Square s : getPossibleSqs()) {
			if (canMove(b, s.row(), s.col()))
				moves.add(new Move(b, this, s, Move.Type.NORMAL));
		}
		return moves;
	}

	@Override
	public int evaluate(Board board) {
		return new KnightEval(board, this).evaluate();
	}

	@Override
	public int getStartingValue() {
		return KnightEval.START_VALUE;
	}

	@Override
	public Piece clone() {
		Knight clone = new Knight(color);
		clone.row = row;
		clone.col = col;
		clone.moveCount = moveCount;
		clone.posMovesCache = posMovesCache;
		return clone;
	}
}
