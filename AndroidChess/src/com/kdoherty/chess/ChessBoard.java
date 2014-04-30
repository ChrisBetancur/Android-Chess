package com.kdoherty.chess;

import java.util.ArrayList;
import java.util.List;

public class ChessBoard {

	/**
	 * A board is represented as a 2D array of Pieces An empty Square is
	 * represented by null
	 */
	private final AbstractPiece[][] pieces;
	
	/**
	 * dimensions of the chessBoard are 8 rows by 8 columns
	 */
	public final static int NUMROWS = 8;
	public final static int NUMCOLS = 8;

	/**
	 * Keeps track of squares where a pawn can be captured by enPoissant
	 */
	private Square enPoissantSq;

	/**
	 * Constructor for Board Initially sets all squares to null
	 */
	public ChessBoard() {
		
		pieces = new AbstractPiece[NUMROWS][NUMCOLS];
		// TODO: Is this needed?
		clearBoard();
	}

	/**
	 * @param r
	 *            row coordinate
	 * @param c
	 *            column coordinate
	 * @return is coordinate (r, c) in a chess board?
	 */
	public static boolean isInbounds(int row, int col) {
		return row < NUMROWS && row >= 0 && col >= 0 && col < NUMCOLS;
	}

	/**
	 * Takes in two locations on the board and determines if they are diagonal
	 * 
	 * @param r
	 *            row coordinate for first location
	 * @param c
	 *            column coordinate for first location
	 * @param r2
	 *            row coordinate for second location
	 * @param c2
	 *            column coordinate for second location
	 * @return true if the two locations are diagonal
	 */
	public static boolean sameDiagonal(int row, int col, int row2, int col2) {
		double colDif = col2 - col;
		if (colDif != 0) {
			return Math.abs((row2 - row) / colDif) == 1.0;
		}
		return false;
	}

	/**
	 * Takes in two locations on the board and determines if they are next to
	 * each other Not using getNeighbors because we want to stop looping as soon
	 * as we find the square in neighbors
	 * 
	 * @param r
	 *            row coordinate for first location
	 * @param c
	 *            column coordinate for first location
	 * @param r2
	 *            row coordinate for second location
	 * @param c2
	 *            column coordinate for second location
	 * @return true if the two locations are neighbors
	 */
	public static boolean isNeighbor(int r, int c, int r2, int c2) {
		for (int i = r2 - 1; i < r2 + 2; i++) {
			for (int j = c2 - 1; j < c2 + 2; j++) {
				if (!(i == r2 && j == c2) && (i == r && j == c))
					return true;
			}
		}
		return false;
	}

	/**
	 * Gets all surrounding squares of the input location
	 * 
	 * @param r
	 *            row coordinate
	 * @param c
	 *            column coordinate
	 * @return all squares surrounding the input row, column
	 */
	public static List<Square> getNeighbors(int r, int c) {
		List<Square> neighbors = new ArrayList<Square>();
		for (int i = r - 1; i < r + 2; i++) {
			for (int j = c - 1; j < c + 2; j++) {
				if (!(i == r && j == c) && isInbounds(i, j))
					neighbors.add(new Square(i, j));
			}
		}
		return neighbors;
	}

	/**
	 * If there are squares between the two input squares, they are returned. If
	 * there are not null is returned.
	 * 
	 * @param s1
	 *            One square to find squares between
	 * @param s2
	 *            The other square to find squares between
	 * @return An <code>List</code> of all squares between the input squares
	 */
	public static List<Square> getBtwnSqs(Square s1, Square s2) {
		if (s1.equals(s2)) {
			return null;
		}
		if (s1.row() == s2.row()) {
			return getBtwnSqsRow(s1, s2);
		}
		if (s1.col() == s2.col()) {
			return getBtwnSqsCol(s1, s2);
		}
		if (sameDiagonal(s1.row(), s1.col(), s2.row(), s2.col())) {
			return getBtwnSqsDiag(s1, s2);
		}
		return null;
	}

