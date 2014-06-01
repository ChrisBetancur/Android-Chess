package com.kdoherty.chess;

//TODO: State issues.
// 1. unmake called before make called
// 2. make called twice
// 2. getTaken only works after make has been called 
// 3. isChecking only works if make has not been called
// Possible solution: Keep track of state in boolean

/**
 * @author Kevin Doherty This represents a chess move. A move has a Piece and a
 *         Square to move the Piece to
 */
public final class Move {

	/** The Piece that is moving */
	private Piece piece;

	/** The row this piece is moving to */
	private int row;

	/** The column this piece is moving to */
	private int col;

	/** The starting row of the Piece before this Move is made */
	private int startingRow;

	/** The starting column of the Piece before this Move is made */
	private int startingCol;

	/** The Square to move this piece to */
	private Square targetSquare;

	/**
	 * The Piece that is taken when this move is made, or null if there isnt one
	 */
	private Piece taken;

	/** The type of Move this is */
	private Type type;

	/** The enPoissant square of the board before this Move is played */
	private Square enPoissantSq;

	/** The type of Move this is * @author kdoherty
	 * @version $Revision: 1.0 $
	 */
	public enum Type {
		// TODO: Inheritance instead of this?

		NORMAL, WHITE_LONG, WHITE_SHORT, BLACK_LONG, BLACK_SHORT, PAWN_PROMOTION, EN_POISSANT;

		/**
		 * Is this Type a castling Move?
		 * 
		
		 * @return true if this Type is either WHITE_LONG, WHITE_SHORT,
		 *         BLACK_LONG, or BLACK_SHORT */
		public boolean isCastling() {
			return this == WHITE_SHORT || this == WHITE_LONG
					|| this == BLACK_SHORT || this == BLACK_LONG;
		}
	}

	/**
	 * A Move consists of A Piece and a coordinate where the piece is moving to
	 * 
	 * @param piece
	 *            The Piece that is moving
	 * @param row
	 *            The row this piece is moving to
	 * @param col
	 *            The column this Piece is moving to
	 */
	public Move(Piece piece, int row, int col) {
		this(piece, row, col, Type.NORMAL);
	}

	/**
	 * Overloaded constructor to take in a Square instead of row/column if its
	 * more convenient
	 * 
	 * @param piece
	 *            The Piece that is moving
	 * @param square
	 *            The Square this Move's Piece is moving to
	 */
	public Move(Piece piece, Square square) {
		this(piece, square.row(), square.col());
	}

	/**
	 * Constructor for Move.
	 * @param piece Piece
	 * @param row int
	 * @param col int
	 * @param type Type
	 */
	public Move(Piece piece, int row, int col, Type type) {
		if (piece == null) {
			throw new NullPointerException(
					"Can't make a move with a null piece");
		}
		this.piece = piece;
		this.startingRow = piece.getRow();
		this.startingCol = piece.getCol();
		this.row = row;
		this.col = col;
		this.type = type;
		this.targetSquare = new Square(row, col);
		// this.made = false;
	}

	/**
	 * Gets the piece from this Move
	 * 
	
	 * @return The Piece that is moving */
	public Piece getPiece() {
		return piece;
	}

	/**
	 * Gets the coordinate of this move represented as a Square
	 * 
	
	 * @return the coordinate of this move represented as a Square */
	public Square getSq() {
		return targetSquare;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}

	/**
	 * Method getTaken.
	 * @return Piece
	 */
	public Piece getTaken() {
		return taken;
	}

	/**
	 * Method getType.
	 * @return Type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Makes this move on the input Board
	 * 
	 * @param b
	 *            The Board to make this move on
	
	 */
	public void make(Board b) {
		if (type == Type.EN_POISSANT) {
			int takenRow = piece.getColor() == Color.WHITE ? row + 1 : row - 1;
			taken = b.getOccupant(takenRow, col);
		} else {
			taken = b.getOccupant(row, col);
		}
		enPoissantSq = b.getEnPoissantSq();
		piece.moveTo(b, row, col);
	}

	/**
	 * Method unmake.
	 * @param b Board
	 */
	public void unmake(Board b) {
		piece.decrementMoveCount();
		switch (type) {
		case NORMAL:
			b.remove(row, col);
			b.setPiece(startingRow, startingCol, piece);
			if (taken != null) {
				b.setPiece(row, col, taken);
			}
			b.setEnPoissantSq(enPoissantSq);
			break;
		case PAWN_PROMOTION:
			b.remove(row, col);
			b.setPiece(startingRow, startingCol, new Pawn(piece.getColor()));
			if (taken != null) {
				b.setPiece(row, col, taken);
			}
			break;
		case EN_POISSANT:
			int direction = piece.getColor() == Color.WHITE ? 1 : -1;
			b.remove(row, col);
			b.setPiece(startingRow, startingCol, piece);
			b.setPiece(row + direction, col, taken);
			break;
		default:
			undoCastling(b);
		}
	}

	/**
	 * Method undoCastling.
	 * @param b Board
	 */
	private void undoCastling(Board b) {
		b.setPiece(startingRow, startingCol, b.remove(row, col));
		Rook rook;
		switch (type) {
		case WHITE_LONG:
			rook = (Rook) b.remove(row, col + 1);
			b.setPiece(7, 0, rook);
			break;
		case WHITE_SHORT:
			rook = (Rook) b.remove(row, col - 1);
			b.setPiece(7, 7, rook);
			break;
		case BLACK_LONG:
			rook = (Rook) b.remove(row, col + 1);
			b.setPiece(0, 0, rook);
			break;
		case BLACK_SHORT:
			rook = (Rook) b.remove(row, col - 1);
			b.setPiece(0, 7, rook);
			break;
		default:
			throw new IllegalStateException(
					"Undo castling should only be used on a Castling move not a "
							+ type + " move");
		}
		rook.decrementMoveCount();
		((King) piece).setHasCastled(false);
	}

	/**
	 * Method isChecking.
	 * @param b Board
	 * @return boolean
	 */
	public boolean isChecking(Board b) {
		make(b);
		Color pieceColor = piece.getColor();
		boolean isChecking = b.kingInCheck(pieceColor.opp());
		unmake(b);
		return isChecking;
	}

	/**
	 * Method isTaking.
	 * @return boolean
	 */
	public boolean isTaking() {
		return taken != null;
	}

	/**
	 * Does this Move equal that object? A Move is equal to an object if: 1. The
	 * object is a Move 2. The two moves contain the same Piece 3. The two moves
	 * are moving that Piece to the same Square
	 * 
	
	 * @param obj Object
	 * @return true if this Move equals the input object */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Move)) {
			return false;
		}
		Move that = (Move) obj;
		return that.getSq().equals(getSq()) && that.getPiece().equals(piece);
	}

	/**
	 * Gets the hashCode for this Move
	 * 
	
	 * @return An integer representation of this Move */
	@Override
	public int hashCode() {
		return piece.hashCode() + getSq().hashCode();
	}

	/**
	 * Represents this Move as a String Chess accepted notation for castling is
	 * used
	 * 
	
	 * @return A String representation of this Move */
	@Override
	public String toString() {
		if (piece instanceof King && piece.getCol() == 4) {
			if ((getSq().equals(new Square(7, 6)) || getSq().equals(
					new Square(0, 6)))) {
				return "0-0";
			}
			if ((getSq().equals(new Square(7, 2)) || getSq().equals(
					new Square(0, 2)))) {
				return "0-0-0 ";
			}
		}

		return piece.toString() + targetSquare;
	}
}
