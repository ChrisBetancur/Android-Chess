package com.kdoherty.chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * @author Kevin Doherty
 * @version 10/14/2013
 * 
 *          This class represents a ChessBoard using a 2D array of Pieces. An
 *          empty Square on the Board is represented by a null Piece. This Board
 *          also contains lists containing all active Pieces for each color.
 *          This way we do not need to iterate over all 64 squares to generate
 *          the Pieces of a certain color. It keeps track of the side to move,
 *          the enPoissant Square if there is one, and the number of moves
 *          played.
 * 
 */
public class Board extends Observable {

	/** The number of rows on a chess board */
	public final static int NUM_ROWS = 8;

	/** The number of columns on a chess board */
	public final static int NUM_COLS = 8;

	/**
	 * A board is represented as a 2D array of Pieces An empty Square is
	 * represented by null
	 */
	private final Piece[][] pieces;

	/** All the white Pieces on this Board */
	private List<Piece> whitePieces;

	/** All the black Pieces on this Board */
	private List<Piece> blackPieces;

	/** The Color of whose turn it is */
	private Color sideToMove = Color.WHITE;

	/** Keeps track of the Square where a pawn can be captured by enPoissant */
	private Square enPoissantSq;

	/** The number of moves that have been played so far */
	private int moveCount;

	/**
	 * Constructor for Board. Initially sets all squares to null
	 */
	public Board() {
		pieces = new Piece[NUM_ROWS][NUM_COLS];
		whitePieces = new ArrayList<Piece>();
		blackPieces = new ArrayList<Piece>();
	}

	/**
	 * Factory method for the Board filled with all the Pieces in their default
	 * location
	 * 
	 * @return A Board with all Pieces in their starting location
	 */
	public static Board defaultBoard() {
		Board b = new Board();
		b.fillWithDefaultPieces();
		return b;
	}

