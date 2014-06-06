package com.kdoherty.chess;

/**
 * @author Kevin Doherty This represents a chess move. A move has a Piece and a
 *         Square to move the Piece to
 */
public final class Move {

	private Board board;

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

	private int pieceMoveCount;

	private boolean made = false;

	/**
	 * The type of Move this is
	 * 
	 */
	public enum Type {
		// TODO: Inheritance instead of this?

		NORMAL, WHITE_LONG, WHITE_SHORT, BLACK_LONG, BLACK_SHORT, PROMOTION_QUEEN, PROMOTION_KNIGHT, EN_POISSANT;

		/**
		 * Is this Type a castling Move?
		 * 
		 * 
		 * @return true if this Type is either WHITE_LONG, WHITE_SHORT,
		 *         BLACK_LONG, or BLACK_SHORT
		 */
		public boolean isCastling() {
			return this == WHITE_SHORT || this == WHITE_LONG
					|| this == BLACK_SHORT || this == BLACK_LONG;
		}
		
		public boolean isPromotion() {
			return this == PROMOTION_QUEEN || this == PROMOTION_KNIGHT;
		}
	}

	public Move(Board board, Piece piece, int row, int col) {
		this(board, piece, row, col, Type.NORMAL);
		initType();
	}

	public Move(Board board, Piece piece, Square square) {
		this(board, piece, square.row(), square.col());
	}

	public Move(Board board, Piece piece, Square square, Type type) {
		this(board, piece, square.row(), square.col(), type);
	}

	public Move(Board board, Piece piece, int row, int col, Type type) {
		this.board = board;
		this.piece = piece;
		this.row = row;
		this.col = col;
		this.targetSquare = new Square(row, col);
		this.startingRow = piece.getRow();
		this.startingCol = piece.getCol();
		this.type = type;
		if (type.isPromotion()) {
			this.pieceMoveCount = piece.getMoveCount();
		}
	}

	public void initType() {
		if (piece instanceof King
				&& !Board.isNeighbor(startingRow, startingCol, row, col)) {
			if (col == 2) {
				if (piece.getColor() == Color.WHITE) {
					this.type = Type.WHITE_LONG;
				} else {
					this.type = Type.BLACK_LONG;
				}
			} else if (col == 6) {
				if (piece.getColor() == Color.WHITE) {
					this.type = Type.WHITE_SHORT;
				} else {
					this.type = Type.BLACK_SHORT;
				}
			}
		} else if (piece instanceof Pawn) {
			Pawn pawn = (Pawn) piece;
			int promotingRow = pawn.getColor() == Color.WHITE ? 0 : 7;
			if (row == promotingRow) {
				this.type = Type.PROMOTION_QUEEN;
				this.pieceMoveCount = piece.getMoveCount();
			} else if (Board.sameDiagonal(startingRow, startingCol, row, col)
					&& board.isEmpty(row, col)) {
				this.type = Type.EN_POISSANT;
			} else {
				this.type = Type.NORMAL;
			}
		} else {
			this.type = Type.NORMAL;
		}
	}

	/**
	 * Gets the piece from this Move
	 * 
	 * @return The Piece that is moving
	 */
	public Piece getPiece() {
		return piece;
	}

	/**
	 * Gets the coordinate of this move represented as a Square
	 * 
	 * 
	 * @return the coordinate of this move represented as a Square
	 */
	public Square getSq() {
		return targetSquare;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Piece getTaken() {
		if (!made) {
			throw new RuntimeException("Taken has not been initialized");
		}
		return taken;
	}

	/**
	 * Method getType.
	 * 
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
	public void make() {
		make(board);
	}

	public void make(Board board) {
		if (made) {
			throw new RuntimeException("Can't make a move twice");
		}
		made = true;
		enPoissantSq = board.getEnPoissantSq();
		if (type == Type.EN_POISSANT) {
			int takenRow = piece.getColor() == Color.WHITE ? row + 1 : row - 1;
			taken = board.getOccupant(takenRow, col);
		} else {
			taken = board.getOccupant(row, col);
		}
		if (type.isPromotion()) {
			Piece promoteTo = new Queen(piece.getColor());
			if (type == Type.PROMOTION_KNIGHT) {
				promoteTo = new Knight(piece.getColor());
			}
			piece.incrementMoveCount();
			board.setEnPoissantSq(null);
			board.remove(startingRow, startingCol);
			board.setPiece(row, col, promoteTo);
		} else {
			piece.moveTo(board, row, col);
		}
	}

	public void unmake() {
		unmake(board);
	}

	public void unmake(Board board) {
		if (!made) {
			throw new RuntimeException("Can't undo a move before making it");
		}
		made = false;
		piece.decrementMoveCount();
		switch (type) {
		case NORMAL:
			board.remove(row, col);
			board.setPiece(startingRow, startingCol, piece);
			if (taken != null) {
				board.setPiece(row, col, taken);
			}
			board.setEnPoissantSq(enPoissantSq);
			break;
		case PROMOTION_KNIGHT:
			// Fall though because unmake is handled in the same way for both promotion types
		case PROMOTION_QUEEN:
			board.remove(row, col);
			Pawn replacement = new Pawn(piece.getColor());
			replacement.moveCount = pieceMoveCount;
			board.setPiece(startingRow, startingCol, replacement);
			if (taken != null) {
				board.setPiece(row, col, taken);
			}
			break;
		case EN_POISSANT:
			board.remove(row, col);
			board.setPiece(startingRow, startingCol, piece);
			int direction = piece.getColor() == Color.WHITE ? 1 : -1;
			board.setPiece(row + direction, col, taken);
			board.setEnPoissantSq(enPoissantSq);
			break;
		default:
			undoCastling();
		}
	}

	private void undoCastling() {
		board.setPiece(startingRow, startingCol, board.remove(row, col));
		Rook rook;
		switch (type) {
		case WHITE_LONG:
			rook = (Rook) board.remove(row, col + 1);
			board.setPiece(7, 0, rook);
			break;
		case WHITE_SHORT:
			rook = (Rook) board.remove(row, col - 1);
			board.setPiece(7, 7, rook);
			break;
		case BLACK_LONG:
			rook = (Rook) board.remove(row, col + 1);
			board.setPiece(0, 0, rook);
			break;
		case BLACK_SHORT:
			rook = (Rook) board.remove(row, col - 1);
			board.setPiece(0, 7, rook);
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
	 * 
	 * @param b
	 *            Board
	 * @return boolean
	 */
	public boolean isChecking() {
		boolean undo = false;
		if (!made) {
			make();
			undo = true;
		}
		Color pieceColor = piece.getColor();
		boolean isChecking = board.kingInCheck(pieceColor.opp());
		if (undo) {
			unmake();
		}
		return isChecking;
	}

	/**
	 * Method isTaking.
	 * 
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
	 * 
	 * @param obj
	 *            Object
	 * @return true if this Move equals the input object
	 */
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
	 * 
	 * @return An integer representation of this Move
	 */
	@Override
	public int hashCode() {
		return piece.hashCode() + getSq().hashCode();
	}

	/**
	 * Represents this Move as a String Chess accepted notation for castling is
	 * used
	 * 
	 * 
	 * @return A String representation of this Move
	 */
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
