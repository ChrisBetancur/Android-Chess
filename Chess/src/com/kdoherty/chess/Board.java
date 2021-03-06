package com.kdoherty.chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * This class represents a ChessBoard using a 2D array of Pieces. An empty
 * Square on the Board is represented by a null Piece. This Board also contains
 * lists containing all active Pieces for each color. This way we do not need to
 * iterate over all 64 squares to generate the Pieces of a certain color. It
 * keeps track of the side to move, the enPoissant Square if there is one, and
 * the Moves played.
 * 
 * @author Kevin Doherty
 * 
 */
public final class Board {

	/** The number of rows on a chess board */
	public static final int NUM_ROWS = 8;

	/** The number of columns on a chess board */
	public static final int NUM_COLS = 8;

	/**
	 * A board is represented as a 2D array of Pieces An empty Square is
	 * represented by null
	 */
	private final Piece[][] pieces;

	/** All the white Pieces on this Board */
	private final List<Piece> whitePieces;

	/** All the black Pieces on this Board */
	private final List<Piece> blackPieces;

	/**
	 * Keeps track of all the moves played so far on this Board. Note: Not using
	 * a Deque here because com.kdoherty.android.MoveListAdapter requires access
	 * to its elements at any index not just first or last.
	 */
	private final List<Move> moveList;

	/** The Color of whose turn it is */
	private Color sideToMove = Color.WHITE;

	/** Keeps track of the Square where a pawn can be captured by enPoissant */
	private Square enPoissantSq;

	/**
	 * Constructor for Board. Initially contains no Pieces.
	 */
	public Board() {
		pieces = new Piece[NUM_ROWS][NUM_COLS];
		whitePieces = new ArrayList<Piece>();
		blackPieces = new ArrayList<Piece>();
		moveList = new ArrayList<Move>();
	}

	/**
	 * Factory method for a Board filled with all Pieces in their default
	 * locations
	 * 
	 * 
	 * @return A Board with all Pieces in their starting locations
	 */
	public static Board defaultBoard() {
		Board board = new Board();
		board.fillWithDefaultPieces();
		return board;
	}

	/**
	 * Are the input row and column on a chess board?
	 * 
	 * @param row
	 *            row coordinate
	 * @param col
	 *            column coordinate
	 * @return is coordinate (r, c) on a chess board?
	 */
	public static boolean isInbounds(int row, int col) {
		return row < NUM_ROWS && row >= 0 && col >= 0 && col < NUM_COLS;
	}

	/**
	 * Takes in two locations on the board and determines if they are diagonal
	 * 
	 * @param row
	 *            row coordinate for first location
	 * @param col
	 *            column coordinate for first location
	 * @param row2
	 *            row coordinate for second location
	 * @param col2
	 *            column coordinate for second location
	 * @return true if the two locations are diagonal
	 */
	public static boolean sameDiagonal(int row, int col, int row2, int col2) {
		double colDif = col2 - col;
		if (colDif != 0) {
			// Note: double compare with == is OK here
			return Math.abs((row2 - row) / colDif) == 1.0;
		}
		return false;
	}

