package com.kdoherty.androidchess.test;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.King;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Piece;
import com.kdoherty.chess.Queen;
import com.kdoherty.chess.Rook;
import com.kdoherty.chess.Square;

public class BoardTest extends BaseTest {

	// 0 1 2 3 4 5 6 7
	// 0| | | | | | | | |
	// 1| | | | | | | | |
	// 2| | | | | | | | |
	// 3| | | | | | | | |
	// 4| | | | | | | | |
	// 5| | | | | | | | |
	// 6| | | | | | | | |
	// 7| | | | | | | | |

	public void testIsInbounds() {
		assertFalse(Board.isInbounds(8, 5));
		assertFalse(Board.isInbounds(8, 6));
		assertFalse(Board.isInbounds(8, -1));
		assertFalse(Board.isInbounds(-1, 4));
		assertFalse(Board.isInbounds(-1, 2));
		assertFalse(Board.isInbounds(-1, 8));
		assertTrue(Board.isInbounds(4, 4));

		// Testing all corner cases
		assertTrue(Board.isInbounds(0, 0));
		assertTrue(Board.isInbounds(0, 7));
		assertTrue(Board.isInbounds(7, 0));
		assertTrue(Board.isInbounds(7, 0));
	}

	public void testSameDiagonal() {
		assertTrue(Board.sameDiagonal(0, 0, 7, 7));
		assertTrue(Board.sameDiagonal(0, 7, 7, 0));
		assertTrue(Board.sameDiagonal(7, 3, 5, 1));
		assertTrue(Board.sameDiagonal(5, 5, 3, 7));
		assertTrue(Board.sameDiagonal(5, 5, 3, 3));
		assertTrue(Board.sameDiagonal(5, 5, 7, 3));
		assertTrue(Board.sameDiagonal(5, 5, 7, 7));
		assertFalse(Board.sameDiagonal(4, 6, 5, 2));
		assertFalse(Board.sameDiagonal(2, 1, 1, 3));
		assertFalse(Board.sameDiagonal(5, 5, 3, 6));
		assertFalse(Board.sameDiagonal(5, 5, 3, 4));
		assertFalse(Board.sameDiagonal(5, 5, 7, 2));
		assertFalse(Board.sameDiagonal(5, 5, 7, 6));
	}

	public void testIsNeighbor() {
		assertTrue(Board.isNeighbor(5, 5, 6, 4));
		assertTrue(Board.isNeighbor(5, 5, 6, 5));
		assertTrue(Board.isNeighbor(5, 5, 6, 6));

		assertTrue(Board.isNeighbor(5, 5, 4, 4));
		assertTrue(Board.isNeighbor(5, 5, 4, 5));
		assertTrue(Board.isNeighbor(5, 5, 4, 6));

		assertTrue(Board.isNeighbor(5, 5, 5, 4));
		assertTrue(Board.isNeighbor(5, 5, 5, 6));

		assertFalse(Board.isNeighbor(5, 5, 6, 7));
		assertFalse(Board.isNeighbor(5, 5, 4, 3));

	}

	public void testGetBtwnSqs() {
		assertEquals(Board.getBtwnSqs(new Square('a', 1), new Square('a', 8))
				.toString(), "[a2, a3, a4, a5, a6, a7]");
		assertEquals(Board.getBtwnSqs(new Square('a', 1), new Square('h', 8))
				.toString(), "[g7, f6, e5, d4, c3, b2]");
		assertEquals(Board.getBtwnSqs(new Square('a', 1), new Square('h', 1))
				.toString(), "[b1, c1, d1, e1, f1, g1]");
		assertEquals(Board.getBtwnSqs(new Square('a', 8), new Square('h', 1))
				.toString(), "[b7, c6, d5, e4, f3, g2]");

		try {
			Board.getBtwnSqs(new Square('a', 1), new Square('a', 1));
			fail("Should not be able to call getBtwnSqs on the same Square");
		} catch (IllegalArgumentException e) {
			// Test does not fail
		}
		try {
			Board.getBtwnSqs(new Square('a', 1), new Square('d', 3));
			fail("Should not be able to call getBtwnSqs on Squares that are not inline with eachother");
		} catch (IllegalArgumentException e) {
			// Test does not fail
		}
	}

	public void testGetOccupant() {
		Board board = Board.defaultBoard();
		assertNull(board.getOccupant(4, 4));
		assertTrue(board.getOccupant(0, 0) instanceof Rook);
		assertTrue(board.getOccupant(0, 0).getColor() == Color.BLACK);
	}

