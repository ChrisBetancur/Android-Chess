package com.kdoherty.chess;

/**
 * @author Kevin Doherty
 * @version 10/14/2013
 * A Square represents a coordinate on the chessBoard
 */
public class Square {

    /** Row coordinate of this square */
    private int row;

    /** Column coordinate of this square */
    private int col;

    /**
     * Constructor for Square
     * @param row Sets this squares row to this value
     * @param col Sets this squares column to this value
     */
    public Square(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * A user friendly constructor for Square using 
     * chess coordinate notation instead of 2D array notation
     * @param c The char representing the column
     * @param r The integer representing this row
     */
    public Square(char c, int r) {
        this.row = 8 - r;
        this.col = Board.toSq(c, r).getCol();
    }

    /**
     * Does this square equal that object?
     * A square is equal to an object if
     * 1. The object is a square
     * 2. They contain the same chess board coordinate
     * @return true if this square equals the input object
     */
    public boolean equals(Object obj) {
        if(obj instanceof Square)
            return ((Square)obj).getRow() == getRow() && ((Square)obj).getCol() == getCol();
        else 
            return false;
    }
    
    /**
     * Gets the hashCode for this Square
     * @return An integer representation of this Square
     */
    public int hashCode() {
        return this.row + this.col;
    }

    /**
     * getter for the row coordinate of this Square
     * @return The row coordinate of this Square
     */
    public int getRow() {
        return this.row; 
    }

    /**
     * getter for the column coordinate of this Square
     * @return The column coordinate of this Square
     */
    public int getCol() {
        return this.col;
    } 

    /**
     * Forms a String to represent this Square
     * @return A String representation of this Square
     */
    public String toString() {
        String s = "";
        switch(this.col) {
        case(0): s += "a"; break;
        case(1): s += "b"; break;
        case(2): s += "c"; break;
        case(3): s += "d"; break;
        case(4): s += "e"; break;
        case(5): s += "f"; break;
        case(6): s += "g"; break;
        case(7): s += "h"; break;
        }
        s += (8 - this.row);
        return s;
    }

}