	/**
	 * helper method for getBtwnSqs. Only to be called on squares which are in
	 * the same row and are not the same row find all squares between the 2
	 * input squares
	 * 
	 * @param s1
	 *            One square in the row
	 * @param s2
	 *            A different square in the same row
	 * @return An <code>List</code> of all squares between the input squares
	 */
	private static List<Square> getBtwnSqsRow(Square s1, Square s2) {
		List<Square> btwnSqs = new ArrayList<Square>();
		int row = s1.row();
		int smallerCol = Math.min(s1.col(), s2.col());
		int largerCol = Math.max(s1.col(), s2.col());
		for (int i = smallerCol + 1; i < largerCol; i++) {
			btwnSqs.add(new Square(row, i));
		}
		return btwnSqs;
	}

	/**
	 * helper method for getBtwnSqs. Only to be called on squares which are in
	 * the same column and are not the same column find all squares between the
	 * 2 input squares
	 * 
	 * @param s1
	 *            One square in the column
	 * @param s2
	 *            A different square in the same column
	 * @return An <code>List</code> of all squares between the input squares
	 */
	private static List<Square> getBtwnSqsCol(Square s1, Square s2) {
		List<Square> btwnSqs = new ArrayList<Square>();
		int col = s1.col();
		int smallerRow = Math.min(s1.row(), s2.row());
		int largerRow = Math.max(s1.row(), s2.row());
		for (int i = largerRow - 1; i > smallerRow; i--) {
			btwnSqs.add(new Square(i, col));
		}
		return btwnSqs;
	}

	/**
	 * helper method for getBtwnSqs. Only to be called on squares which are in
	 * the same diagonal and are not the same diagonal find all squares between
	 * the 2 input squares
	 * 
	 * @param s1
	 *            One square in the diagonal
	 * @param s2
	 *            A different square in the same diagonal
	 * @return An <code>List</code> of all squares between the input squares
	 */
	private static List<Square> getBtwnSqsDiag(Square s1, Square s2) {
		List<Square> btwnSqs = new ArrayList<Square>();
		int rowDif = s1.row() - s2.row(); // 4 - 7 = -4
		Square startSq = rowDif < 0 ? s1 : s2;
		Square endSq = rowDif < 0 ? s2 : s1;
		int rowIncr = 1;
		int colIncr = startSq.col() - endSq.col() > 0 ? -1 : 1; // -1
		int row = startSq.row();
		int col = startSq.col();
		int dif = Math.abs(rowDif);
		for (int i = 0; i < dif - 1; i++) {
			row += rowIncr;
			col += colIncr;
			btwnSqs.add(new Square(row, col));
		}
		return btwnSqs;
	}

	/**
	 * EFFECT: Sets this Board's enPoissant Square to the input Square
	 * 
	 * @param s
	 *            The Square to make an enPoissantSq
	 */
	public void setEnPoissantSq(Square enPoissantSq) {
		this.enPoissantSq = enPoissantSq;
	}

	/**
	 * Gets this Board's enPoissant Square
	 * 
	 * @return The enPoissantSq of this Board
	 */
	public Square getEnPoissantSq() {
		return enPoissantSq;
	}

	/**
	 * Gets the Piece at the input coordinate
	 * 
	 * @param r
	 *            The row coordinate
	 * @param c
	 *            The column coordinate
	 * @return The Piece at the input coordinate if there is one and null
	 *         otherwise
	 */
	public AbstractPiece getOccupant(int r, int c) {
		return pieces[r][c];
	}

	/**
	 * Is the input coordinate empty?
	 * 
	 * @param r
	 *            The row coordinate
	 * @param c
	 *            The column coordinate
	 * @return true if the input coordinate is empty
	 */
	public boolean isEmpty(int r, int c) {
		return getOccupant(r, c) == null;
	}

	/**
	 * Does the input coordinate contain a Piece?
	 * 
	 * @param r
	 *            The row coordinate
	 * @param c
	 *            The column coordinate
	 * @return true if the input coordinate contains a piece
	 */
	public boolean isOccupied(int r, int c) {
		return !isEmpty(r, c);
	}

	/**
	 * Makes the input coordinate empty
	 * 
	 * @param r
	 *            The row coordinate of where to remove
	 * @param c
	 *            The column coordinate of where to remove
	 * @return The removed piece if there was one and otherwise null
	 */
	public AbstractPiece remove(int r, int c) {
		AbstractPiece removed = getOccupant(r, c);
		pieces[r][c] = null;
		return removed;
	}

