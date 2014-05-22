package com.kdoherty.chess;

import java.util.ArrayList;
import java.util.List;

import com.kdoherty.engine.PawnEval;

public final class Pawn extends Piece {

	/** The starting row for pawns */
	private int homeRow;

	/** The forward direction for this Pawn */
	private int forward;

	private List<Move> moves;

	/**
	 * Constructor for a Pawn Sets this Pawn's color Initializes homeRow and
	 * forward based on the color
	 * 
	 * @param color
	 *            The color to set this Pawn's color to
	 */
	public Pawn(Color color) {
		super(color);
		homeRow = color == Color.WHITE ? 6 : 1;
		forward = color == Color.WHITE ? -1 : 1;
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
	public boolean canMove(Board b, int r, int c, boolean testCheck) {
		return Board.isInbounds(r, c)
				&& (canMoveOne(b, r, c) || canMoveTwo(b, r, c)
						|| canTakeNormally(b, r, c) || canEnPoissant(b, r, c))
				&& (!testCheck || !stillInCheck(b, r, c));
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
		return row + forward == r && (col + 1 == c || col - 1 == c)
				&& (b.isEmpty(r, c) || isTaking(b, r, c));
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
		return row + forward == r && (col + 1 == c || col - 1 == c)
				&& !isTaking(b, r, c) && !stillInCheck(b, r, c);
	}

	/**
	 * This will represent the piece as a String If the piece is white a
	 * lowerCase letter will be used If the piece is black an upperCase letter
	 * will be used
	 * 
	 * @return A String representation of this Piece
	 */
	public String toString() {
		return color == Color.WHITE ? "p" : "P";
	}

	/**
	 * Finds all moves this Piece can make on the input Board
	 * 
	 * @param b
	 *            The Board on which we are getting all moves of this Piece
	 * @return All moves this Piece can make on the input Board
	 */
	@Override
	public List<Move> getMoves(Board b) {
		if (moves != null) {
			return moves;
		}
		int finalRow = color == Color.WHITE ? 0 : 7;
		List<Move> moves = new ArrayList<Move>();
		for (Square s : getPossibleSqs()) {
			int sRow = s.row();
			int sCol = s.col();
			if (canMove(b, sRow, sCol)) {
				if (s.col() != col && b.isEmpty(sRow, sCol)) {
					// En Poissant
					moves.add(new Move(this, sRow, sCol, Move.Type.EN_POISSANT));
				} else if (s.row() != finalRow) {
					// Normal Move
					moves.add(new Move(this, s));
				} else {
					// Pawn Promotion
					moves.add(new Move(new Queen(color, row, col), sRow, sCol,
							Move.Type.PAWN_PROMOTION));
					moves.add(new Move(new Knight(color, row, col), sRow, sCol,
							Move.Type.PAWN_PROMOTION));
				}
			}

		}
		return moves;
	}

	/**
	 * Produces an ArrayList which contains all squares this Piece could
	 * potentially move to
	 * 
	 * @return An ArrayList of Squares which this Piece could potentially move
	 *         to
	 */
	private ArrayList<Square> getPossibleSqs() {
		ArrayList<Square> posSqs = new ArrayList<Square>();
		posSqs.add(new Square(row + forward, col));
		posSqs.add(new Square(row + 2 * forward, col));
		posSqs.add(new Square(row + forward, col + 1));
		posSqs.add(new Square(row + forward, col - 1));
		return posSqs;
	}

	/**
	 * Can this Pawn move one Square ahead to the input row/column
	 * 
	 * @param b
	 *            The board we are checking if this Pawn can move one Square on
	 * @param r
	 *            The row we are checking if this Pawn canMove one space and be
	 *            at
	 * @param c
	 *            The column we are checking if this Pawn canMove one space and
	 *            be at
	 * @return true if this Pawn canMove one space and be at the input
	 *         row/column
	 */
	private boolean canMoveOne(Board b, int r, int c) {
		return row + forward == r && col == c && b.isEmpty(row + forward, col);
	}

	/**
	 * Can this Pawn move two Square ahead to the input row/column
	 * 
	 * @param b
	 *            The board we are checking if this Pawn can move two Square on
	 * @param r
	 *            The row we are checking if this Pawn canMove two spaces and be
	 *            at
	 * @param c
	 *            The column we are checking if this Pawn canMove two spaces and
	 *            be at
	 * @return true if this Pawn canMove two spaces and be at the input
	 *         row/column
	 */
	private boolean canMoveTwo(Board b, int r, int c) {
		return row + 2 * forward == r && col == c
				&& b.isEmpty(row + forward, col)
				&& b.isEmpty(row + 2 * forward, col) && row == homeRow;
	}

	/**
	 * Can this pawn take enPoissant style at the input row/column.
	 * 
	 * @param b
	 *            The Board we are checking if this Pawn can enPoissant on
	 * @param r
	 *            The row of the potential enPoissant Square
	 * @param c
	 *            The column of the potential enPoissant Square
	 * @return true if this Pawn can enPoissant to the input row/column
	 */
	private boolean canEnPoissant(Board b, int r, int c) {
		int enPoissantRow = color == Color.WHITE ? 3 : 4;
		return row == enPoissantRow
				&& new Square(r, c).equals(b.getEnPoissantSq())
				&& row + forward == r && (col + 1 == c || col - 1 == c);
	}

	/**
	 * Can this pawn take normally on the input row/column
	 * 
	 * @param b
	 *            The Board we are checking if this Pawn can take on
	 * @param r
	 *            The row of the potential capture Square
	 * @param c
	 *            The column of the potential capture Square
	 * @return true if this Pawn can take a piece on the input row/column
	 */
	private boolean canTakeNormally(Board b, int r, int c) {
		return row + forward == r && (col + 1 == c || col - 1 == c)
				&& isTaking(b, r, c);
	}

	/**
	 * EFFECT: If this Pawn can move to the input square, it is moved Overridden
	 * move to method for Pawn to account for situations like enPoissant,
	 * promotions,
	 */
	@Override
	public void moveTo(Board b, int r, int c) {
		incrementMoveCount();
		if (row + 2 * forward == r) {
			b.setEnPoissantSq(new Square(row + forward, col));
		}
		if (canEnPoissant(b, r, c) && row + forward == r
				&& (col + 1 == c || col - 1 == c)) {
			b.movePiece(row, col, r, c);
			b.remove(r - forward, c);
			b.setEnPoissantSq(null);
		} else {
			b.movePiece(row, col, r, c);
			b.setEnPoissantSq(null);
		}
		if (isPromoting()) {
			// TODO
		}
	}

	/**
	 * Has this Pawn reached the final rank?
	 * 
	 * @return true if this Pawn has reached the final rank
	 */
	public boolean isPromoting() {
		int finalRank = color == Color.WHITE ? 0 : 7;
		return row == finalRank;
	}

	/**
	 * EFFECT: Promotes this pawn to a Queen
	 * 
	 * @param b
	 *            The Board to promote this Pawn to a Queen on
	 */
	public void promoteToQueen(Board b) {
		b.remove(row, col);
		b.setPiece(row, col, new Queen(color));
	}

	/**
	 * EFFECT: Promotes this pawn to a Knight
	 * 
	 * @param b
	 *            The Board to promote this Pawn to a Knight on
	 */
	public void promoteToKnight(Board b) {
		b.remove(row, col);
		b.setPiece(row, col, new Knight(color));
	}

	@Override
	public int evaluate(Board board) {
		return PawnEval.eval(board, this);
	}
}
