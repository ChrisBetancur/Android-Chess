package com.kdoherty.engine;

import com.kdoherty.chess.Piece;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Square;

/**
 * This class is responsible for evaluating a Pawn. It takes into account its
 * position on the Board and its position relative to other Pieces on the Board.
 * 
 * @author Kevin Doherty
 * 
 */
public final class PawnEval extends PieceEval {

	public static int START_VALUE = 100;
	
	private static int DOUBLED_ISOLATED_PENALTY = -25;
	private static int SINGLE_ISOLATED_PENALTY = -10;
	private static int DOUBLED_PENALTY = -7;
	private static int MOBILE_CONNECTED_PASSED_BONUS = 50;
	private static int PROTECTED_PASSED_BONUS = 10;
	private static int PASSED_BONUS = 10;

	/** Represents direction from Whites perspective */
	private enum Direction {
		RIGHT, LEFT;
	}

	/**
	 * Used to determine if the Color should or should not match this PawnEval's
	 * Color
	 */
	private enum MatchColor {
		TRUE, FALSE;
	}

	private static int[] BOARD_VALS = { 
		0,   0,   0,   0,   0,   0,  0,  0,
		5,  10,  10, -20, -20,  10, 10,  5,
		5,  -5, -10,   0,   0, -10, -5,  5,
		0,   0,   0,  20,  20,   0,  0,  0,
		5,   5,  10,  30,  30,  10,  5,  5,
		10, 10,  20,  40,  40,  20, 10, 10,
		50, 50,  50,  50,  50,  50, 50, 50,
		0,   0,   0,   0,   0,   0,  0,  0
	};

	/**
	 * Constructor for PawnEval
	 * 
	 * @param board
	 *            The Board to evaluate the input Queen on
	 * @param pawn
	 *            The Pawn to evaluate on the input Board
	 */
	public PawnEval(Board board, Pawn pawn) {
		super(board, pawn);
	}

	/**
	 * Evaluates this PawnEval's Pawn on its Board.
	 * 
	 * @return An integer rating of how good a Pawn is on a Board. The higher
	 *         the rating the better the placement.
	 */
	@Override
	public int evaluate() {
		Square s = piece.getSq();
		int index = color == Color.WHITE ? s.toNum() : 63 - s.toNum();
		
		int value = START_VALUE;
		value += BOARD_VALS[index];
		
		if (isIsolated() && isDoubled()) {
			value += DOUBLED_ISOLATED_PENALTY;
		} else if (isIsolated()) {
			value += SINGLE_ISOLATED_PENALTY;
		} else if (isDoubled()) {
			value += DOUBLED_PENALTY;
		} else if (isMobileConnectedPassed()) {
			value += MOBILE_CONNECTED_PASSED_BONUS;
		} else if (isProtectedPassed()) {
			value += PROTECTED_PASSED_BONUS;
		} else if (isPassed((Pawn) piece)) {
			value += PASSED_BONUS;
		}
		
		return value;
	}