	/**
	 * Sets the input piece to the input coordinate
	 * 
	 * @param r
	 *            The row coordinate of where to set the piece
	 * @param c
	 *            The column coordinate of where to set the piece
	 * @param p
	 *            The piece to set at the input coordinate
	 * @return The piece previously at this coordinate if there was one
	 *         otherwise null
	 */
	public AbstractPiece setPiece(int r, int c, AbstractPiece p) {
		AbstractPiece removed = remove(r, c);
		pieces[r][c] = p;
		p.setRow(r);
		p.setCol(c);
		return removed;
	}

	/**
	 * Overloaded setPiece method to take in a square in chess square notation
	 * 
	 * @param c
	 *            The column to set the piece at represented by a char 'a - h'
	 * @param row
	 *            The row to set the piece at, 1-8 with 8 being the 0 row in the
	 *            2D array
	 * @param p
	 *            The piece to take set at the input square
	 * @return The Piece previously at the input square if there was one
	 *         otherwise null
	 */
	public AbstractPiece setPiece(char c, int row, AbstractPiece p) {
		Square s = new Square(c, row);
		return setPiece(s.row(), s.col(), p);
	}

	/**
	 * Moves a piece from the first input coordinate to the second input
	 * coordinate
	 * 
	 * @param r
	 *            The row coordinate of the where we are moving the piece from
	 * @param c
	 *            The column coordinate of the where we are moving the piece
	 *            from
	 * @param r2
	 *            The row coordinate of the where we are moving the piece to
	 * @param c2
	 *            The column coordinate of the where we are moving the piece to
	 * @return The piece taken if there was one, if not it will return null. If
	 *         there was no piece at the first input square, an exception will
	 *         be thrown.
	 */
	public AbstractPiece movePiece(int r, int c, int r2, int c2) {
		if (isEmpty(r, c)) {
			throw new RuntimeException("No piece found at: " + new Square(r, c));
		}
		AbstractPiece p = getOccupant(r, c);
		remove(r, c);
		return setPiece(r2, c2, p);
	}

	/**
	 * EFFECT: Removes all pieces on the board if there were any
	 */
	public void clearBoard() {
		for (int i = 0; i < NUMROWS; i++) {
			for (int j = 0; j < NUMCOLS; j++) {
				remove(i, j);
			}
		}
	}

	/**
	 * EFFECT: Clears the board and fill it with the Pieces at their starting
	 * location
	 */
	public void reset() {
		clearBoard();
		fillWithDefaultPieces();
	}

	/**
	 * Gets all pieces of the input color on this board
	 * 
	 * @param color
	 *            The color of the pieces we are trying to get
	 * @return An List of all pieces of the input color
	 */
	public List<AbstractPiece> getPieces(Color color) {
		List<AbstractPiece> pieces = new ArrayList<AbstractPiece>();
		for (int i = 0; i < NUMROWS; i++) {
			for (int j = 0; j < NUMCOLS; j++) {
				AbstractPiece p = getOccupant(i, j);
				if (p != null && p.getColor() == color) {
					pieces.add(p);
				}
			}
		}
		return pieces;
	}

	public List<AbstractPiece> getAllPieces() {
		List<AbstractPiece> pieces = new ArrayList<AbstractPiece>();
		for (int i = 0; i < NUMROWS; i++) {
			for (int j = 0; j < NUMCOLS; j++) {
				// if (isOccupied(i, j)) {
				pieces.add(getOccupant(i, j));
				// }
			}
		}
		return pieces;
	}

	/**
	 * gets all moves of all pieces of the input color
	 * 
	 * @param color
	 *            the color of the pieces whose moves we will return
	 * @return A List of all moves pieces of the input color can make
	 */
	// public List<Move> getMoves(Color color) {
	// List<Move> moves = new ArrayList<Move>();
	// List<Move> pMoves;
	// for (Piece p : getPieces(color)) {
	// pMoves = p.getMoves(this);
	// if (pMoves != null && pMoves.size() != 0 && pMoves.get(0) != null) {
	// moves.addAll(pMoves);
	// }
	// }
	// return moves;
	// }

	/**
	 * Is the input square being attacked by a piece of the input color?
	 * 
	 * @param r
	 *            The row coordinate of the square we are checking if it is
	 *            attacked
	 * @param c
	 *            The column coordinate of the square we are checking if it is
	 *            attacked
	 * @param color
	 *            The color of pieces we are checking if they attack the input
	 *            square
	 * @return true if the input square is attacked by a piece of the input
	 *         color
	 */
	// public boolean isAttacked(int r, int c, Color color) {
	// for (Piece p : getPieces(color)) {
	// if (p.isAttacking(this, r, c)) {
	// return true;
	// }
	// }
	// return false;
	// }