	public void testIsEmpty() {
		Board board = Board.defaultBoard();
		assertTrue(board.isEmpty(3, 4));
		assertFalse(board.isEmpty(1, 1));
	}

	public void testIsOccupied() {
		Board board = Board.defaultBoard();
		assertTrue(board.isOccupied(1, 1));
		assertFalse(board.isOccupied(3, 4));
	}

	public void testRemove() {
		Board board = Board.defaultBoard();
		Piece removed = board.remove(0, 7);
		assertTrue(removed instanceof Rook);
		Piece postRemoved = board.getOccupant(0, 7);
		assertNull(postRemoved);
	}

	public void testSetPiece() {
		Board board = new Board();
		board.setPiece('a', 2, new Pawn(Color.WHITE));
		Piece piece = board.getOccupant(6, 0);
		assertNotNull(piece);
		assertTrue(piece instanceof Pawn && piece.getColor() == Color.WHITE
				&& piece.getRow() == 6 && piece.getCol() == 0);
	}

	public void testMovePiece() {
		Board board = Board.defaultBoard();
		board.movePiece(6, 4, 4, 4); // pe4
		assertNull(board.getOccupant(6, 4));
		Piece piece = board.getOccupant(4, 4);
		assertTrue(piece instanceof Pawn && piece.getColor() == Color.WHITE
				&& piece.getRow() == 4 && piece.getCol() == 4);
	}

	public void testClearBoard() {
		Board board = Board.defaultBoard();
		board.clearBoard();
		for (int i = 0; i < Board.NUM_ROWS; i++) {
			for (int j = 0; j < Board.NUM_COLS; j++) {
				assertNull(board.getOccupant(i, j));
			}
		}
	}

	public void testGetPieces() {
		Board board = Board.defaultBoard();
		assertEquals(board.getPieces(Color.WHITE).toString(),
				"[r, n, b, q, k, b, n, r, p, p, p, p, p, p, p, p]");
	}

	public void testGetMoves() {
		Board board = Board.defaultBoard();
		assertEquals(
				board.getMoves(Color.WHITE).toString(),
				"[nc3, na3, nh3, nf3, pa3, pa4, pb3, pb4, pc3, pc4, pd3, pd4, pe3, pe4, pf3, pf4, pg3, pg4, ph3, ph4]");
	}

	public void testIsAttacked() {
		Board board = new Board();
		board.setPiece(4, 4, new Queen(Color.WHITE));
		assertTrue(board.isAttacked(3, 4, Color.WHITE));
		assertFalse(board.isAttacked(3, 2, Color.WHITE));
	}

	public void testFindKing() {
		Board board = Board.defaultBoard();
		King whiteKing = board.findKing(Color.WHITE);
		assertNotNull(whiteKing);
		assertTrue(whiteKing.getRow() == 7 && whiteKing.getCol() == 4);

		King blackKing = board.findKing(Color.BLACK);
		assertNotNull(blackKing);
		assertTrue(blackKing.getRow() == 0 && blackKing.getCol() == 4);

		Board emptyBoard = new Board();
		try {
			emptyBoard.findKing(Color.WHITE);
			fail("Should fail because there is no white King on the Board");
		} catch (IllegalStateException e) {
			// Test passes
		}
	}

	public void testKingInCheck() {
		Board board = kingInCheckBoard();
		assertTrue(board.kingInCheck(Color.BLACK));
		assertFalse(board.kingInCheck(Color.WHITE));
	}

	public void testIsCheckmate() {
		Board board = checkmateBoard();
		assertTrue(board.isCheckMate(Color.BLACK));
		assertFalse(board.isCheckMate(Color.WHITE));
	}

	public void testIsDraw() {
		Board board = drawBoard();
		assertTrue(board.isDraw(Color.BLACK));
		board = Board.defaultBoard();
		assertFalse(board.isDraw(Color.BLACK));
	}
	
	public void testClone() {
		Board castleBoard = castlingReadyBoard();
		assertEquals(castleBoard, castleBoard.clone());
		Board checkMateBoard = checkmateBoard();
		assertEquals(checkMateBoard, checkMateBoard.clone());
		Board drawBoard = drawBoard();
		assertEquals(drawBoard, drawBoard.clone());
		Board kingInCheckBoard = kingInCheckBoard();
		assertEquals(kingInCheckBoard, kingInCheckBoard.clone());
		assertFalse(kingInCheckBoard == kingInCheckBoard.clone());
		System.out.println(Board.defaultBoard());
	}
}