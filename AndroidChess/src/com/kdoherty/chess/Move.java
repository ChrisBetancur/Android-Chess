package com.kdoherty.chess;

/**
 * @author Kevin Doherty
 * @version 10/14/2013
 * This represents a chess move. A move has a piece and a 
 * Square to move the piece to
 */
public class Move {

    /** The Piece that is moving */
    private AbstractPiece piece;
    
    /** The row this piece is moving to */
    private int row;
    
    /** The column this piece is moving to */
    private int col;

    /**
     * A Move consists of A Piece and a coordinate where the piece is moving to
     * @param piece The Piece that is moving
     * @param row The row this piece is moving to
     * @param col The column this Piece is moving to
     */
    public Move(AbstractPiece piece, int row, int col) {
        this.piece = piece;
        this.row = row;
        this.col = col;
    }
    
    /**
     * Overloaded constructor to take in a Square 
     * instead of row/column if its more convenient 
     * @param piece The Piece that is moving
     * @param square The Square this Move's Piece is moving to
     */
    public Move(AbstractPiece piece, Square square) {
        this.piece = piece;
        this.row = square.row();
        this.col = square.col();
    }

    /**
     * Gets the piece from this Move
     * @return The Piece that is moving 
     */
    public AbstractPiece getPiece() {
        return piece;
    }

    /**
     * Gets the coordinate of this move represented as a Square
     * @return the coordinate of this move represented as a Square
     */
    public Square getSq() {
        return new Square(row, col);
    }

    /**
     * Does this Move equal that object?
     * A Move is equal to an object if:
     * 1. The object is a Move
     * 2. The two moves contain the same Piece
     * 3. The two moves are moving that piece to the same Square
     * @return true if this Move equals the input object
     */
    public boolean equals(Object obj) {      
            return obj instanceof Move && (((Move)obj).getSq()).equals(getSq()) && 
                    ((Move)obj).getPiece().equals(piece);
    }
    
    /**
     * Gets the hashCode for this Move
     * @return An integer representation of this Move
     */
    public int hashCode() {
        return piece.hashCode() + getSq().hashCode();
    }
 
    /**
     * Makes this move on the input Board
     * @param b The Board to make this move on
     * @return The piece captured by making this move if there was one
     * otherwise null
     */
    public AbstractPiece makeMove(Board b) {
        b.remove(piece.getRow(), piece.getCol());
        return b.setPiece(row, col, piece);
    }

    /**
     * EFFECT:
     * Takes back this move
     * @param b The Board to undo this Move on
     * @param prevRow The row from which Piece came from
     * @param prevCol The column from which Piece came from
     * @param taken The Piece which was taken after this Move was made
     */
    public void undo(Board b, int prevRow, int prevCol, AbstractPiece taken) {
        b.remove(row, col);
        b.setPiece(prevRow, prevCol, getPiece());
        if (taken != null) {
            b.setPiece(row, col, taken);
        }
    }

    /**
     * Represents this Move as a String
     * Chess accepted notation for castling is used
     * @return A String representation of this Move
     */
    public String toString() {
        if (piece instanceof King) {
            if ((getSq().equals(new Square(7,6)) ||
                    getSq().equals(new Square(0,6)))) {
                return "0-0";
            }
            if ((getSq().equals(new Square(7,2)) || 
                    getSq().equals(new Square(0,2)))) {
                return "0-0-0 ";
            }   
        }

        return piece.toString()+ getSq(); 
    }
}
