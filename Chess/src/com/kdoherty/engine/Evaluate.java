package com.kdoherty.engine;

import java.util.ArrayList;
import java.util.List;

import com.kdoherty.chess.Bishop;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.King;
import com.kdoherty.chess.Knight;
import com.kdoherty.chess.Piece;
import com.kdoherty.chess.Queen;
import com.kdoherty.chess.Rook;
import com.kdoherty.chess.Square;

/**
 * This class is responsible for evaluating a Board. No thinking ahead is done
 * here.
 * 
 * @author Kevin Doherty
 */
final class Evaluate {
	
	//TODO: Evaluate captures to higher depth than regular moves?

	/**
	 * The bonus given if it is the Board's side to move is the same as the
	 * color we are evaluating the position from
	 */
	private static int SIDE_TO_MOVE_BONUS = 10;

	/** The bonus to give if Color we are evaluating has both of their Bishops */
	private static int BISHOP_PAIR_BONUS = 15;

	/** The bonus given if the Color we are evaluating has castled */
	private static int CASTLED_BONUS = 45;

	/**
	 * The bonus given if the Color we are evaluating has a Queen close to the
	 * opposing Color's King
	 */
	private static int QUEEN_CLOSE_TO_KING_BONUS = 25;

	/**
	 * Cached value for castling, because once white has castled we don't need
	 * to keep checking
	 */
	private static boolean whiteCastled = false;

	/**
	 * Cached value for castling, because once black has castled we don't need
	 * to keep checking
	 */
	private static boolean blackCastled = false;

	/**
	 * Evaluates a Board from the input Colors perspective.
	 * 
	 * @param b
	 *            Board The Board to evaluate.
	 * @param color
	 *            Color The perspective to evaluate from.
	 * @return A rating of the Board from the input Color's perspective. The
	 *         higher the rating the better the position is for the input Color.
	 */
	static int evaluate(Board b, Color color, boolean debug) {
		if (b.isCheckMate(color.opp())) {
			return Integer.MAX_VALUE;
		}
		int value = 0;
		if (color == Color.WHITE) {
			if ((whiteCastled || b.findKing(Color.WHITE).hasCastled())
					&& !isEndGame(b)) {
				whiteCastled = true;
				value += CASTLED_BONUS;
			}
		} else if ((blackCastled || b.findKing(Color.BLACK).hasCastled())
				&& !isEndGame(b)) {
			blackCastled = true;
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

		//value += (getNumMoves(b, color) - getNumMoves(b, color.opp()));
		value += getMaterialCount(b, color);

		return value;
	}

	/**
	 * Gets a list of Squares within 3 Squares of the input King.
	 * 
	 * @param king
	 *            The King to get the "zone" around
	 * @return All Squares within 3 Squares of the input King.
	 */
	private static List<Square> kingZone(King king) {
		List<Square> zone = new ArrayList<Square>();
		for (int i = king.getRow() - 2; i < king.getRow() + 3; i++) {
			for (int j = king.getCol() - 2; j < king.getCol() + 3; j++) {
				if (Board.isInbounds(i, j)) {
					zone.add(new Square(i, j));
				}
			}
		}
		return zone;
	}

	/**
	 * Gets all Pieces of the input color in the opponent's King's zone.
	 * 
	 * @param b
	 *            Board The Board the Pieces are on
	 * @param color
	 *            Color The Color of the Pieces to get
	 * @return All Pieces of the input Color in the opponent's King's zone.
	 */
	private static List<Piece> getPiecesInOppKingZone(Board b, Color color) {
		King kingOfColor = b.findKing(color.opp());
		List<Square> kingZone = kingZone(kingOfColor);
		List<Piece> piecesInZone = new ArrayList<Piece>();
		for (Square s : kingZone) {
			Piece p = b.getOccupant(s.row(), s.col());
			if (p != null && p.getColor() == color)
				piecesInZone.add(p);
		}
		return piecesInZone;

	}

	/**
	 * Is the Queen of the input Color close to the opposing King?
	 * 
	 * @param b
	 *            Board The Board to check if the queen is close to King on.
	 * @param color
	 *            Color The Color of the Queen to check.
	 * @return Is the Queen of the input Color close to the opposing King?
	 */
	static boolean queenCloseToKing(Board b, Color color) {
		for (Piece p : getPiecesInOppKingZone(b, color)) {
			if (p instanceof Queen)
				return true;
		}
		return false;
	}

	/**
	 * Are there still queens on the Board?
	 * 
	 * @param b
	 *            Board to check if there are still queens on.
	 * @return Are there still queens on the Board?
	 */
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

	/**
	 * Are there still minor pieces on the Board? Minor pieces are Bishops and
	 * Knights.
	 * 
	 * @param b
	 *            Board The Board to check if there are still minor pieces on
	 * @return Are there still minor pieces on the Board.
	 */
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

	/**
	 * Are there still rooks on the Board?
	 * 
	 * @param b
	 *            Board to check if there are still rooks on.
	 * @return Are there still rooks on the Board?
	 */
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

	/**
	 * Is it the end game on the input Board? It is the end game when there are
	 * either no queens on the Board or there are no minor pieces and no rooks
	 * on the board
	 * 
	 * @param b
	 *            Board
	 * @return boolean
	 */
	static boolean isEndGame(Board b) {
		return !stillQueens(b) || (!stillMinorPieces(b) && !stillRooks(b));
	}

	/**
	 * Does the input Color have both of their Bishop's on the input Board?
	 * 
	 * @param b
	 *            The Board to check if the input Color has the Bishop pair on.
	 * @param color
	 *            The Color of the side we are checking for a Bishop pair.
	 * @return Does the input Color have both of their Bishop's on the input
	 *         Board?
	 */
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

	/**
	 * Gets the number of moves the input can make on the input Board.
	 * 
	 * @param b
	 *            The Board to get the number of moves of the input Color on
	 * @param color
	 *            The Color of which we are counting the number of moves on
	 * @return The number of moves the input can make on the input Board.
	 */
//	private static int getNumMoves(Board b, Color color) {
//		return b.getMoves(color).size();
//	}

	/**
	 * Gets the total Piece value of all Pieces on the input Board of the input
	 * Color.
	 * 
	 * @param board
	 *            The Board to get the total Piece value on.
	 * @param color
	 *            The Color of the Pieces to add to the total
	 * @return The total Piece value of all Pieces on the input Board of the
	 *         input Color.
	 */
	private static int getTotalPieceValue(Board board, Color color) {
		int total = 0;
		for (Piece p : board.getPieces(color)) {
			total += p.evaluate(board);
		}
		return total;
	}

	/**
	 * Gets the difference in material count by subtracting the opposite color's
	 * material count form the input Color's material count.
	 * 
	 * @param board
	 *            The Board to get the material count on
	 * @param color
	 *            The perspective to view the material count from.
	 * @return The difference in material count obtained by subtracting the
	 *         opposite color's material count form the input Color's material
	 *         count. The higher the returned value, the better the evaluation
	 *         from the perspective of the input Color.
	 */
	private static int getMaterialCount(Board board, Color color) {
		return getTotalPieceValue(board, color)
				- getTotalPieceValue(board, color.opp());
	}
}