	/**
	 * A Pawn is isolated if there are no Pawns of its Color located on its
	 * adjacent columns.
	 * 
	 * @return Is this PawnEval's Pawn isolated?
	 */
	private boolean isIsolated() {
		int adjCol1 = col + 1;
		int adjCol2 = col - 1;

		boolean adjCol1Inbounds = Board.isInbounds(0, adjCol1);
		boolean adjCol2Inbounds = Board.isInbounds(0, adjCol2);

		for (int r = 0; r < Board.NUM_ROWS; r++) {
			if (adjCol1Inbounds
					&& isPawnColored(board.getOccupant(r, adjCol1),
							MatchColor.TRUE)) {
				return false;
			}
			if (adjCol2Inbounds
					&& isPawnColored(board.getOccupant(r, adjCol2),
							MatchColor.TRUE)) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Is this PawnEval's Pawn doubled? A Pawn is doubled if there is another
	 * Pawn of the same Color on the same Column.
	 * 
	 * @return Is this PawnEval's Pawn doubled?
	 */
	private boolean isDoubled() {
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			Piece posPawn = board.getOccupant(i, col);
			if (isPawnColored(posPawn, MatchColor.TRUE) && posPawn != piece) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Is this PawnEval's Pawn passed and have another Pawn of the same Color
	 * defending it but not passed itself?
	 * 
	 * @return Is this PawnEval's Pawn passed and have another Pawn of the same
	 *         Color defending it but not passed itself?
	 */
	private boolean isProtectedPassed() {
		if (!isPassed((Pawn) piece)) {
			return false;
		}

		int backward = color == Color.WHITE ? 1 : -1;
		Piece posProtecter1 = null;
		Piece posProtecter2 = null;

		if (col != Board.NUM_COLS - 1) {
			posProtecter1 = board.getOccupant(row + backward, col + 1);
		}
		if (col != 0) {
			posProtecter2 = board.getOccupant(row + backward, col - 1);
		}

		return (isPawnColored(posProtecter1, MatchColor.TRUE) && !isPassed((Pawn) posProtecter1))
				|| (isPawnColored(posProtecter2, MatchColor.TRUE) && !isPassed((Pawn) posProtecter2));
	}

	/**
	 * Is this PawnEval's Pawn passed and have an adjacent Pawn of the same
	 * Color which is also passed?
	 * 
	 * @return Is this PawnEval's Pawn passed and have an adjacent Pawn of the
	 *         same Color which is also passed?
	 */
	private boolean isMobileConnectedPassed() {
		if (!isPassed((Pawn) piece)) {
			return false;
		}

		Pawn rightPawn = getAdjPawn(Direction.RIGHT, MatchColor.TRUE);
		Pawn leftPawn = getAdjPawn(Direction.LEFT, MatchColor.TRUE);
		boolean leftPassed = false;
		boolean rightPassed = false;

		if (leftPawn != null) {
			leftPassed = isPassed(leftPawn);
		}
		if (rightPawn != null) {
			rightPassed = isPassed(rightPawn);
		}

		return leftPassed || rightPassed;
	}

	/**
	 * Gets the first Pawn that is located on the adjacent column to the input
	 * direction of this PawnEval's Pawn looking from White's perspective. If no
	 * Pawn exists, null is returned.
	 * 
	 * @param dir
	 *            The direction to get the adjacent Pawn form
	 * @param matchColor
	 *            Should the returned Pawn's Color match this PawnEval's Pawn's
	 *            Color?
	 * @return The first Pawn that is located on the adjacent column to the
	 *         input direction of this PawnEval's Pawn looking from White's
	 *         perspective. If no Pawn exists, null is returned.
	 */
	private Pawn getAdjPawn(Direction dir, MatchColor matchColor) {
		int adjCol = col - 1;
		if (dir == Direction.RIGHT) {
			adjCol = col + 1;
		}
		if (!Board.isInbounds(0, adjCol)) {
			// Adjacent column to input direction is out of bounds
			return null;
		}
		if (color == Color.WHITE) {
			for (int r = 0; r < Board.NUM_ROWS; r++) {
				Piece posPawn = board.getOccupant(r, adjCol);
				if (isPawnColored(posPawn, matchColor)) {
					return (Pawn) posPawn;
				}
			}
		} else {
			for (int r = Board.NUM_ROWS - 1; r >= 0; r--) {
				Piece posPawn = board.getOccupant(r, adjCol);
				if (isPawnColored(posPawn, matchColor))
					return (Pawn) posPawn;
			}
		}
		return null;
	}

	/**
	 * Is the input Pawn passed? A Pawn is passed if there are no opposing
	 * Pawns, in front of it on the same column and on the two adjacent columns.
	 * 
	 * @param p
	 *            The Pawn to check if it is passed.
	 * @return Is the input Pawn passed?
	 */
	private boolean isPassed(Pawn pawn) {
		int adjCol1 = col + 1;
		int adjCol2 = col - 1;
		
		boolean checkRight = true;
		boolean checkLeft = true;
		if (!Board.isInbounds(row, adjCol1)) {
			checkRight = false;
		} else if (!Board.isInbounds(row, adjCol2)) {
			checkLeft = false;
		}
		
		if (color == Color.WHITE) {
			for (int r = row - 1; r >= 0; r--) {
				if ((checkRight && isPawnColored(board.getOccupant(r, adjCol1),
						MatchColor.FALSE))
						|| isPawnColored(board.getOccupant(r, col),
								MatchColor.FALSE)
						|| (checkLeft && isPawnColored(board.getOccupant(r, adjCol2),
								MatchColor.FALSE))) {
					return false;
				}
			}
		} else {
			for (int r = row + 1; r < 8; r++) {
				if ((checkRight && isPawnColored(board.getOccupant(r, adjCol1),
						MatchColor.FALSE))
						|| isPawnColored(board.getOccupant(r, col),
								MatchColor.FALSE)
						|| (checkLeft && isPawnColored(board.getOccupant(r, adjCol2),
								MatchColor.FALSE))) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Is the input Piece not null, a Pawn, and either the same color as this
	 * PawnEval's Pawn or opposite.
	 * 
	 * @param posPawn
	 *            The Piece to check
	 * @param matchColor
	 *            Should this return if the input Pawn is the same color as this
	 *            PawnEval's Piece or opposite.
	 * @return Is the input Piece not null, a Pawn, and of the Color specified
	 *         by the input matchColor?
	 */
	private boolean isPawnColored(Piece posPawn, MatchColor matchColor) {
		if (!(posPawn instanceof Pawn)) {
			return false;
		}
		if (matchColor == MatchColor.TRUE) {
			return posPawn.getColor() == color;
		} else {
			return posPawn.getColor() != color;
		}
	}
}