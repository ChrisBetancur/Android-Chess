package com.kdoherty.engine;

import com.kdoherty.chess.Piece;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Square;

public class PawnEval {
	private static int START_VALUE = 100;
	private static int DOUBLED_ISOLATED_PENALTY = -25;
	private static int SINGLE_ISOLATED_PENALTY = -10;
	private static int DOUBLED_PENALTY = -7;
	private static int MOBILE_CONNECTED_PASSED_BONUS = 50;
	private static int PROTECTED_PASSED_BONUS = 10;
	private static int PASSED_BONUS = 10;

	private static int[] BOARD_VALS = { 0, 0, 0, 0, 0, 0, 0, 0, 5, 10, 10, -20,
			-20, 10, 10, 5, 5, -5, -10, 0, 0, -10, -5, 5, 0, 0, 0, 20, 20, 0,
			0, 0, 5, 5, 10, 30, 30, 10, 5, 5, 10, 10, 20, 40, 40, 20, 10, 10,
			50, 50, 50, 50, 50, 50, 50, 50, 0, 0, 0, 0, 0, 0, 0, 0 };

	/*
	 * evaluates the pawn based on its position
	 */
	public static int eval(Board b, Pawn p) {
		int value = START_VALUE;
		Square s = p.getSq();
		int index = p.getColor() == Color.WHITE ? s.toNum() : 63 - s.toNum();
		value += BOARD_VALS[index];
		if (isIsolated(b, p) && isDoubled(b, p)) // double isolated pawns
			value += DOUBLED_ISOLATED_PENALTY;
		else if (isIsolated(b, p)) // single isolated pawn
			value += SINGLE_ISOLATED_PENALTY;
		else if (isDoubled(b, p)) // doubled non-isolated pawn
			value += DOUBLED_PENALTY;
		else if (isMobileConnectedPassed(b, p)) // mobile connected passed
												// pawns
			value += MOBILE_CONNECTED_PASSED_BONUS;
		else if (isProtectedPassed(b, p)) // protected passed pawn
			value += PROTECTED_PASSED_BONUS;
		else if (isPassed(b, p)) // passed pawn, not protected or connected
			value += PASSED_BONUS;
		return value;
	}

	public static boolean isIsolated(Board b, Pawn p) {
		int adjCol1 = p.getCol() + 1;
		int adjCol2 = p.getCol() - 1;

		for (int r = 0; r < Board.NUMROWS; r++) {
			if (Board.isInbounds(r, adjCol1)
					&& b.getOccupant(r, adjCol1) != null
					&& b.getOccupant(r, adjCol1) instanceof Pawn
					&& b.getOccupant(r, adjCol1).getColor() == p.getColor())
				return false;
			if (Board.isInbounds(r, adjCol2)
					&& b.getOccupant(r, adjCol2) != null
					&& b.getOccupant(r, adjCol2) instanceof Pawn
					&& b.getOccupant(r, adjCol2).getColor() == p.getColor())
				return false;
		}
		return true;
	}

	public static boolean isDoubled(Board b, Pawn p) {
		int col = p.getCol();
		for (int i = 0; i < Board.NUMROWS; i++) {
			Piece piece = b.getOccupant(i, col);
			if (piece != null && piece instanceof Pawn
					&& piece.getColor() == p.getColor())
				return true;
		}
		return false;
	}

	public static boolean isProtectedPassed(Board b, Pawn p) {
		return isPassed(b, p)
				&& PosnEval.getLeastValofDefending(b, p.getRow(), p.getCol(),
						p.getColor()) == 1;
	}

	public static boolean isMobileConnectedPassed(Board b, Pawn p) {
		Pawn right = getAdjPawn(b, p, true);
		Pawn left = getAdjPawn(b, p, false);
		boolean leftPassed = false;
		boolean rightPassed = false;
		if (left != null) {
			leftPassed = isPassed(b, left);
		}
		if (right != null) {
			rightPassed = isPassed(b, right);
		}

		return isPassed(b, p) && (leftPassed || rightPassed);
	}

	public static Pawn getAdjPawn(Board b, Pawn p, boolean right) {
		int adjCol;
		if (right)
			adjCol = p.getCol() + 1;
		else
			adjCol = p.getCol() - 1;
		if (p.getColor() == Color.WHITE) {
			for (int r = 0; r < Board.NUMROWS; r++) {
				Piece piece = b.getOccupant(r, adjCol);
				if (piece != null && piece instanceof Pawn
						&& piece.getColor() == p.getColor())
					return (Pawn) piece;
			}
		} else {
			for (int r = Board.NUMROWS - 1; r >= 0; r--) {
				Piece piece = b.getOccupant(r, adjCol);
				if (piece != null && piece instanceof Pawn
						&& piece.getColor() == p.getColor())
					return (Pawn) p;
			}
		}
		return null;
	}

	public static boolean isPassed(Board b, Pawn p) {
		int adjCol1 = p.getCol() + 1;
		int adjCol2 = p.getCol() - 1;
		if (p.getColor() == Color.WHITE) {
			for (int r = p.getRow() - 1; r >= 0; r--) {
				if (b.getOccupant(r, adjCol1) != null
						&& b.getOccupant(r, adjCol1) instanceof Pawn
						&& b.getOccupant(r, adjCol1).getColor() != p.getColor())
					return false;
				if (b.getOccupant(r, adjCol2) != null
						&& b.getOccupant(r, adjCol2) instanceof Pawn
						&& b.getOccupant(r, adjCol2).getColor() != p.getColor())
					return false;
			}
		} else {
			for (int r = p.getRow() + 1; r < 8; r++) {
				if (b.getOccupant(r, adjCol1) != null
						&& b.getOccupant(r, adjCol1) instanceof Pawn
						&& b.getOccupant(r, adjCol1).getColor() != p.getColor())
					return false;
				if (b.getOccupant(r, adjCol2) != null
						&& b.getOccupant(r, adjCol2) instanceof Pawn
						&& b.getOccupant(r, adjCol2).getColor() != p.getColor())
					return false;
			}
		}
		return true;
	}

}