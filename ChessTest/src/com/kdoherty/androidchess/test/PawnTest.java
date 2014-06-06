package com.kdoherty.androidchess.test;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Piece;
import com.kdoherty.chess.Square;

public class PawnTest extends BaseTest {

	public void testPawnGetMoves() {
		Board board = Board.defaultBoard();
		Piece pawn = board.getOccupant(6, 4); // e2 Pawn
		assertEquals(pawn.getMoves(board).toString(), "[pe3, pe4]");
	}

	public void testPawnGetMovesCaptures() {
		Board board = pawnCaptureBoard();
		Piece pawn = board.getOccupant(4, 4); // e4 pawn
		assertEquals(pawn.getMoves(board).toString(), "[pe5, pf5, pd5]");
	}

	public void testPawnMoveTwoWhite() {
		Board board = Board.defaultBoard();
		for (int i = 0; i < Board.NUM_COLS; i++) {
			Piece pawn = board.getOccupant(6, i);
			assertTrue(pawn.canMove(board, 4, i));
			Move move = new Move(board, pawn, 4, i);
			move.make();
			assertEquals(board.getEnPoissantSq(), new Square(5, i));
			assertEquals(pawn.getMoveCount(), 1);
			move.unmake();
		}
	}

	public void testPawnMoveTwoBlack() {
		Board board = Board.defaultBoard();
		for (int i = 0; i < Board.NUM_COLS; i++) {
			Piece pawn = board.getOccupant(1, i);
			assertTrue(pawn.canMove(board, 3, i));
			Move move = new Move(board, pawn, 3, i);
			move.make();
			assertEquals(board.getEnPoissantSq(), new Square(2, i));
			assertEquals(pawn.getMoveCount(), 1);
			move.unmake();
		}
	}

	public void testPawnMoveOneWhite() {
		Board board = Board.defaultBoard();
		board.setEnPoissantSq(new Square(5, 5));
		for (int i = 0; i < Board.NUM_COLS; i++) {
			Piece pawn = board.getOccupant(6, i);
			assertTrue(pawn.canMove(board, 5, i));
			Move move = new Move(board, pawn, 5, i);
			board.setEnPoissantSq(new Square(5, 5)); // Make sure it is set to
														// null after move is
														// made
			move.make();
			assertNull(board.getEnPoissantSq());
			assertEquals(pawn.getMoveCount(), 1);
			move.unmake();
		}
	}

	public void testPawnMoveOneBlack() {
		Board board = Board.defaultBoard();
		for (int i = 0; i < Board.NUM_COLS; i++) {
			Piece pawn = board.getOccupant(1, i);
			assertTrue(pawn.canMove(board, 2, i));
			Move move = new Move(board, pawn, 2, i);
			board.setEnPoissantSq(new Square(5, 5)); // Make sure it is set to
														// null after move is
														// made
			move.make();
			assertNull(board.getEnPoissantSq());
			assertEquals(pawn.getMoveCount(), 1);
			move.unmake();
		}
	}

	public void testWhitePawnNormalCaptureLeft() {
		Board board = pawnCaptureBoard();
		board.setEnPoissantSq(new Square(5, 5)); // Make sure it is set to null
													// after move is made
		Piece pawn = board.getOccupant(4, 4); // e4 pawn
		Move pxd5 = new Move(board, pawn, 3, 3); // d4 black pawn
		pxd5.make();

		assertNull(board.getEnPoissantSq());
		assertEquals(pawn.getMoveCount(), 1);
		assertNull(board.getOccupant(4, 4));
		assertNotNull(board.getOccupant(3, 3));
		assertTrue(board.getOccupant(3, 3).getColor() == Color.WHITE);

		Piece taken = pxd5.getTaken();
		assertNotNull(taken);
		assertTrue(taken instanceof Pawn && taken.getColor() == Color.BLACK);
	}

	public void testWhitePawnNormalCaptureRight() {
		Board board = pawnCaptureBoard();
		board.setEnPoissantSq(new Square(5, 5)); // Make sure it is set to null
													// after move is made
		Piece pawn = board.getOccupant(4, 4); // e4 pawn
		Move pxf5 = new Move(board, pawn, 3, 5); // xf5 black pawn
		pxf5.make();

		assertNull(board.getEnPoissantSq());
		assertEquals(pawn.getMoveCount(), 1);
		assertNull(board.getOccupant(4, 4));
		assertNotNull(board.getOccupant(3, 5));
		assertTrue(board.getOccupant(3, 5).getColor() == Color.WHITE);

		Piece taken = pxf5.getTaken();
		assertNotNull(taken);
		assertTrue(taken instanceof Pawn && taken.getColor() == Color.BLACK);
	}

	public void testBlackPawnNormalCaptureLeft() {
		Board board = pawnCaptureBoard();
		board.setEnPoissantSq(new Square(5, 5)); // Make sure it is set to null
													// after move is made
		Piece pawn = board.getOccupant(3, 3); // d5 pawn
		Move pxe4 = new Move(board, pawn, 4, 4); // xe4 white pawn
		pxe4.make();

		assertNull(board.getEnPoissantSq());
		assertEquals(pawn.getMoveCount(), 1);
		assertNull(board.getOccupant(3, 3));
		assertNotNull(board.getOccupant(4, 4));
		assertTrue(board.getOccupant(4, 4).getColor() == Color.BLACK);
		
		Piece taken = pxe4.getTaken();
		assertNotNull(taken);
		assertTrue(taken instanceof Pawn && taken.getColor() == Color.WHITE);
	}
	
	public void testBlackPawnNormalCaptureRight() {
		Board board = pawnCaptureBoard();
		board.setEnPoissantSq(new Square(5, 5)); // Make sure it is set to null
													// after move is made
		Piece pawn = board.getOccupant(3, 5); // f5 pawn
		Move pxe4 = new Move(board, pawn, 4, 4); // xe4 white pawn
		pxe4.make();

		assertNull(board.getEnPoissantSq());
		assertEquals(pawn.getMoveCount(), 1);
		assertNull(board.getOccupant(3, 5));
		assertNotNull(board.getOccupant(4, 4));
		assertTrue(board.getOccupant(4, 4).getColor() == Color.BLACK);
		
		Piece taken = pxe4.getTaken();
		assertNotNull(taken);
		assertTrue(taken instanceof Pawn && taken.getColor() == Color.WHITE);
	}
	
	public void testClone() {
		Pawn pawn = new Pawn(Color.WHITE);
		pawn.setRow(3);
		pawn.setCol(3);
		pawn.incrementMoveCount();
		Pawn clone = (Pawn) pawn.clone();
		assertFalse(pawn == clone);
		assertEquals(pawn, clone);
		pawn.setRow(4);
		assertFalse(pawn.equals(clone));
	}
	
	public void testToString() {
		Pawn whitePawn = new Pawn(Color.WHITE);
		assertEquals(whitePawn.toString(), "p");
		Pawn blackPawn = new Pawn(Color.BLACK);
		assertEquals(blackPawn.toString(), "P");
	}
}