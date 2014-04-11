package com.kdoherty.chess;

import java.util.ArrayList;

/**
 * @author Kevin Doherty
 * @version 10/14/2013
 * 
 * This class represents a ChessBoard
 * using a 2D array of Pieces.
 * An empty Square on the Board is represented by a null Piece
 * Note that a square object is not taken in as arguments to methods
 * due to efficiency concerns despite being more readable
 * 
 * Static Methods:
 * 
 * isInbounds(int row, int col)                   -> boolean
 * sameDiag(int row, int col)                     -> boolean
 * isNeighbor(int row, int col, int row, int col) -> boolean
 * getBtwnSqs(int row, int col, int row, int col) -> ArrayList<Square>
 * getNeighbors(int row, int col)                 -> boolean
 * toSq(char col, int row)                        -> boolean
 * 
 * 
 * Dynamic Methods:
 * 
 * setEnPoissantSq                     -> void
 * isEmpty(int row, int col)           -> boolean
 * isOccupied(int row, int col)        -> boolean
 * getOccupant(int row, int col)       -> Piece
 * setPiece(int row, int col, Piece p) -> Piece
 * remove(int row, int col)            -> Piece
 * movePiece(int row, int col)         -> void
 * clearBoard()                        -> void
 * reset()                             -> void
 * getPieces(Color c)                  -> ArrayList<Piece>
 * getMoves(Color c)                   -> ArrayList<Move>
 * isAttacked(int row, int col)        -> boolean
 * findKing(Color c)                   -> King
 * kingInCheck(Color c)                -> boolean
 * isCheckMate(Color c)                -> boolean
 * isDraw(Color c)                     -> boolean
 * isGameOver(Color c)                 -> boolean
 * fillWithDefaultPieces()             -> void
 * 
 */
public class Board {

    /**
     * A board is represented as a 2D array of Pieces
     * An empty Square is represented by null
     */
    private Piece [][] pieces;

    /**
     *  dimensions of the chessBoard are
     *  8 rows by 8 columns
     */
    public final static int NUMROWS = 8;
    public final static int NUMCOLS = 8;

    /**
     * Keeps track of squares where a pawn can be captured by enPoissant
     */
    private Square enPoissantSq;

    /**
     * Constructor for Board
     * Initially sets all squares to null
     */
    public Board() {
        pieces = new Piece[NUMROWS][NUMCOLS];
        this.clearBoard();

    }

    /**
     * @param r row coordinate
     * @param c column coordinate
     * @return is coordinate (r, c) in a chess board?
     */
    public static boolean isInbounds(int r, int c) {
        return r < NUMROWS && r >= 0 && c >= 0 && c < NUMCOLS;
    }

    /**
     * Takes in two locations on the board and determines if they are diagonal
     * @param r row coordinate for first location
     * @param c column coordinate for first location
     * @param r2 row coordinate for second location
     * @param c2 column coordinate for second location
     * @return true if the two locations are diagonal
     */
    public static boolean sameDiagonal(int r, int c, int r2, int c2) {
        double colDif = c2 - c;
        if (colDif != 0) {
            return Math.abs((r2 - r) / colDif) == 1.0; 
        }
        return false;
    }

