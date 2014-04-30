package com.kdoherty.engine;

import java.util.ArrayList;

import com.kdoherty.chess.Bishop;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.King;
import com.kdoherty.chess.Knight;
import com.kdoherty.chess.AbstractPiece;
import com.kdoherty.chess.Queen;
import com.kdoherty.chess.Rook;
import com.kdoherty.chess.Square;

public class PosnEval {

	private static int SIDE_TO_MOVE_BONUS = 10;
	private static int BISHOP_PAIR_BONUS = 15;
	private static int CASTLED_BONUS = 45;
	private static int QUEEN_CLOSE_TO_KING_BONUS = 25;
	private static boolean WHITE_CASTLED = false;
	private static boolean BLACK_CASTLED = false;

	// TODO;
	// private static int QUEEN_CLOSE_TO_KING_MATE_DEPTH = 4;

	// evaluates placement of pieces, does not care about side to move
	public static int evalBoard(Board b, Color color) {
		int value = 0;
		if (color == Color.WHITE) {
			if (WHITE_CASTLED && !isEndGame(b)) {
				// System.out.println("white get castled bonus");
				value += CASTLED_BONUS;
			}
		} else if (BLACK_CASTLED && !isEndGame(b)) {
			// System.out.println("black get castled bonus");
			value += CASTLED_BONUS;
		}

		if (queenCloseToKing(b, color)) {
			// System.out.println(color + " gets queen close to king bonus");
			value += QUEEN_CLOSE_TO_KING_BONUS;
			// CPUPlayer.setMateDepth(QUEEN_CLOSE_TO_KING_MATE_DEPTH);
		}
		// else CPUPlayer.setMateDepth(CPUPlayer.defaultMateDepth);
		if (bishopPair(b, color)) {
			// System.out.println(color + " gets bishop pair bonus");
			value += BISHOP_PAIR_BONUS;
		}

		if (b.getSideToMove() == color) {
			// System.out.println(color + " gets side to move bonus");
			value += SIDE_TO_MOVE_BONUS;
		}
		// value += getNumSqsCtrl(b,color)
		// * 1.5;
		// value += ((getHangingPieceValue(b,color) * -1) + 10);
		// System.out.println(color + " gets hanging piece penalty of " +
		// (getHangingPieceValue(b,color) * -1));
		return value;
	}

	public static void setWhiteCastled(Boolean b) {
		WHITE_CASTLED = b;
	}

	public static void setBlackCastled(Boolean b) {
		BLACK_CASTLED = b;
	}

	// returns all squares within 3 squares of the king
	public static ArrayList<Square> kingZone(Board b, King k) {
		ArrayList<Square> zone = new ArrayList<Square>();
		for (int i = k.getRow() - 2; i < k.getRow() + 3; i++) {
			for (int j = k.getCol() - 2; j < k.getCol() + 3; j++) {
				if (Board.isInbounds(i, j)) {
					zone.add(new Square(i, j));
				}
			}
		}
		return zone;
	}

	/*
	 * returns a list of pieces of color color which are in the opp King's zone
	 */
	public static ArrayList<AbstractPiece> getPiecesInOppKingZone(Board b,
			Color color) {
		King kingOfColor = b.findKing(color.opp());
		ArrayList<Square> kingZone = kingZone(b, kingOfColor);
		ArrayList<AbstractPiece> piecesInZone = new ArrayList<AbstractPiece>();
		for (Square s : kingZone) {
			AbstractPiece p = b.getOccupant(s.row(), s.col());
			if (p != null && p.getColor() == color)
				piecesInZone.add(p);
		}
		return piecesInZone;

	}

	/*
	 * is color's queen close to oppColor's King
	 */
	public static boolean queenCloseToKing(Board b, Color color) {
		for (AbstractPiece p : getPiecesInOppKingZone(b, color)) {
			if (p instanceof Queen)
				return true;
		}
		return false;
	}

	public static int getMinVal(Board b, ArrayList<AbstractPiece> pieces) {
		int min = 10001;
		for (AbstractPiece p : pieces) {
			int eval = p.evaluate(b);
			if (eval < min)
				min = eval;
		}
		return min;
	}

	// are there still queens on the board
	public static boolean stillQueens(Board b) {
		for (int i = 0; i < Board.NUMROWS; i++) {
			for (int j = 0; j < Board.NUMCOLS; j++) {
				AbstractPiece p = b.getOccupant(i, j);
				if (p != null && p instanceof Queen)
					return true;
			}
		}
		return false;
	}

