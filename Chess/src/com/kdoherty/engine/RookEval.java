package com.kdoherty.engine;

import com.kdoherty.chess.Piece;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Queen;
import com.kdoherty.chess.Rook;
import com.kdoherty.chess.Square;

/**
 * This class is responsible for evaluating a Rook. It takes into account its
 * position on the Board and its position relative to other Pieces on the Board.
 * 
 * @author Kevin Doherty
 * 
 */
public class RookEval extends PieceEval {

	/** Starting point value assigned to a Rook */
	public static int START_VALUE = 500;

	/** Bonus added for being on an open file (column with no Pawns) */
	private static int OPEN_FILE_BONUS = 20;

	/**
	 * Bonus added for being on an open file (column with no Pawns of the Rook's
	 * color and at least one Pawn of the opposing Color)
	 */
	private static int SEMI_OPEN_FILE_BONUS = 15;

	/**
	 * Bonus added for being doubled(there being a rook or queen on the same
	 * column) on a semi-open file
	 */
	private static int DOUBLED_SEMI_OPEN_FILE_BONUS = 30;

	/** Bonus added for being doubled on an open file */
	private static int DOUBLED_OPEN_FILE_BONUS = 50;

	/** Bonus for being on the seventh rank (starting row of opposing Pawns) */
	private static int SEVENTH_RANK_BONUS = 55;

	/** Bonus for being doubled on the seventh rank */
	private static int DOUBLED_SEVENTH_RANK_BONUS = 10;

	/** Bonus added for being on the same column as the opposing Queen */
	private static int SAME_FILE_AS_OPP_QUEEN_BONUS = 10;

	/**
	 * Bonuses and penalties added to a Rooks value based on its position on the
	 * Board
	 */
	private static int[] BOARD_VALS = {
		0,  0,  0,  0,  0,  0,  0,  0,
	   -5,  0,  0,  0,  0,  0,  0, -5,
	   -5,  0,  0,  0,  0,  0,  0, -5,
	   -5,  0,  0,  0,  0,  0,  0, -5,
	   -5,  0,  0,  0,  0,  0,  0, -5,
	   -5,  0,  0,  0,  0,  0,  0, -5,
	    5, 10, 10, 10, 10, 10, 10,  5,
	    0,  0,  0,  0,  0,  0,  0,  0 
	};

	/**
	 * Constructor for RookEval
	 * 
	 * @param board
	 *            The Board to evaluate the input Rook on
	 * @param rook
	 *            The Rook to evaluate on the input Board
	 */
	public RookEval(Board board, Rook rook) {
		super(board, rook);
	}

	/**
	 * Evaluates this RookEval's Rook on its Board.
	 * 
	 * @return An integer rating of how good a Rook is on a Board. The higher
	 *         the rating the better the placement.
	 */
	@Override
	public int evaluate() {
		Square s = piece.getSq();
		int index = piece.getColor() == Color.WHITE ? s.toNum() : 63 - s
				.toNum();

		int value = START_VALUE;
		value += BOARD_VALS[index];

		int seventhRank = piece.getColor() == Color.WHITE ? 1 : 6;
		if (piece.getRow() == seventhRank) {
			value += SEVENTH_RANK_BONUS;
		}
		if (piece.getRow() == seventhRank && isDoubledOnRank()) {
			value += DOUBLED_SEVENTH_RANK_BONUS;
		}
		if (isOnSameFileAsQueen()) {
			value += SAME_FILE_AS_OPP_QUEEN_BONUS;
		}
		if (isOnOpenFile() && isDoubledOnFile()) {
			value += DOUBLED_OPEN_FILE_BONUS;
		} else if (isOnSemiOpenFile() && isDoubledOnFile()) {
			value += DOUBLED_SEMI_OPEN_FILE_BONUS;
		} else if (isOnOpenFile()) {
			value += OPEN_FILE_BONUS;
		} else if (isOnSemiOpenFile()) {
			value += SEMI_OPEN_FILE_BONUS;
		}

		return value;
	}

	/**
	 * Is the Rook on an open file? An open file is a column with no Pawns on
	 * it.
	 * 
	 * @return Is the Rook on an open file?
	 */
	private boolean isOnOpenFile() {
		int col = piece.getCol();
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			Piece p = board.getOccupant(i, col);
			if (p != null && p instanceof Pawn)
				return false;
		}
		return true;
	}

	/**
	 * Is the input Rook on a semi-open file? A semi-open file is one where
	 * there is no Pawn of the Rook's Color and one Pawn of the opposing Color.
	 * 
	 * @return Is the input Rook on a semi-open file?
	 */
	private boolean isOnSemiOpenFile() {
		int col = piece.getCol();
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			Piece p = board.getOccupant(i, col);
			if (p != null && p instanceof Pawn
					&& p.getColor() == piece.getColor()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Is there another rook or queen on the same file as the input Rook?
	 * 
	 * @param b
	 *            Board The Board to check if the input Rook is doubled on
	 * @param r
	 *            Rook The Rook to check if it is doubled on its file
	 * @return Is there another rook or queen on the same column as the input
	 *         Rook?
	 */
	private boolean isDoubledOnFile() {
		int col = piece.getCol();
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			Piece p = board.getOccupant(i, col);
			if (p != null && (p instanceof Rook || p instanceof Queen)
					&& p.getColor() == piece.getColor())
				return true;
		}
		return false;
	}

	/**
	 * Is the input Rook on the same file as the opposite Color's Queen?
	 * 
	 * @return Is the input Rook on the same file as the opposite Color's Queen?
	 */
	private boolean isOnSameFileAsQueen() {
		int col = piece.getCol();
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			Piece p = board.getOccupant(i, col);
			if (p != null && p instanceof Queen
					&& p.getColor() != piece.getColor())
				return true;
		}
		return false;
	}

	/**
	 * Is there another rook or queen on the same rank as the input Rook?
	 * 
	 * @return Is there another rook or queen on the same row as the input Rook?
	 */
	private boolean isDoubledOnRank() {
		int row = piece.getRow();
		for (int i = 0; i < Board.NUM_COLS; i++) {
			Piece p = board.getOccupant(row, i);
			if (p != null && (p instanceof Rook || p instanceof Queen)
					&& p.getColor() == piece.getColor()) {
				return true;
			}
		}
		return false;
	}
}