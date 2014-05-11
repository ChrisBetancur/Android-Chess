package com.kdoherty.androidchess.test;

import junit.framework.TestCase;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.King;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Rook;

public class BaseTest extends TestCase {

	private Pawn p1 = new Pawn(Color.WHITE);
//	private Pawn p2 = new Pawn(Color.WHITE);
//	private Pawn p3 = new Pawn(Color.WHITE);
//	private Pawn p4 = new Pawn(Color.WHITE);
//	private Pawn p5 = new Pawn(Color.WHITE);
//	private Pawn p6 = new Pawn(Color.WHITE);
//	private Pawn p7 = new Pawn(Color.WHITE);
//	private Pawn p8 = new Pawn(Color.WHITE);
	private Pawn p9 = new Pawn(Color.BLACK);
//	private Pawn p10 = new Pawn(Color.BLACK);
//	private Pawn p11 = new Pawn(Color.BLACK);
//	private Pawn p12 = new Pawn(Color.BLACK);
//	private Pawn p13 = new Pawn(Color.BLACK);
//	private Pawn p14 = new Pawn(Color.BLACK);
//	private Pawn p15 = new Pawn(Color.BLACK);
//	private Pawn p16 = new Pawn(Color.BLACK);

	private Rook r1 = new Rook(Color.WHITE);
	private Rook r2 = new Rook(Color.WHITE);
	private Rook r3 = new Rook(Color.BLACK);
	private Rook r4 = new Rook(Color.BLACK);

//	private Knight n1 = new Knight(Color.WHITE);
//	private Knight n2 = new Knight(Color.WHITE);
//	private Knight n3 = new Knight(Color.BLACK);
//	private Knight n4 = new Knight(Color.BLACK);
//
//	private Bishop b1 = new Bishop(Color.WHITE);
//	private Bishop b2 = new Bishop(Color.WHITE);
//	private Bishop b3 = new Bishop(Color.BLACK);
//	private Bishop b4 = new Bishop(Color.BLACK);
//
//	private Queen q1 = new Queen(Color.WHITE);
//	private Queen q2 = new Queen(Color.BLACK);

	private King k1 = new King(Color.WHITE);
	private King k2 = new King(Color.BLACK);

	/** Directions are from the Colors POV */
	protected enum Direction {
		RIGHT, LEFT;
	}

	protected Board castlingReadyBoard() {
		Board board = new Board();

		board.setPiece('e', 1, k1);
		board.setPiece('a', 1, r1);
		board.setPiece('h', 1, r2);

		board.setPiece('e', 8, k2);
		board.setPiece('a', 8, r3);
		board.setPiece('h', 8, r4);

		return board;
	}

	protected Board enPoissantReadyBoard(Color color, Direction dir) {
		return color == Color.WHITE ? whiteEnPoissantReadyBoard(dir)
				: blackEnPoissantReadyBoard(dir);
	}

	private Board whiteEnPoissantReadyBoard(Direction dir) {
		Board board = new Board();

		board.setPiece('e', 8, k2);
		board.setPiece('e', 1, k1);

		board.setPiece('e', 5, p1);

		if (dir == Direction.LEFT) {
			board.setPiece('d', 7, p9);
			p9.moveTo(board, 3, 3); // d7 - d5
		} else {
			board.setPiece('f', 7, p9);
			p9.moveTo(board, 3, 5); // f7 - f5
		}

		return board;
	}

	private Board blackEnPoissantReadyBoard(Direction dir) {
		Board board = new Board();

		board.setPiece('e', 8, k2);
		board.setPiece('e', 1, k1);

		board.setPiece('e', 4, p9);

		if (dir == Direction.LEFT) {
			board.setPiece('f', 2, p1);
			p1.moveTo(board, 4, 5); // f2 - f4
		} else {
			board.setPiece('d', 2, p1);
			p1.moveTo(board, 4, 3); // d2 - d4
		}

		return board;
	}
	
	protected Board pawnPromotionReadyBoard(Color color) {
		return color == Color.WHITE ? whitePawnPromotionReadyBoard()
				: blackPawnPromotionReadyBoard();
	}

	private Board whitePawnPromotionReadyBoard() {
		Board board = new Board();

		board.setPiece('e', 1, k1);
		board.setPiece('a', 7, p1);

		board.setPiece('e', 8, k2);

		return board;
	}

	private Board blackPawnPromotionReadyBoard() {
		Board board = new Board();

		board.setPiece('e', 1, k1);
		board.setPiece('a', 2, p9);

		board.setPiece('e', 8, k2);

		return board;
	}

}
