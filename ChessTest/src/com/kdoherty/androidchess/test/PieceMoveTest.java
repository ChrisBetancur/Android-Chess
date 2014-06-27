package com.kdoherty.androidchess.test;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Piece;

public class PieceMoveTest extends BaseTest {
	
	public void testQueenMoves() {
		Board board = fillWithTestMate(5);
		Piece piece = board.getOccupant(5, 2);
		System.out.println(board);
		System.out.println(piece.getMoves(board));
	}

}
