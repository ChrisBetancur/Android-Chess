package com.kdoherty.chess;


import java.util.ArrayList;

/**
 * This class represents a Chess Piece
 * This is a superClass for Pawn, Knight, Bishop,
 * Rook, Queen, and King
 * @author Kevin Doherty
 * @version 10/14/2013
 *
 */
public abstract class Piece {

    /** The row where this Piece is located */
    int row;

    /** The column where this Piece is located */
    int col;

    /** The color of this Piece */
    final Color color;

    /**
     * The constructor for Piece
     * @param color The color of this Piece
     * note: Does not set row and column so we can make a piece
     * this is done when the Piece is set on a Board
     */
    public Piece(Color color) {
        this.color = color;
    }

    /**
     * Sets this piece's row to the input integer
     * @param row The integer to set this pieces row to
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Sets this piece's column to the input integer
     * @param col The integer to set this pieces column to
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Gets the row coordinate of this Piece
     * @return The row coordinate of this Piece
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Gets the column coordinate of this Piece
     * @return The column coordinate of this Piece
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Gets the Square which this Piece is on
     * @return The Square which this Piece is on
     */
    public Square getSq() {
        return new Square(this.row, this.col);
    }

    /**
     * Gets the Color of this Piece
     * @return The Color of this Piece
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Can this piece move on the input board to the input square?
     * @param b The Board the piece is checking if it can move on
     * @param r The row we are checking if the piece can move to
     * @param c The column we are checking if the piece can move to
     * @return true if the Piece can move to the input square and false otherwise
     */
    public abstract boolean canMove(Board b, int r, int c);

    /**
     * Is this Piece attacking the input square?
     * Note it is still attacking the square even if it is pinned
     * to it's king
     * @param b The Board we are checking if the Piece is attacking a square on
     * @param r The row we are checking if this Piece is attacking
     * @param c The row we are checking if this Piece is attacking
     * @return true if this Piece is attacking the input square on the
     * input Board and false otherwise
     */
    public abstract boolean isAttacking(Board b, int r, int c);

    /**
     * EFFECT:
     * Moves this Piece to the input square if it can move there
     * @param b The Board we are moving this Piece on
     * @param r The row we are moving this Piece to
     * @param c The column we are moving this Piece to
     */

    public void moveTo(Board b, int r, int c) {
        if (this.canMove(b, r, c)) {
            b.movePiece(this.row, this.col, r, c);
        }
    }
    
    /**
     * Is this Piece defending the input square?
     * Note it is not defending the square if it is pinned to 
     * it's king, or if the input square contains a Piece of 
     * the opposite color
     * @param b The Board we are checking if this Piece is defending a square on
     * @param r The row we are checking if it is defended by this piece
     * @param c The column we are checking if it is defended by this piece
     * @return true if this Piece is defending the input square on the input
     * Board and false otherwise
     */
    public abstract boolean isDefending(Board b, int r, int c);

    /**
     * Finds all moves this Piece can make on the input Board
     * @param b The Board on which we are getting all moves of this Piece
     * @return All moves this Piece can make on the input Board
     */
    public abstract ArrayList<Move> getMoves(Board b);

    /**
     * A piece equals another Object if they are:
     * 1. Both Pieces of the same type
     * 2. Have the same color
     * 3. Are located on the same row
     * 4. Are located on the same column
     * @param o The object we are checking if it is equal to this Piece
     * @return true if this piece is equal to the input object
     */
    public boolean equals(Object o) {
        return o != null && 
                o.getClass() == this.getClass() &&
                color == ((Piece)o).getColor() &&
                row == ((Piece)o).getRow() &&
                col == ((Piece)o).getCol();
    }
    
    public int hashCode() {
    	return toString().hashCode();
    }

    /**
     * This will represent the piece as a String
     * If the piece is white a lowerCase letter will be used
     * If the piece is black an upperCase letter will be used
     * @return A String representation of this Piece
     */
    public abstract String toString();

    /**
     * A piece is taking if the input square contains a Piece of the opposite color
     * @param b The board to check if the piece is taking on
     * @param r The row coordinate of the square to check
     * if this Piece is taking another piece on
     * @param c The column coordinate of the square to check
     * if this Piece is taking another piece on
     * @return true if this Piece would take a piece if it moved to the coordinate
     */
    public boolean isTaking(Board b, int r, int c) {
        return b.isOccupied(r, c) && b.getOccupant(r, c).getColor() != this.color;   
    }

    /**
     * Is this piece blocked going from its current square to the input square on the input board
     * @param b The board we are checking if this piece is blocked on
     * @param r The row coordinate of the intended Square
     * @param c The column coordinate of the intend Square
     * @return true if this piece is blocked going from its current square to the input square
     */
    public boolean isBlocked(Board b, int r, int c) {
        for (Square square : Board.getBtwnSqs(this.getSq(), new Square(r, c))) {
            if (b.isOccupied(square.getRow(), square.getCol())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Can this Piece move to the input coordinate without putting its King in check?
     * @param b The Board this Piece is trying to move on
     * @param r The row this Piece is trying to move to
     * @param c The column this Piece is trying to move to
     * @return true if this Piece can move without putting its King in check
     */
    
    public boolean stillInCheck(Board b, int r, int c) {
        boolean stillInCheck = true;
        Move m = new Move(this, r, c);
        int oldRow = this.row;
        int oldCol = this.col;
        Piece taken = m.makeMove(b);
        if (!(b.kingInCheck(this.color))) {
            stillInCheck = false;
        }
        m.undo(b, oldRow, oldCol, taken);
        return stillInCheck;
    }
}