	/**
	 * Are the input row and column on a chess board?
	 * 
	 * @param r
	 *            row coordinate
	 * @param c
	 *            column coordinate
	 * @return is coordinate (r, c) on a chess board?
	 */
	public static boolean isInbounds(int row, int col) {
		return row < NUM_ROWS && row >= 0 && col >= 0 && col < NUM_COLS;
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
	 * Helper method for getBtwnSqs. Only to be called on squares which are in
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
	 * Helper method for getBtwnSqs. Only to be called on squares which are in
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
	 * Helper method for getBtwnSqs. Only to be called on squares which are in
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
		int rowDif = s1.row() - s2.row();
		Square startSq = rowDif < 0 ? s1 : s2;
		Square endSq = rowDif < 0 ? s2 : s1;
		int rowIncr = 1;
		int colIncr = startSq.col() - endSq.col() > 0 ? -1 : 1;
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
	 * Gets the number of moves played on this Board
	 * 
	 * @return The number of moves that have been played so far
	 */
	public int getMoveCount() {
		return moveCount;
	}

	/**
	 * Sets the move count
	 * 
	 * @param moveCount
	 *            The number to set the move count to
	 */
	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
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
	 * Sets this Board's enPoissant Square to the input Square
	 * 
	 * @param s
	 *            The Square to make an enPoissantSq
	 */
	public void setEnPoissantSq(Square enPoissantSq) {
		this.enPoissantSq = enPoissantSq;
	}

	/**
	 * Gets the color of the side to move
	 * 
	 * @return The color of whose turn it is to move
	 */
	public Color getSideToMove() {
		return sideToMove;
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
	public Piece getOccupant(int r, int c) {
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
	public Piece remove(int r, int c) {
		Piece removed = getOccupant(r, c);
		pieces[r][c] = null;
		if (removed != null) {
			if (removed.getColor() == Color.WHITE) {
				whitePieces.remove(removed);
			} else {
				blackPieces.remove(removed);
			}
		}
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
	public Piece setPiece(int r, int c, Piece p) {
		Piece removed = remove(r, c);
		pieces[r][c] = p;
		p.setRow(r);
		p.setCol(c);
		if (p.getColor() == Color.WHITE) {
			whitePieces.add(p);
		} else {
			blackPieces.add(p);
		}
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
	public Piece setPiece(char c, int row, Piece p) {
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
	public Piece movePiece(int r, int c, int r2, int c2) {
		if (isEmpty(r, c)) {
			throw new RuntimeException("No piece found at: " + new Square(r, c));
		}
		Piece p = getOccupant(r, c);
		remove(r, c);
		return setPiece(r2, c2, p);
	}

	/**
	 * Removes all pieces on the board if there were any
	 */
	public void clearBoard() {
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				remove(i, j);
			}
		}
	}

	/**
	 * Clears the board and fill it with the Pieces at their starting location
	 */
	public void reset() {
		clearBoard();
		fillWithDefaultPieces();
	}

	/**
	 * Toggles the color of the side to move
	 */
	public void toggleSideToMove() {
		this.sideToMove = sideToMove.opp();
	}

	public void toggleSideToMoveUpdate() {
		this.sideToMove = sideToMove.opp();
		setChanged();
		notifyObservers(sideToMove);
	}

	/**
	 * Gets all pieces of the input color on this board
	 * 
	 * @param color
	 *            The color of the pieces we are trying to get
	 * @return An List of all pieces of the input color
	 */
	public List<Piece> getPieces(Color color) {
		return color == Color.WHITE ? cloneList(whitePieces)
				: cloneList(blackPieces);
	}

	/**
	 * Deep clones a List
	 * 
	 * @param list
	 *            The List to clone
	 * @return An ArrayList containing all the same elements as the input list
	 */
	private static <T> List<T> cloneList(List<? extends T> list) {
		List<T> clone = new ArrayList<T>();
		for (T t : list) {
			clone.add(t);
		}
		return clone;
	}

	/**
	 * Gets all moves of all Pieces of the input Color
	 * 
	 * @param color
	 *            the Color of the Pieces whose moves we will return
	 * @return A List of all moves Pieces of the input Color can make
	 */
	public List<Move> getMoves(Color color) {
		List<Move> moves = new ArrayList<Move>();
		List<Move> pMoves;
		for (Piece p : getPieces(color)) {
			pMoves = p.getMoves(this);
			if (pMoves != null && pMoves.size() != 0) {
				moves.addAll(pMoves);
			}
		}
		return moves;
	}

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
	public boolean isAttacked(int r, int c, Color color) {
		for (Piece p : getPieces(color)) {
			if (p.isAttacking(this, r, c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds the king of the input color
	 * 
	 * @param color
	 *            The color to find the king of
	 * @return The King of the input color throws a runtimeException if there is
	 *         not a King of the input color on this Board
	 */
	public King findKing(Color color) {
		for (Piece piece : getPieces(color)) {
			if (piece instanceof King) {
				return (King) piece;
			}
		}
		throw new IllegalStateException("The " + color
				+ " King is not on the Board");
	}

	/**
	 * Is the input color's King in Check?
	 * 
	 * @param color
	 *            The Color of the king we want to find if its in check
	 * @return true if the input Color's King is in check
	 */
	public boolean kingInCheck(Color color) {
		return findKing(color).isInCheck(this);
	}

	/**
	 * Is the input color in checkMate?
	 * 
	 * @param color
	 *            The color to check if they are in checkMate
	 * @return true if the input color is in checkMate
	 */
	public boolean isCheckMate(Color color) {
		return kingInCheck(color) && getMoves(color).size() == 0;
	}

	/**
	 * Is it a draw because the input color can't move
	 * 
	 * @param color
	 *            The color of the side to check if they are causing a draw
	 * @return true if the input color is causing a draw
	 */
	public boolean isDraw(Color color) {
		return !kingInCheck(color) && getMoves(color).size() == 0;
	}

	/**
	 * Is this game over? A game is over if there is checkMate or a draw for
	 * either color
	 * 
	 * @return true if the game is over
	 */
	public boolean isGameOver() {
		return isCheckMate(Color.WHITE) || isCheckMate(Color.BLACK)
				|| isDraw(Color.WHITE) || isDraw(Color.BLACK);
	}

	/**
	 * Fills the board with the pieces in their starting position
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

	/**
	 * Generates an integer representation of this Board
	 * 
	 * @return An integer representation of this Board
	 */
	@Override
	public int hashCode() {
		int result = 37;
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			for (int j = 0; j < Board.NUM_COLS; j++) {
				Piece piece = getOccupant(i, j);
				if (piece != null) {
					result += piece.hashCode();
				}
			}
		}
		return result;
	}

	/**
	 * A Board is equal to another Board if it contains all the same Pieces on
	 * all the same squares.
	 * 
	 * @return Does this Board equal that object?
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Board)) {
			return false;
		}
		Board thatBoard = (Board) obj;
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			for (int j = 0; j < Board.NUM_COLS; j++) {
				Piece thisPiece = getOccupant(i, j);
				Piece thatPiece = thatBoard.getOccupant(i, j);
				if ((thisPiece == null && thatPiece != null)
						|| !thisPiece.equals(thatPiece)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * A readable ASCII representation of this Board
	 * @return A String representation of this Board
	 */
	@Override
	public String toString() {
		String b = "";
		String colNum = "  ";
		for (int j = 0; j < NUM_COLS; j++) {
			colNum += j + " ";
		}
		colNum += "\n";
		for (int i = 0; i < NUM_ROWS; i++) {
			b = b + i + "|";
			for (int j = 0; j < NUM_COLS; j++) {
				if (pieces[i][j] != null)
					b = b + pieces[i][j];
				else
					b = b + " ";
				b = b + "|";
			}
			b = b + "\n";
		}
		b = b + "\n";
		b = colNum + b;
		return b;
	}
}
