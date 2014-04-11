package com.kdoherty.chess;


import java.util.ArrayList;



public class Pawn extends Piece {

    /** The starting row for pawns */
    private int homeRow;

    /** The forward direction for this Pawn */
    private int forward;

    /**
     * Constructor for a Pawn
     * Sets this Pawn's color
     * Initializes homeRow and forward based on the color
     * @param color The color to set this Pawn's color to
     */
    public Pawn(Color color) {
        super(color);
        homeRow = color == Color.WHITE ? 6 : 1;
        forward = color == Color.WHITE ? -1 : 1;
    }

    /**
     * Can this piece move on the input board to the input square?
     * @param b The Board the piece is checking if it can move on
     * @param r The row we are checking if the piece can move to
     * @param c The column we are checking if the piece can move to
     * @return true if the Piece can move to the input square and false otherwise
     */
    public boolean canMove(Board b, int r, int c) {
        return Board.isInbounds(r, c) &&
                (this.canMoveOne(b, r, c) ||
                        this.canMoveTwo(b, r, c) ||
                        this.canTakeNormally(b, r, c) ||
                        this.canEnPoissant(b, r, c)) &&
                        !this.stillInCheck(b, r, c);
    }

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
    public boolean isAttacking(Board b, int r, int c) {
        return this.row + forward == r && (this.col + 1 == c || this.col - 1 == c) 
                && (b.isEmpty(r, c) || this.isTaking(b, r, c));
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
    public boolean isDefending(Board b, int r, int c) {
        return this.row + forward == r && (this.col + 1 == c || this.col - 1 == c) 
                && !this.isTaking(b, r, c) && !this.stillInCheck(b, r, c);
    }

    /**
     * This will represent the piece as a String
     * If the piece is white a lowerCase letter will be used
     * If the piece is black an upperCase letter will be used
     * @return A String representation of this Piece
     */
    public String toString() {
        return this.color == Color.WHITE ? "p" : "P";
    }

    /**
     * Finds all moves this Piece can make on the input Board
     * @param b The Board on which we are getting all moves of this Piece
     * @return All moves this Piece can make on the input Board
     */
    public ArrayList<Move> getMoves(Board b) {
        int finalRow = this.color == Color.WHITE ? 0 : 7;
        ArrayList<Move> moves = new ArrayList<Move>();
        for (Square s : getPossibleSqs()) {
            if (this.canMove(b, s.getRow(), s.getCol())) {
                if (s.getRow() != finalRow) {
                    moves.add(new Move(this, s));
                }
                else {
                    moves.add(new Move(new Queen(this.color), s));
                    moves.add(new Move(new Knight(this.color), s)); 
                }
            }

        }
        return moves;
    }

    /**
     * Produces an ArrayList which contains all squares this Piece could
     * potentially move to
     * @return An ArrayList of Squares which this Piece could potentially move to
     */
    private ArrayList<Square> getPossibleSqs() {
        ArrayList<Square> posSqs = new ArrayList<Square>();
        posSqs.add(new Square(this.row + this.forward, this.col));
        posSqs.add(new Square(this.row + 2 * this.forward, this.col));
        posSqs.add(new Square(this.row + this.forward, this.col + 1));
        posSqs.add(new Square(this.row + this.forward, this.col - 1));
        return posSqs;
    }

    /**
     * Can this Pawn move one Square ahead to the input row/column
     * @param b The board we are checking if this Pawn can move one Square on
     * @param r The row we are checking if this Pawn canMove one space and be at
     * @param c The column we are checking if this Pawn canMove one space and be at
     * @return true if this Pawn canMove one space and be at the input row/column
     */
    private boolean canMoveOne(Board b, int r, int c) {
        return this.row + this.forward == r &&
                this.col == c &&
                b.isEmpty(this.row + this.forward, this.col); 
    }

    /**
     * Can this Pawn move two Square ahead to the input row/column
     * @param b The board we are checking if this Pawn can move two Square on
     * @param r The row we are checking if this Pawn canMove two spaces and be at
     * @param c The column we are checking if this Pawn canMove two spaces and be at
     * @return true if this Pawn canMove two spaces and be at the input row/column
     */
    private boolean canMoveTwo(Board b, int r, int c) {
        return this.row + 2 * this.forward == r && this.col == c &&
                b.isEmpty(this.row + this.forward, this.col) &&
                b.isEmpty(this.row + 2 * this.forward, this.col) &&
                this.row == this.homeRow;
    }

    /**
     * Can this pawn take enPoissant style at the input row/column.
     * @param b The Board we are checking if this Pawn can enPoissant on
     * @param r The row of the potential enPoissant Square
     * @param c The column of the potential enPoissant Square
     * @return true if this Pawn can enPoissant to the input row/column
     */
    private boolean canEnPoissant(Board b, int r, int c) {
        int enPoissantRow = this.color == Color.WHITE ? 3 : 4;
        return this.row == enPoissantRow && new Square(r, c).equals(b.getEnPoissantSq())
                && this.row + this.forward == r && (this.col + 1 == c || this.col - 1 == c);      
    }

    /**
     * Can this pawn take normally on the input row/column
     * @param b The Board we are checking if this Pawn can take on
     * @param r The row of the potential capture Square
     * @param c The column of the potential capture Square
     * @return true if this Pawn can take a piece on the input row/column
     */
    private boolean canTakeNormally(Board b, int r, int c) {
        return this.row + this.forward == r && (this.col + 1 == c || this.col - 1 == c)
                && this.isTaking(b, r, c);
    }

    /**
     * EFFECT:
     * If this Pawn can move to the input square, it is moved
     * Overridden move to method for Pawn to
     * account for situations like enPoissant, promotions, 
     */
    public void moveTo(Board b, int r, int c) {
        if (this.canMove(b, r, c)) {
            if (this.row + 2 * forward == r) {
                b.setEnPoissantSq(new Square(this.row + forward, this.col));
            }
            if (this.canEnPoissant(b, r, c) && this.row + forward == r && (this.col + 1 == c || this.col - 1 == c)) {
                b.movePiece(this.row, this.col, r, c);
                b.remove(r - forward, c);
            }
            else {
                b.movePiece(this.row, this.col, r, c);
            }
            if (this.isPromoting()) {
                //TODO
            }
        }
    }

    /**
     * Has this Pawn reached the final rank?
     * @return true if this Pawn has reached the final rank
     */
    private boolean isPromoting() {
        int finalRank = this.color == Color.WHITE ? 0 : 7;
        return this.row == finalRank;
    }

    /**
     * EFFECT:
     * Promotes this pawn to a Queen
     * @param b The Board to promote this Pawn to a Queen on
     */
    public void promoteToQueen(Board b) {
        b.remove(this.row, this.col);
        b.setPiece(this.row, this.col, new Queen(this.color));
    }

    /**
     * EFFECT:
     * Promotes this pawn to a Knight
     * @param b The Board to promote this Pawn to a Knight on
     */
    public void promoteToKnight(Board b) {
        b.remove(this.row, this.col);
        b.setPiece(this.row, this.col, new Knight(this.color));
    }
}