	/**
	 * Finds the king of the input color
	 * 
	 * @param color
	 *            The color to find the king of
	 * @return The King of the input color throws a runtimeException if there is
	 *         not a King of the input color on this Board
	 */
	public King findKing(Color color) {
		for (int i = 0; i < NUMROWS; i++) {
			for (int j = 0; j < NUMCOLS; j++) {
				AbstractPiece p = getOccupant(i, j);
				if (p != null && p.getColor() == color && p instanceof King) {
					return (King) p;
				}
			}
		}
		throw new RuntimeException("The " + color + " King is not on the Board");
	}

	/**
	 * Is the input color's King in Check?
	 * 
	 * @param color
	 *            The Color of the king we want to find if its in check
	 * @return true if the input Color's King is in check
	 */
	// public boolean kingInCheck(Color color) {
	// return findKing(color).isInCheck(this);
	// }

	/**
	 * Is the input color in checkMate?
	 * 
	 * @param color
	 *            The color to check if they are in checkMate
	 * @return true if the input color is in checkMate
	 */
	// public boolean isCheckMate(Color color) {
	// return kingInCheck(color) && getMoves(color).size() == 0;
	// }

	/**
	 * Is it a draw because the input color can't move
	 * 
	 * @param color
	 *            The color of the side to check if they are causing a draw
	 * @return true if the input color is causing a draw
	 */
	// public boolean isDraw(Color color) {
	// return !kingInCheck(color) && getMoves(color).size() == 0;
	// }

	/**
	 * Is this game over? A game is over if there is checkMate or a draw for
	 * either color
	 * 
	 * @return true if the game is over
	 */
	// public boolean isGameOver() {
	// if (isCheckMate(Color.WHITE) || isCheckMate(Color.BLACK)) {
	// return true;
	// }
	// if (isDraw(Color.WHITE) || isDraw(Color.BLACK)) {
	// return true;
	// }
	// return false;
	// }

	/**
	 * EFFECT: fills the board with the pieces in their starting position
	 */
	public void fillWithDefaultPieces() {
		clearBoard();
		// adding white pieces
		setPiece('a', 1, new Rook(Color.WHITE));
		setPiece('b', 1, new Knight(Color.WHITE));
		setPiece('c', 1, new Bishop(Color.WHITE));
		setPiece('d', 1, new Queen(Color.WHITE));
		setPiece('e', 1, new King(Color.WHITE));
		setPiece('f', 1, new Bishop(Color.WHITE));
		setPiece('g', 1, new Knight(Color.WHITE));
		setPiece('h', 1, new Rook(Color.WHITE));
		setPiece('a', 2, new Pawn(Color.WHITE));
		setPiece('b', 2, new Pawn(Color.WHITE));
		setPiece('c', 2, new Pawn(Color.WHITE));
		setPiece('d', 2, new Pawn(Color.WHITE));
		setPiece('e', 2, new Pawn(Color.WHITE));
		setPiece('f', 2, new Pawn(Color.WHITE));
		setPiece('g', 2, new Pawn(Color.WHITE));
		setPiece('h', 2, new Pawn(Color.WHITE));
		// adding black pieces
		setPiece('a', 8, new Rook(Color.BLACK));
		setPiece('b', 8, new Knight(Color.BLACK));
		setPiece('c', 8, new Bishop(Color.BLACK));
		setPiece('d', 8, new Queen(Color.BLACK));
		setPiece('e', 8, new King(Color.BLACK));
		setPiece('f', 8, new Bishop(Color.BLACK));
		setPiece('g', 8, new Knight(Color.BLACK));
		setPiece('h', 8, new Rook(Color.BLACK));
		setPiece('a', 7, new Pawn(Color.BLACK));
		setPiece('b', 7, new Pawn(Color.BLACK));
		setPiece('c', 7, new Pawn(Color.BLACK));
		setPiece('d', 7, new Pawn(Color.BLACK));
		setPiece('e', 7, new Pawn(Color.BLACK));
		setPiece('f', 7, new Pawn(Color.BLACK));
		setPiece('g', 7, new Pawn(Color.BLACK));
		setPiece('h', 7, new Pawn(Color.BLACK));

	}
}