	/**
	 * Takes in two locations on the board and determines if they are next to
	 * each other Not using getNeighbors because we want to stop looping as soon
	 * as we find the square in neighbors. Does not check isInbound
	 * 
	 * @param r
	 *            row coordinate for first location
	 * @param c
	 *            column coordinate for first location
	 * @param r2
	 *            row coordinate for second location
	 * @param c2
	 *            column coordinate for second location
	 * 
	 * @return true if the two locations are neighbors
	 */
	public static boolean isNeighbor(int r, int c, int r2, int c2) {
		for (int i = r2 - 1; i < r2 + 2; i++) {
			for (int j = c2 - 1; j < c2 + 2; j++) {
				if (!(i == r2 && j == c2) && (i == r && j == c)) {
					return true;
				}
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
	 * 
	 * @return An <code>List</code> of all squares between the input squares
	 */
	public static List<Square> getBtwnSqs(Square s1, Square s2) {
		if (s1.equals(s2)) {
			throw new IllegalArgumentException("Can't get Squares between "
					+ s1 + " and " + s2);
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
		throw new IllegalArgumentException("Can't Squares between " + s1
				+ " and " + s2);
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
	 * 
	 * @return A <code>List</code> of all squares between the input squares
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
	 * 
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
	 * 
	 * @return An <code>List</code> of all squares between the input squares
	 */
	private static List<Square> getBtwnSqsDiag(Square s1, Square s2) {
		List<Square> btwnSqs = new ArrayList<Square>();
		int rowDiff = s1.row() - s2.row();
		Square startSq = rowDiff < 0 ? s1 : s2;
		Square endSq = rowDiff < 0 ? s2 : s1;
		int rowIncr = 1;
		int colIncr = startSq.col() - endSq.col() > 0 ? -1 : 1;
		int row = startSq.row();
		int col = startSq.col();
		int diff = Math.abs(rowDiff);
		for (int i = 0; i < diff - 1; i++) {
			row += rowIncr;
			col += colIncr;
			btwnSqs.add(new Square(row, col));
		}
		return btwnSqs;
	}

	/**
	 * Deep clones a List
	 * 
	 * @param list
	 *            The List to clone
	 * 
	 * @return A List containing all the same elements as the input list
	 */
	private static <T> List<T> cloneList(List<? extends T> list) {
		List<T> clone = new ArrayList<T>();
		for (T t : list) {
			clone.add(t);
		}
		return clone;
	}

	/**
	 * Gets this Board's enPoissant Square
	 * 
	 * 
	 * @return The enPoissantSq of this Board
	 */
	public Square getEnPoissantSq() {
		return enPoissantSq;
	}

	/**
	 * Sets this Board's enPoissant Square to the input Square
	 * 
	 * @param enPoissantSq
	 *            The Square to make an enPoissantSq
	 */
	public void setEnPoissantSq(Square enPoissantSq) {
		if (enPoissantSq == null || enPoissantSq.row() == 2
				|| enPoissantSq.row() == 5) {
			this.enPoissantSq = enPoissantSq;
		} else {
			throw new RuntimeException("enPoissantSquare can't be set to "
					+ enPoissantSq);
		}
	}

	/**
	 * Gets the color of the side to move
	 * 
	 * 
	 * @return The color of whose turn it is to move
	 */
	public Color getSideToMove() {
		return sideToMove;
	}

	/**
	 * Toggles the color of the side to move
	 */
	public void toggleSideToMove() {
		this.sideToMove = sideToMove.opp();
	}

	/**
	 * Adds a move to the list of Moves played on this Board
	 * 
	 * @param move
	 *            The Move to add
	 */
	public void addMove(Move move) {
		moveList.add(move);
	}

	/**
	 * Removes the most recently played move from the list of Moves played on
	 * this Board
	 */
	public void undoMove() {
		if (!moveList.isEmpty()) {
			moveList.remove(moveList.size() - 1).unmake();
		}
	}

	/**
	 * Gets the last move played on this Board
	 * 
	 * 
	 * @return The last move played on this Board
	 */
	public Move getLastMove() {
		if (moveList.isEmpty()) {
			return null;
		}
		return moveList.get(moveList.size() - 1);
	}

	/**
	 * Gets the number of moves played on this Board
	 * 
	 * 
	 * @return The number of moves played on this Board
	 */
	public int getMoveCount() {
		return moveList.size();
	}

	/**
	 * Gets the Piece at the input coordinate
	 * 
	 * @param r
	 *            The row coordinate
	 * @param c
	 *            The column coordinate
	 * 
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
	 * 
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
	 * 
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
	 * 
	 * @return The removed piece if there was one and otherwise null
	 */
	public Piece remove(int r, int c) {
		Piece removed = getOccupant(r, c);
		if (removed != null) {
			if (removed.getColor() == Color.WHITE) {
				whitePieces.remove(removed);
			} else {
				blackPieces.remove(removed);
			}
		}
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
	 * 
	 * @return The piece previously at this coordinate if there was one
	 *         otherwise null
	 */
	public Piece setPiece(int r, int c, Piece p) {
		if (p == null) {
			throw new NullPointerException("Can't set a null piece");
		}
		Piece removed = remove(r, c);
		pieces[r][c] = p;
		p.setRow(r);
		p.setCol(c);
		if (p.getColor() == Color.WHITE) {
			whitePieces.add(p);
		} else if (p.getColor() == Color.BLACK) {
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
	 * 
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
	 * 
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
	 * Gets all pieces of the input color on this board
	 * 
	 * @param color
	 *            The color of the pieces we are trying to get
	 * 
	 * @return An List of all pieces of the input color
	 */
	public List<Piece> getPieces(Color color) {
		return color == Color.WHITE ? cloneList(whitePieces)
				: cloneList(blackPieces);
	}

	/**
	 * Gets all moves of all Pieces of the input Color
	 * 
	 * @param color
	 *            the Color of the Pieces whose moves we will return
	 * 
	 * @return A List of all moves Pieces of the input Color can make
	 */
	public List<Move> getMoves(Color color) {
		List<Move> moves = new ArrayList<>();
		List<Move> pMoves;
		List<Piece> pieces = getPieces(color);
		for (Piece p : pieces) {
			pMoves = p.getMoves(this);
			if (!pMoves.isEmpty()) {
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
	 * 
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
	 * 
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
	 * 
	 * @return true if the input Color's King is in check
	 */
	public boolean kingInCheck(Color color) {
		return findKing(color).isInCheck(this);
	}

	/**
	 * Is the input color in check mate?
	 * 
	 * @param color
	 *            The color to check if they are in checkMate
	 * 
	 * @return true if the input color is in checkMate
	 */
	public boolean isCheckMate(Color color) {
		return kingInCheck(color) && getMoves(color).isEmpty();
	}

	/**
	 * Is it a draw because the input color can't move?
	 * 
	 * @param color
	 *            The color of the side to check if they are causing a draw
	 * 
	 * @return true if the input color is causing a draw
	 */
	public boolean isDraw(Color color) {
		return !kingInCheck(color) && getMoves(color).isEmpty();
	}

	/**
	 * Is this game over? A game is over if there is checkMate or a draw for
	 * either color
	 * 
	 * 
	 * @return true if the game is over
	 */
	public boolean isGameOver() {
		return isCheckMate(Color.WHITE) || isCheckMate(Color.BLACK)
				|| isDraw(Color.WHITE) || isDraw(Color.BLACK);
	}

	/**
	 * Do the two lists contain the same elements. Order is ignored.
	 * 
	 * @param listOne
	 *            One list to check
	 * @param listTwo
	 *            Another list to check
	 * 
	 * @return Do the two input lists contain the same Elements
	 */
	private static <T> boolean sameElements(List<T> listOne, List<T> listTwo) {
		return listOne.containsAll(listTwo) && listOne.size() == listTwo.size();
	}

	/**
	 * A Board is equal to another Object if: 
	 * 1. The Object is a Board 
	 * 2. They contain the same Pieces in the same positions 
	 * 3. They have the same enPoissant Square 
	 * 4. They have the same sideToMove
	 * 
	 * @param obj
	 *            Object The object to test equality with this Board
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
		Square thatEnPoissantSq = thatBoard.getEnPoissantSq();
		Color thatSideToMove = thatBoard.getSideToMove();

		return Arrays.deepEquals(pieces, thatBoard.pieces)
				&& sideToMove == thatSideToMove
				&& (enPoissantSq == thatEnPoissantSq ||
				   (enPoissantSq != null && enPoissantSq.equals(thatEnPoissantSq)))
				&& sameElements(whitePieces, thatBoard.whitePieces)
				&& sameElements(blackPieces, thatBoard.blackPieces);
	}

	/**
	 * Generates an integer representation of this Board. This will return the
	 * same number for equal Boards.
	 * 
	 * 
	 * @return An integer representation of this Board
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(pieces);
		result = prime * result + ((blackPieces == null) ? 0 : Arrays.deepHashCode(blackPieces.toArray()));
		result = prime * result + ((whitePieces == null) ? 0 : Arrays.deepHashCode(whitePieces.toArray()));
		result = prime * result + ((enPoissantSq == null) ? 0 : enPoissantSq.hashCode());
		result = prime * result + ((sideToMove == null) ? 0 : sideToMove.hashCode());
		return result;
	}

	/**
	 * A readable ASCII representation of this Board
	 * 
	 * 
	 * @return A String representation of this Board
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		StringBuilder colNum = new StringBuilder("  ");
		for (int j = 0; j < NUM_COLS; j++) {
			colNum.append(j);
			colNum.append(" ");
		}
		colNum.append("\n");
		for (int i = 0; i < NUM_ROWS; i++) {
			sb.append(i);
			sb.append("|");
			for (int j = 0; j < NUM_COLS; j++) {
				if (isOccupied(i, j)) {
					sb.append(getOccupant(i, j));
				} else {
					sb.append(" ");
				}
				sb.append("|");
			}
			sb.append("\n");
		}
		sb.append("\n");
		sb.insert(0, colNum.toString());
		sb.append("enPoissant Square: ");
		sb.append(enPoissantSq);
		sb.append(" side toMove: ");
		sb.append(sideToMove);
		sb.append(" moveList: ");
		sb.append(moveList);
		return sb.toString();
	}

	/**
	 * Generates a copy of this Board such that this.equals(this.clone()) will
	 * always be true but this == this.clone() is false.
	 * 
	 * @return A deep copy of this Board
	 */
	@Override
	public Board clone() {
		Board clone = new Board();

		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				Piece piece = getOccupant(i, j);
				if (piece != null) {
					clone.setPiece(i, j, piece.clone());
				}
			}
		}

		clone.enPoissantSq = enPoissantSq;
		clone.sideToMove = sideToMove;
		clone.moveList.addAll(moveList);

		return clone;
	}

	/**
	 * Fills this Board with all Pieces in their starting locations
	 */
	private void fillWithDefaultPieces() {
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