	/*
	 * returns an ArrayList of pieces which canMove to a square
	 */
	public static ArrayList<AbstractPiece> getTakingPieces(Board b, int row,
			int col, Color color) {
		ArrayList<AbstractPiece> pieces = new ArrayList<AbstractPiece>();
		for (AbstractPiece p : b.getPieces(color)) {
			if (p != null && p.canMove(b, row, col))
				pieces.add(p);
		}
		return pieces;
	}

	public static boolean isHanging(Board b, AbstractPiece p) {
		int r = p.getRow();
		int c = p.getCol();
		int pVal = p.evaluate(b);
		if (getMinVal(b, getTakingPieces(b, r, c, p.getColor().opp())) < pVal)
			return true;
		return false;
	}

	public static ArrayList<AbstractPiece> getHangingPieces(Board b, Color color) {
		ArrayList<AbstractPiece> hangingPieces = new ArrayList<AbstractPiece>();
		for (AbstractPiece p : b.getPieces(color)) {
			if (isHanging(b, p)) {
				hangingPieces.add(p);
			}
		}
		return hangingPieces;
	}

	// gets the total value of all pieces in the list
	public static int getListVal(Board b, ArrayList<AbstractPiece> pieces) {
		int total = 0;
		for (AbstractPiece p : pieces) {
			total += p.evaluate(b);
		}
		return total;
	}

	public static int getHangingPieceValue(Board b, Color color) {
		return getListVal(b, getHangingPieces(b, color));
	}

	public static boolean stillMinorPieces(Board b) {
		for (int i = 0; i < Board.NUMROWS; i++) {
			for (int j = 0; j < Board.NUMCOLS; j++) {
				AbstractPiece p = b.getOccupant(i, j);
				if (p != null && (p instanceof Knight || p instanceof Bishop))
					return true;
			}
		}
		return false;
	}

	public static boolean stillRooks(Board b) {
		for (int i = 0; i < Board.NUMROWS; i++) {
			for (int j = 0; j < Board.NUMCOLS; j++) {
				AbstractPiece p = b.getOccupant(i, j);
				if (p != null && p instanceof Rook)
					return true;
			}
		}
		return false;
	}

	public static boolean isEndGame(Board b) {
		return !stillQueens(b)
				|| (stillQueens(b) && !stillMinorPieces(b) && !stillRooks(b));
	}

	public static boolean bishopPair(Board b, Color color) {
		int bishopCount = 0;
		for (int i = 0; i < Board.NUMROWS; i++) {
			for (int j = 0; j < Board.NUMCOLS; j++) {
				AbstractPiece p = b.getOccupant(i, j);
				if (p != null && p.getColor() == color && p instanceof Bishop)
					bishopCount++;
			}
		}
		return bishopCount == 2;
	}

	public static int getNumMoves(Board b, Color color) {
		return b.getMoves(color).size();
	}

	/*
	 * returns an ArrayList of pieces which are defending a square
	 */
	public static ArrayList<AbstractPiece> getDefendingPieces(Board b, int row,
			int col, Color color) {
		ArrayList<AbstractPiece> pieces = new ArrayList<AbstractPiece>();
		for (AbstractPiece p : b.getPieces(color)) {
			if (p != null && p.isDefending(b, row, col))
				pieces.add(p);
		}
		return pieces;
	}

	public static int getNumSqsCtrl(Board b, Color color) {
		int numSqs = 0;
		for (int i = 0; i < Board.NUMROWS; i++) {
			for (int j = 0; j < Board.NUMCOLS; j++) {
				// each square that is defended by a pawn and not by an opp pawn
				// is under control
				if (b.getOccupant(i, j) == null
						&& ((getDefendingPieces(b, i, j, color).size() != 0 && getDefendingPieces(
								b, i, j, color.opp()).size() == 0) || (getLeastValofDefending(
								b, i, j, color) < getLeastValofDefending(b, i,
								j, color.opp())))) {
					numSqs++;
				}
			}
		}
		return numSqs;
	}

	/*
	 * gets least value of defending pieces returns 0 if there are no
	 * defendingPieces
	 */
	public static double getLeastValofDefending(Board b, int row, int col,
			Color color) {
		ArrayList<AbstractPiece> defendingPieces = getDefendingPieces(b, row,
				col, color);
		double lowestVal = 101;
		if (defendingPieces == null)
			return 0;
		for (AbstractPiece p : defendingPieces) {
			if (p.evaluate(b) < lowestVal)
				lowestVal = p.evaluate(b);
		}
		return lowestVal;
	}

}