    /**
     * Takes in two locations on the board and determines
     * if they are next to each other
     * Not using getNeighbors for efficiency
     * because we want to stop looping as soon 
     * as we find the square in neighbors
     * @param r row coordinate for first location
     * @param c column coordinate for first location
     * @param r2 row coordinate for second location
     * @param c2 column coordinate for second location
     * @return true if the two locations are neighbors
     */
    public static boolean isNeighbor(int r, int c, int r2, int c2) { 
        for (int i = r2 - 1; i < r2 + 2; i++) { 
            for (int j = c2 - 1; j < c2 + 2; j++) { 
                if (!(i == r2 && j == c2) &&  (i == r && j == c))
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets all surrounding squares of the input location
     * @param r row coordinate
     * @param c column coordinate
     * @return all squares surrounding the input row, column
     */
    public static ArrayList<Square> getNeighbors(int r, int c) {
        ArrayList<Square> neighbors = new ArrayList<Square>();
        for (int i = r - 1; i < r + 2; i++) { 
            for (int j = c - 1; j < c + 2; j++) { 
                if (!(i == r && j == c) && isInbounds(i, j))
                    neighbors.add(new Square (i, j));
            }
        }
        return neighbors;
    }

    /**
     * If there are squares between the two input squares, they are returned.
     * If there are not null is returned.
     * @param s1 One square to find squares between
     * @param s2 The other square to find squares between
     * @return An <code>ArrayList</code> of all squares between the input squares
     */
    public static ArrayList<Square> getBtwnSqs(Square s1, Square s2) {
        if (s1.equals(s2)) {
            return null;
        }
        if (s1.getRow() == s2.getRow()) {
            return getBtwnSqsRow(s1, s2);
        }
        if (s1.getCol() == s2.getCol()) {
            return getBtwnSqsCol(s1, s2);
        }
        if (sameDiagonal(s1.getRow(), s1.getCol(), s2.getRow(), s2.getCol())) {
            return getBtwnSqsDiag(s1, s2);
        }
        return null;
    }


    /**
     * helper method for getBtwnSqs.
     * Only to be called on squares which are in the same row and are not the same row
     * find all squares between the 2 input squares
     * @param s1 One square in the row
     * @param s2 A different square in the same row
     * @return An <code>ArrayList</code> of all squares between the input squares
     */
    private static ArrayList<Square> getBtwnSqsRow(Square s1, Square s2) {
        ArrayList<Square> btwnSqs = new ArrayList<Square>();
        int row = s1.getRow();
        int smallerCol = Math.min(s1.getCol(), s2.getCol());
        int largerCol = Math.max(s1.getCol(), s2.getCol());
        for (int i = smallerCol + 1; i < largerCol; i++) {
            btwnSqs.add(new Square(row, i));
        }
        return btwnSqs;
    }

    /**
     * helper method for getBtwnSqs.
     * Only to be called on squares which are in the same column and are not the same column
     * find all squares between the 2 input squares
     * @param s1 One square in the column
     * @param s2 A different square in the same column
     * @return An <code>ArrayList</code> of all squares between the input squares
     */
    private static ArrayList<Square> getBtwnSqsCol(Square s1, Square s2) {
        ArrayList<Square> btwnSqs = new ArrayList<Square>();
        int col = s1.getCol();      
        int smallerRow = Math.min(s1.getRow(), s2.getRow());
        int largerRow = Math.max(s1.getRow(), s2.getRow());
        for (int i = largerRow - 1; i > smallerRow; i--) {
            btwnSqs.add(new Square(i, col));
        }
        return btwnSqs;
    }

    /**
     * helper method for getBtwnSqs.
     * Only to be called on squares which are in the same diagonal and are not the same diagonal
     * find all squares between the 2 input squares
     * @param s1 One square in the diagonal
     * @param s2 A different square in the same diagonal
     * @return An <code>ArrayList</code> of all squares between the input squares
     */
    private static ArrayList<Square> getBtwnSqsDiag(Square s1, Square s2) {
        ArrayList<Square> btwnSqs = new ArrayList<Square>();
        int rowDif = s1.getRow() - s2.getRow(); // 4 - 7 = -4
        Square startSq = rowDif < 0 ? s1 : s2; 
        Square endSq = rowDif < 0 ? s2 : s1;
        int rowIncr = 1;
        int colIncr = startSq.getCol() - endSq.getCol() > 0 ? -1 : 1; // -1
        int row = startSq.getRow();
        int col = startSq.getCol();
        int dif = Math.abs(rowDif);
        for (int i = 0; i < dif - 1; i++) {
            row += rowIncr;
            col += colIncr;
            btwnSqs.add(new Square(row, col));
        }
        return btwnSqs;
    }

    /**
     * Converts an input square in chess notation to a Square
     * @param c Letters a-h representing the columns on a chess board
     * @param r numbers 1-8 representing the rows on a chess board
     * @return The Square which the chess notation represents
     */
    public static Square toSq(char c, int r) {
        int col;
        if (r > 8 || r < 1) {
            throw new RuntimeException("invalid row input");
        }
        switch (c) {
        case 'a': col = 0; break;
        case 'b': col = 1; break;
        case 'c': col = 2; break;
        case 'd': col = 3; break;
        case 'e': col = 4; break;
        case 'f': col = 5; break;
        case 'g': col = 6; break;
        case 'h': col = 7; break;
        default: throw new RuntimeException("invalid column input");
        }
        if (!isInbounds(8 - r, col)) {
        }
        return new Square(8 - r, col);
    }

    /**
     * EFFECT:
     * Sets this Board's enPoissant Square to the input Square
     * @param s The Square to make an enPoissantSq
     */
    public void setEnPoissantSq(Square s) {
        this.enPoissantSq = s;
    }  

    /**
     * Gets this Board's enPoissant Square
     * @return The enPoissantSq of this Board
     */
    public Square getEnPoissantSq() {
        return this.enPoissantSq;
    }

    /**
     * Gets the Piece at the input coordinate
     * @param r The row coordinate
     * @param c The column coordinate
     * @return The Piece at the input coordinate if there is one
     * and null otherwise
     */
    public Piece getOccupant(int r, int c) {
        return this.pieces[r][c];
    }

    /**
     * Is the input coordinate empty?
     * @param r The row coordinate
     * @param c The column coordinate
     * @return true if the input coordinate is empty
     */
    public boolean isEmpty(int r, int c) {
        return this.getOccupant(r, c) == null;
    }

    /**
     * Does the input coordinate contain a Piece?
     * @param r The row coordinate 
     * @param c The column coordinate
     * @return true if the input coordinate contains a piece 
     */
    public boolean isOccupied(int r, int c) {
        return !isEmpty(r, c);
    }

    /**
     * Makes the input coordinate empty
     * @param r The row coordinate of where to remove
     * @param c The column coordinate of where to remove
     * @return The removed piece if there was one and 
     * otherwise null
     */
    public Piece remove(int r, int c) {
        Piece removed = this.getOccupant(r, c);
        this.pieces[r][c] = null;
        return removed;


    }

    /**
     * Sets the input piece to the input coordinate
     * @param r The row coordinate of where to set the piece
     * @param c The column coordinate of where to set the piece
     * @param p The piece to set at the input coordinate
     * @return The piece previously at this coordinate if there was one
     * otherwise null
     */
    public Piece setPiece(int r, int c, Piece p) {
        Piece removed = this.remove(r, c);
        this.pieces[r][c] = p;
        p.setRow(r);
        p.setCol(c);
        return removed;   
    }

    /**
     * Overloaded setPiece method to take in a square in chess square notation
     * @param c The column to set the piece at represented by a char 'a - h'
     * @param row The row to set the piece at, 1-8 with 8 being the 0 row in the 2D array
     * @param p The piece to take set at the input square
     * @return The Piece previously at the input square if there was one
     * otherwise null
     */
    public Piece setPiece(char c, int row, Piece p) {
        Square s = new Square(c, row);
        return this.setPiece(s.getRow(), s.getCol(), p);
    }

    /**
     * Moves a piece from the first input coordinate to the second input coordinate
     * @param r The row coordinate of the where we are moving the piece from
     * @param c The column coordinate of the where we are moving the piece from
     * @param r2 The row coordinate of the where we are moving the piece to
     * @param c2 The column coordinate of the where we are moving the piece to
     * @return The piece taken if there was one, if not it will return null.
     * If there was no piece at the first input square, an exception will be thrown.
     */
    public Piece movePiece(int r, int c, int r2, int c2) {
        if (this.isEmpty(r, c)) {
            throw new RuntimeException("No piece found at: " + new Square(r, c));
        }
        Piece p = this.getOccupant(r, c);
        this.remove(r, c);
        return this.setPiece(r2, c2, p);
    }

    /**
     * EFFECT:
     * Removes all pieces on the board if there were any
     */
    public void clearBoard() {
        for (int i = 0; i < NUMROWS; i++) {
            for (int j = 0; j < NUMCOLS; j++) {
                this.remove(i, j);
            }
        }
    }

    /**
     * EFFECT:
     * Clears the board and fill it with the
     * Pieces at their starting location
     */
    public void reset() {
        this.clearBoard();
        this.fillWithDefaultPieces();
    }

    /**
     * Gets all pieces of the input color on this board
     * @param color The color of the pieces we are trying to get
     * @return An ArrayList of all pieces of the input color
     */
    public ArrayList<Piece> getPieces(Color color) {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for (int i = 0; i < NUMROWS; i++) {
            for (int j = 0; j < NUMCOLS; j++) {
                Piece p = this.getOccupant(i, j);
                if (p != null && p.getColor() == color) {
                    pieces.add(p);
                }
            }
        }
        return pieces;
    }

    /**
     * gets all moves of all pieces of the input color
     * @param color the color of the pieces whose moves we will return
     * @return An arrayList of all moves pieces of the input color can make
     */
    public ArrayList<Move> getMoves(Color color) {
        ArrayList<Move> moves = new ArrayList<Move>();
        ArrayList<Move> pMoves = new ArrayList<Move>();
        for (Piece p : getPieces(color)) {
            pMoves = p.getMoves(this);
            if (pMoves != null && pMoves.size() != 0 && pMoves.get(0) != null) {
                moves.addAll(pMoves);
            }
        }
        return moves;
    }

    /**
     * Is the input square being attacked by a piece of the input color?
     * @param r The row coordinate of the square we are checking if it is attacked
     * @param c The column coordinate of the square we are checking if it is attacked
     * @param color The color of pieces we are checking if they attack the input square
     * @return true if the input square is attacked by a piece of the input color
     */
    public boolean isAttacked(int r, int c, Color color) {
        for (Piece p : this.getPieces(color)) {
            if (p.isAttacking(this, r, c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the king of the input color
     * @param color The color to find the king of
     * @return The King of the input color
     * throws a runtimeException if there is not a King of the input
     * color on this Board
     */
    public King findKing(Color color) {
        for (int i = 0; i < NUMROWS; i++) {
            for (int j = 0; j < NUMCOLS; j++) {
                Piece p = this.getOccupant(i, j);
                if (p != null && p.getColor() == color && p instanceof King) {
                    return (King)p;
                }
            }
        }
        throw new RuntimeException("The " + color + " King is not on the Board");
    }

    /**
     * Is the input color's King in Check?
     * @param color The Color of the king we want to find if its in check
     * @return true if the input Color's King is in check
     */
    public boolean kingInCheck(Color color) {
        return this.findKing(color).isInCheck(this);
    }

    /**
     * Is the input color in checkMate?
     * @param color The color to check if they are in checkMate
     * @return true if the input color is in checkMate
     */
    public boolean isCheckMate(Color color) {
        return this.kingInCheck(color) &&
                this.getMoves(color).size() == 0;
    }

    /**
     * Is it a draw because the input color can't move
     * @param color The color of the side to check if they are causing a draw
     * @return true if the input color is causing a draw
     */
    public boolean isDraw(Color color) {
        return !this.kingInCheck(color) &&
                this.getMoves(color).size() == 0;
    }

    /**
     * Is this game over?
     * A game is over if there is checkMate or a draw for either color
     * @return true if the game is over
     */
    public boolean isGameOver() {
        if (this.isCheckMate(Color.WHITE) || this.isCheckMate(Color.BLACK)) {
            return true;
        }
        if (this.isDraw(Color.WHITE) || this.isDraw(Color.BLACK)) {
            return true;
        }
        return false;
    }

    /**
     * EFFECT:
     * fills the board with the pieces in their starting position
     */
    public void fillWithDefaultPieces() {
        clearBoard();
        // adding white pieces
        setPiece('a', 1, new Rook  (Color.WHITE));
        setPiece('b', 1, new Knight(Color.WHITE));
        setPiece('c', 1, new Bishop(Color.WHITE));
        setPiece('d', 1, new Queen (Color.WHITE));
        setPiece('e', 1, new King  (Color.WHITE));
        setPiece('f', 1, new Bishop(Color.WHITE));
        setPiece('g', 1, new Knight(Color.WHITE));
        setPiece('h', 1, new Rook  (Color.WHITE));
        setPiece('a', 2, new Pawn  (Color.WHITE));
        setPiece('b', 2, new Pawn  (Color.WHITE));
        setPiece('c', 2, new Pawn  (Color.WHITE));
        setPiece('d', 2, new Pawn  (Color.WHITE));
        setPiece('e', 2, new Pawn  (Color.WHITE));
        setPiece('f', 2, new Pawn  (Color.WHITE));
        setPiece('g', 2, new Pawn  (Color.WHITE));
        setPiece('h', 2, new Pawn  (Color.WHITE));
        // adding black pieces
        setPiece('a', 8, new Rook  (Color.BLACK));
        setPiece('b', 8, new Knight(Color.BLACK));
        setPiece('c', 8, new Bishop(Color.BLACK));
        setPiece('d', 8, new Queen (Color.BLACK));
        setPiece('e', 8, new King  (Color.BLACK));
        setPiece('f', 8, new Bishop(Color.BLACK));
        setPiece('g', 8, new Knight(Color.BLACK));
        setPiece('h', 8, new Rook  (Color.BLACK));
        setPiece('a', 7, new Pawn  (Color.BLACK));
        setPiece('b', 7, new Pawn  (Color.BLACK));
        setPiece('c', 7, new Pawn  (Color.BLACK));
        setPiece('d', 7, new Pawn  (Color.BLACK));
        setPiece('e', 7, new Pawn  (Color.BLACK));
        setPiece('f', 7, new Pawn  (Color.BLACK));
        setPiece('g', 7, new Pawn  (Color.BLACK));
        setPiece('h', 7, new Pawn  (Color.BLACK));

    }
}
