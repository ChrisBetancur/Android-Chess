package com.kdoherty.androidchess.test;

import junit.framework.TestCase;

import com.kdoherty.chess.Bishop;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.King;
import com.kdoherty.chess.Knight;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Queen;
import com.kdoherty.chess.Rook;

public class BaseTest extends TestCase {

	private Pawn p1 = new Pawn(Color.WHITE);
	private Pawn p2 = new Pawn(Color.WHITE);
	private Pawn p3 = new Pawn(Color.WHITE);
	private Pawn p4 = new Pawn(Color.WHITE);
	private Pawn p5 = new Pawn(Color.WHITE);
	private Pawn p6 = new Pawn(Color.WHITE);
//	private Pawn p7 = new Pawn(Color.WHITE);
//	private Pawn p8 = new Pawn(Color.WHITE);
	private Pawn p9 = new Pawn(Color.BLACK);
	private Pawn p10 = new Pawn(Color.BLACK);
	private Pawn p11 = new Pawn(Color.BLACK);
	private Pawn p12 = new Pawn(Color.BLACK);
	private Pawn p13 = new Pawn(Color.BLACK);
	private Pawn p14 = new Pawn(Color.BLACK);
//	private Pawn p15 = new Pawn(Color.BLACK);
//	private Pawn p16 = new Pawn(Color.BLACK);

	private Rook r1 = new Rook(Color.WHITE);
	private Rook r2 = new Rook(Color.WHITE);
	private Rook r3 = new Rook(Color.BLACK);
	private Rook r4 = new Rook(Color.BLACK);

	private Knight n1 = new Knight(Color.WHITE);
	private Knight n2 = new Knight(Color.WHITE);
	private Knight n3 = new Knight(Color.BLACK);
	private Knight n4 = new Knight(Color.BLACK);

	private Bishop b1 = new Bishop(Color.WHITE);
//	private Bishop b2 = new Bishop(Color.WHITE);
	private Bishop b3 = new Bishop(Color.BLACK);
	private Bishop b4 = new Bishop(Color.BLACK);

	private Queen q1 = new Queen(Color.WHITE);
	private Queen q2 = new Queen(Color.BLACK);

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

