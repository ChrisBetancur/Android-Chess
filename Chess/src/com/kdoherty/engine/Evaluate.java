package com.kdoherty.engine;

import java.util.ArrayList;
import java.util.List;

import com.kdoherty.chess.Bishop;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.King;
import com.kdoherty.chess.Knight;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Piece;
import com.kdoherty.chess.Queen;
import com.kdoherty.chess.Rook;
import com.kdoherty.chess.Square;

public class Evaluate {

	private static int SIDE_TO_MOVE_BONUS = 10;
	private static int BISHOP_PAIR_BONUS = 15;
	private static int CASTLED_BONUS = 45;
	private static int QUEEN_CLOSE_TO_KING_BONUS = 25;
	private static boolean whiteCastled = false;
	private static boolean blackCastled = false;

	// TODO;
	// private static int QUEEN_CLOSE_TO_KING_MATE_DEPTH = 4;

	// evaluates placement of pieces
	public static int evaluate(Board b, Color color) {
		if (b.isCheckMate(color.opp())) {
			return Integer.MAX_VALUE;
		}
		int value = 0;
		if (color == Color.WHITE) {
			if ((whiteCastled || b.findKing(Color.WHITE).hasCastled()) && !isEndGame(b)) {
				value += CASTLED_BONUS;
			}
		} else if ((blackCastled || b.findKing(Color.BLACK).hasCastled()) && !isEndGame(b)) {
			value += CASTLED_BONUS;
		}
		if (queenCloseToKing(b, color)) {
			value += QUEEN_CLOSE_TO_KING_BONUS;
		}
		if (hasBishopPair(b, color)) {
			value += BISHOP_PAIR_BONUS;
		}
		if (b.getSideToMove() == color) {
			value += SIDE_TO_MOVE_BONUS;
		}
		value += (getNumMoves(b, color) - getNumMoves(b, color.opp()));
		value += getMaterialCount(b, color);
		return value;
	}

	// returns all squares within 3 squares of the king
	private static ArrayList<Square> kingZone(Board b, King k) {
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
	private static ArrayList<Piece> getPiecesInOppKingZone(Board b,
			Color color) {
		King kingOfColor = b.findKing(color.opp());
		ArrayList<Square> kingZone = kingZone(b, kingOfColor);
		ArrayList<Piece> piecesInZone = new ArrayList<Piece>();
		for (Square s : kingZone) {
			Piece p = b.getOccupant(s.row(), s.col());
			if (p != null && p.getColor() == color)
				piecesInZone.add(p);
		}
		return piecesInZone;

	}

	/*
	 * is color's queen close to oppColor's King
	 */
	private static boolean queenCloseToKing(Board b, Color color) {
		for (Piece p : getPiecesInOppKingZone(b, color)) {
			if (p instanceof Queen)
				return true;
		}
		return false;
	}

	private static int getMinVal(Board b, ArrayList<Piece> pieces) {
		int min = 10001;
		for (Piece p : pieces) {
			int eval = p.evaluate(b);
			if (eval < min)
				min = eval;
		}
		return min;
	}

	// are there still queens on the board
	private static boolean stillQueens(Board b) {
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			for (int j = 0; j < Board.NUM_COLS; j++) {
				Piece p = b.getOccupant(i, j);
				if (p != null && p instanceof Queen)
					return true;
			}
		}
		return false;
	}

	/*
	 * returns an ArrayList of pieces which canMove to a square
	 */
	private static ArrayList<Piece> getTakingPieces(Board b, int row,
			int col, Color color) {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		for (Piece p : b.getPieces(color)) {
			if (p != null && p.canMove(b, row, col))
				pieces.add(p);
		}
		return pieces;
	}

	public static boolean isHanging(Board b, Piece p) {
		int r = p.getRow();
		int c = p.getCol();
		int pVal = p.evaluate(b);
		if (getMinVal(b, getTakingPieces(b, r, c, p.getColor().opp())) < pVal)
			return true;
		return false;
	}

	public static ArrayList<Piece> getHangingPieces(Board b, Color color) {
		ArrayList<Piece> hangingPieces = new ArrayList<Piece>();
		for (Piece p : b.getPieces(color)) {
			if (isHanging(b, p)) {
				hangingPieces.add(p);
			}
		}
		return hangingPieces;
	}

	private static boolean stillMinorPieces(Board b) {
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			for (int j = 0; j < Board.NUM_COLS; j++) {
				Piece p = b.getOccupant(i, j);
				if (p != null && (p instanceof Knight || p instanceof Bishop))
					return true;
			}
		}
		return false;
	}

	private static boolean stillRooks(Board b) {
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			for (int j = 0; j < Board.NUM_COLS; j++) {
				Piece p = b.getOccupant(i, j);
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

	private static boolean hasBishopPair(Board b, Color color) {
		int bishopCount = 0;
		for (Piece piece : b.getPieces(color)) {
			if (piece instanceof Bishop) {
				bishopCount++;
				if (bishopCount == 2) {
					return true;
				}
			}
		}
		return false;
	}

	private static int getNumMoves(Board b, Color color) {
		return b.getMoves(color).size();
	}

	/*
	 * returns an ArrayList of pieces which are defending a square
	 */
	private static List<Piece> getDefendingPieces(Board b, int row,
			int col, Color color) {
		List<Piece> pieces = new ArrayList<Piece>();
		for (Piece p : b.getPieces(color)) {
			if (p != null && p.isDefending(b, row, col))
				pieces.add(p);
		}
		return pieces;
	}

	public static int getNumSqsCtrl(Board b, Color color) {
		int numSqs = 0;
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			for (int j = 0; j < Board.NUM_COLS; j++) {
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
	static int getLeastValofDefending(Board b, int row, int col,
			Color color) {
		List<Piece> defendingPieces = getDefendingPieces(b, row,
				col, color);
		int lowestVal = Integer.MAX_VALUE;
		if (defendingPieces == null)
			return 0;
		for (Piece p : defendingPieces) {
			int pieceEval = p.evaluate(b);
			if (pieceEval < lowestVal)
				lowestVal = pieceEval;
		}
		return lowestVal;
	}
	
    /*
     * returns the sum of values of pieces of the input color
     */
    private static int getTotalPieceValue(Board board, Color color) {
        int total = 0;
        for (Piece p : board.getPieces(color)) {
            total += p.evaluate(board);
        }
        return total;
    }
    
    /*
     * returns the difference in material count 
     * by subtracting oppColor's count from color's count
     */
    private static int getMaterialCount(Board board, Color color) {
        return getTotalPieceValue(board, color) - getTotalPieceValue(board, color.opp());
    }
    
	public static boolean isBlockingCenterPawn(Board b, Piece p) {
		int pawnRow = (p.getColor() == Color.WHITE) ? 6 : 1;
		int blockingRow = (p.getColor() == Color.WHITE) ? pawnRow - 1
				: pawnRow + 1;
		return p.getRow() == blockingRow
				&& ((p.getCol() == 3 && b.getOccupant(pawnRow, 3) instanceof Pawn) || (p
						.getCol() == 4 && b.getOccupant(pawnRow, 4) instanceof Pawn));
	}
}
