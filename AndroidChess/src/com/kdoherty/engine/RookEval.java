package com.kdoherty.engine;

import com.kdoherty.chess.Piece;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Queen;
import com.kdoherty.chess.Rook;
import com.kdoherty.chess.Square;

public class RookEval {

	private static int START_VALUE = 500;
	private static int OPEN_FILE_BONUS = 20;
	private static int SEMI_OPEN_FILE_BONUS = 15;
	private static int DOUBLED_SEMI_OPEN_FILE_BONUS = 30;
	private static int DOUBLED_OPEN_FILE_BONUS = 50;
	private static int SEVENTH_RANK_BONUS = 55;
	private static int DOUBLED_SEVENTH_RANK_BONUS = 10;
	private static int SAME_FILE_AS_OPP_QUEEN_BONUS = 10;
	private static int[] BOARD_VALS = {
										0, 0, 0, 0, 0, 0, 0, 0,
									   -5, 0, 0, 0, 0, 0, 0, -5,
									   -5, 0, 0, 0, 0, 0, 0, -5,
									   -5, 0, 0, 0, 0, 0, 0, -5,
									   -5, 0, 0, 0, 0, 0, 0, -5,
									   -5, 0, 0, 0, 0, 0, 0, -5,
									    5, 10, 10, 10, 10, 10, 10,
									    5, 0, 0, 0, 0, 0, 0, 0, 0 };

	public static int eval(Board b, Rook r) {
		int value = START_VALUE;
		int seventhRank = r.getColor() == Color.WHITE ? 1 : 6;
//		int eigthRank = r.getColor() == Color.WHITE ? 0 : 7;
//		int homeRow = r.getColor() == Color.WHITE ? 7 : 0;
//		int homeRank = r.getColor() == Color.WHITE ? 6 : 1;
		Square s = r.getSq();
		int index = r.getColor() == Color.WHITE ? s.toNum() : 63 - s
				.toNum();
		value += BOARD_VALS[index];
		if (r.getRow() == seventhRank)
			value += SEVENTH_RANK_BONUS;
		if (r.getRow() == seventhRank && isDoubledOnRank(b, r))
			value += DOUBLED_SEVENTH_RANK_BONUS;
		if (isOnSameFileAsQueen(b, r))
			value += SAME_FILE_AS_OPP_QUEEN_BONUS;
		if (isOnOpenFile(b, r) && isDoubledOnFile(b, r))
			value += DOUBLED_OPEN_FILE_BONUS;
		else if (isOnSemiOpenFile(b, r) && isDoubledOnFile(b, r))
			value += DOUBLED_SEMI_OPEN_FILE_BONUS;
		else if (isOnOpenFile(b, r))
			value += OPEN_FILE_BONUS;
		else if (isOnSemiOpenFile(b, r))
			value += SEMI_OPEN_FILE_BONUS;
		return value;
	}

	/*
	 * Is the rook on a file with no pawns on it?
	 */
	private static boolean isOnOpenFile(Board b, Rook r) {
		int col = r.getCol();
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			Piece p = b.getOccupant(i, col);
			if (p != null && p instanceof Pawn)
				return false;
		}
		return true;
	}

	/*
	 * Is the rook on a semi-open file?
	 */

	private static boolean isOnSemiOpenFile(Board b, Rook r) {
		int col = r.getCol();
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			Piece p = b.getOccupant(i, col);
			if (p != null && p instanceof Pawn
					&& p.getColor() == r.getColor())
				return false;
		}
		return true;
	}

	/*
	 * Is there a rook or queen of the same color on the same file?
	 */
	private static boolean isDoubledOnFile(Board b, Rook r) {
		int col = r.getCol();
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			Piece p = b.getOccupant(i, col);
			if (p != null && (p instanceof Rook || p instanceof Queen)
					&& p.getColor() == r.getColor())
				return true;
		}
		return false;
	}

	/*
	 * Is there a queen of the opposite color on the same file?
	 */
	private static boolean isOnSameFileAsQueen(Board b, Rook r) {
		int col = r.getCol();
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			Piece p = b.getOccupant(i, col);
			if (p != null && p instanceof Queen
					&& p.getColor() != r.getColor())
				return true;
		}
		return false;
	}

	/*
	 * Is there a rook or queen of the same color on the same rank?
	 */
	private static boolean isDoubledOnRank(Board b, Rook r) {
		int row = r.getRow();
		for (int i = 0; i < Board.NUM_COLS; i++) {
			Piece p = b.getOccupant(row, i);
			if (p != null && (p instanceof Rook || p instanceof Queen)
					&& p.getColor() == r.getColor())
				return true;
		}
		return false;
	}
}