	protected Board fillWithTestMate(int x) {
		Board board = new Board();

		switch (x) {

		case (0): {
			board.setPiece('h', 8, k2);
			board.setPiece('c', 5, r1);
			board.setPiece('d', 6, r2);
			board.setPiece('g', 1, k1);
			break;
		}

		case (1): {
			board.setPiece('a', 8, r3);
			board.setPiece('a', 5, q2);
			board.setPiece('g', 8, r4);
			board.setPiece('h', 7, k2);
			board.setPiece('g', 4, r1);
			board.setPiece('f', 5, n1);
			board.setPiece('b', 2, k1);
			board.setPiece('b', 3, p1);
			board.setPiece('c', 3, r2);
			break;
		}
		case (2): {
			board.setPiece('a', 8, r3);
			board.setPiece('a', 5, q2);
			board.setPiece('g', 8, r4);
			board.setPiece('h', 7, p9);
			board.setPiece('h', 8, k2);
			board.setPiece('h', 6, q1);
			board.setPiece('g', 4, r1);
			board.setPiece('f', 5, n1);
			board.setPiece('b', 2, k1);
			board.setPiece('b', 3, p1);
			board.setPiece('c', 3, r2);
			// b.setPiece('h', 7, k2);
			break;
		}
		case (3): {
			board.setPiece('h', 8, k2);
			board.setPiece('f', 8, k1);
			board.setPiece('g', 8, b3);
			board.setPiece('g', 7, p9);
			board.setPiece('h', 7, p10);
			board.setPiece('h', 1, r1);
			board.setPiece('g', 6, p1);
			break;
		}

		case (4): {
			board.setPiece('e', 8, k1);
			board.setPiece('e', 6, k2);
			board.setPiece('f', 4, q1);
			board.setPiece('c', 7, r1);
			board.setPiece('d', 4, p1);
			board.setPiece('c', 4, b1);
			board.setPiece('b', 5, n1);
			board.setPiece('a', 7, r3);
			board.setPiece('h', 7, r4);
			board.setPiece('a', 8, b3);
			board.setPiece('h', 8, b4);
			board.setPiece('f', 7, p9);
			board.setPiece('d', 5, n3);
			break;
		}
		case (5): {
			board.setPiece('a', 8, q2);
			board.setPiece('a', 7, p9);
			board.setPiece('f', 7, p10);
			board.setPiece('g', 7, p11);
			board.setPiece('b', 2, p1);
			board.setPiece('f', 2, p2);
			board.setPiece('g', 3, p3);
			board.setPiece('c', 3, q1);
			board.setPiece('d', 6, r1);
			board.setPiece('f', 8, r3);
			board.setPiece('e', 6, n3);
			board.setPiece('f', 5, n1);
			board.setPiece('f', 1, k1);
			board.setPiece('h', 7, k2);
			break;
		}
		case (6): {
			board.setPiece('a', 7, r3);
			board.setPiece('a', 6, p9);
			board.setPiece('a', 2, p1);
			board.setPiece('b', 8, n3);
			board.setPiece('b', 5, p10);
			board.setPiece('c', 3, p2);
			board.setPiece('d', 7, r4);
			board.setPiece('e', 6, q1);
			board.setPiece('e', 1, r1);
			board.setPiece('f', 8, q2);
			board.setPiece('f', 2, p3);
			board.setPiece('g', 3, r2);
			board.setPiece('g', 2, p4);
			board.setPiece('g', 1, k1);
			board.setPiece('h', 8, k2);
			board.setPiece('h', 7, p11);
			board.setPiece('h', 2, p5);
			break;
		}
		case (7): {
			board.setPiece('a', 2, p1);
			board.setPiece('b', 2, p2);
			board.setPiece('c', 2, p3);
			board.setPiece('e', 2, k1);
			board.setPiece('g', 2, b1);
			board.setPiece('e', 3, n1);
			board.setPiece('f', 1, r1);
			board.setPiece('f', 7, q1);
			board.setPiece('h', 2, q2);
			board.setPiece('h', 4, r3);
			board.setPiece('h', 6, k2);
			board.setPiece('a', 7, p11);
			board.setPiece('b', 7, p12);
			board.setPiece('g', 7, p13);
			board.setPiece('d', 4, p4);
			break;
		}
		case (8): {
			board.setPiece('a', 3, p5);
			board.setPiece('a', 5, p12);
			board.setPiece('a', 6, n4);
			board.setPiece('b', 1, k1);
			board.setPiece('b', 2, p3);
			board.setPiece('b', 5, k2);
			board.setPiece('b', 6, b3);
			board.setPiece('d', 1, b1);
			board.setPiece('d', 4, p6);
			board.setPiece('d', 5, p11);
			board.setPiece('e', 2, n2);
			board.setPiece('e', 6, p10);
			board.setPiece('e', 8, r4);
			board.setPiece('f', 1, n1);
			board.setPiece('f', 6, r3);
			board.setPiece('g', 2, p2);
			board.setPiece('g', 6, r1);
			board.setPiece('h', 5, p1);
			board.setPiece('h', 6, p9);
			board.setPiece('h', 7, q2);
			break;
		}
		case (9): {
			board.setPiece('a', 2, p1);
			board.setPiece('b', 2, p2);
			board.setPiece('c', 2, p3);
			board.setPiece('f', 2, p4);
			board.setPiece('g', 2, p5);
			board.setPiece('h', 2, p6);
			board.setPiece('a', 7, p9);
			board.setPiece('b', 7, p10);
			board.setPiece('c', 7, p11);
			board.setPiece('g', 7, p12);
			board.setPiece('h', 7, p13);
			board.setPiece('a', 1, r1);
			board.setPiece('g', 1, k1);
			board.setPiece('e', 1, r2);
			board.setPiece('g', 5, n1);
			board.setPiece('a', 8, r3);
			board.setPiece('e', 8, r4);
			board.setPiece('e', 2, q1);
			board.setPiece('d', 8, q2);
			board.setPiece('f', 5, n3);
			board.setPiece('g', 8, k2);
			board.setPiece('e', 4, p14);
			break;
		}
		}
		return board;
	}
	
	public Board kingInCheckBoard() {
		Board board = new Board();
		board.setPiece('e', 1, k1);
		board.setPiece('e', 8, k2);
		board.setPiece('a', 8, q1);
		return board;
	}
	
	public Board checkmateBoard() {
		Board board = kingInCheckBoard();
		board.setPiece('b', 7, r1);
		return board;
	}
	
	public Board drawBoard() {
		Board board = new Board();
		board.setPiece('a', 8, k2);
		board.setPiece('b', 6, q1);
		board.setPiece('c', 8, k1);
		return board;
	}

}
