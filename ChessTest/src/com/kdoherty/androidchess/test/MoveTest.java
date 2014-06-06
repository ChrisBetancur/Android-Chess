package com.kdoherty.androidchess.test;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Knight;
import com.kdoherty.chess.Move;
import com.kdoherty.chess.Piece;
import com.kdoherty.chess.Queen;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Square;

public class MoveTest extends BaseTest {

	public void testMake() {
		Board board = Board.defaultBoard();
		Piece piece = board.getOccupant(6, 5); // f2 Pawn
		Move move = new Move(board, piece, 4, 5); // pf4
		move.make();
		Piece movedPiece = board.getOccupant(4, 5);

		assertNotNull(movedPiece);
		assertNull(board.getOccupant(6, 5));
		assertNull(move.getTaken());
		assertNotNull(board.getEnPoissantSq());
	}

	public void testPawnPromotionMakeUnmake() {
		Board board = pawnPromotionReadyBoard(Color.WHITE);
		for (Move move : board.getMoves(Color.WHITE)) {
			if (move.getType().isPromotion()) {
				
				Piece startingPiece = move.getPiece();
				int expectedStartingRow = startingPiece.getColor() == Color.WHITE ? 1 : 6;
				int startingRow = startingPiece.getRow();
				
				assertEquals(expectedStartingRow, startingRow);
				
				int startingCol = startingPiece.getCol();

				move.make();
				
				assertTrue(board.isEmpty(startingRow, startingCol));
				
				Piece expected = new Queen(move.getPiece().getColor(), move.getRow(), move.getCol());
				if (move.getType() == Move.Type.PROMOTION_KNIGHT) {
					expected = new Knight(move.getPiece().getColor(), move.getRow(), move.getCol());
				}
				
				assertEquals(expected, board.getOccupant(move.getRow(), move.getCol()));
				
				move.unmake();
				
				Piece putBack = board.getOccupant(startingRow, startingCol);
				assertTrue(putBack instanceof Pawn);
				assertTrue(putBack.equals(startingPiece));
				assertTrue(board.isEmpty(move.getRow(), move.getCol()));
				
				assertEquals(pawnPromotionReadyBoard(Color.WHITE), board);
			}
		}
	}

	public void testUnmakeNormal() {
		Board board = Board.defaultBoard();
		Piece piece = board.getOccupant(6, 5); // f2 Pawn
		Move move = new Move(board, piece, 4, 5); // pf4
		move.make();
		move.unmake();
		assertEquals(board, Board.defaultBoard());
	}

	public void testUnmakeCastlingWhiteShort() {
		Board board = castlingReadyBoard();
		Piece whiteKing = board.getOccupant(7, 4);
		Move whiteShortCastle = new Move(board, whiteKing, 7, 6,
				Move.Type.WHITE_SHORT);
		whiteShortCastle.make();
		whiteShortCastle.unmake();
		assertEquals(board, castlingReadyBoard());
	}

	public void testUnmakeCastlingWhiteLong() {
		Board board = castlingReadyBoard();
		Piece whiteKing = board.getOccupant(7, 4);
		Move whiteLongCastle = new Move(board, whiteKing, 7, 2,
				Move.Type.WHITE_LONG);
		whiteLongCastle.make();
		whiteLongCastle.unmake();
		assertEquals(board, castlingReadyBoard());
	}

	public void testUnmakeCastlingBlackShort() {
		Board board = castlingReadyBoard();
		Piece blackKing = board.getOccupant(0, 4);
		Move blackShortCastle = new Move(board, blackKing, 0, 6,
				Move.Type.BLACK_SHORT);
		blackShortCastle.make();
		blackShortCastle.unmake();
		assertEquals(board, castlingReadyBoard());
	}

	public void testUnmakeCastlingBlackLong() {
		Board board = castlingReadyBoard();
		Piece blackKing = board.getOccupant(0, 4);
		Move blackLongCastle = new Move(board, blackKing, 0, 2,
				Move.Type.BLACK_LONG);
		blackLongCastle.make();
		blackLongCastle.unmake();
		assertEquals(board, castlingReadyBoard());
	}

	public void testUnmakeEnPoissantWhiteLeft() {
		Board board = enPoissantReadyBoard(Color.WHITE, Direction.LEFT);
		Piece whitePawn = board.getOccupant(3, 4); // Get white pawn on e5
		Move whiteLeftEnPoissant = new Move(board, whitePawn, 2, 3,
				Move.Type.EN_POISSANT); // e5 - d6
		whiteLeftEnPoissant.make();
		whiteLeftEnPoissant.unmake();
		assertEquals(board, enPoissantReadyBoard(Color.WHITE, Direction.LEFT));
	}

	public void testUnmakeEnPoissantWhiteRight() {
		Board board = enPoissantReadyBoard(Color.WHITE, Direction.RIGHT);
		Piece whitePawn = board.getOccupant(3, 4); // Get white pawn on e5
		Move whiteRightEnPoissant = new Move(board, whitePawn, 2, 5,
				Move.Type.EN_POISSANT); // e5 - f6
		whiteRightEnPoissant.make();
		whiteRightEnPoissant.unmake();
		assertEquals(board, enPoissantReadyBoard(Color.WHITE, Direction.RIGHT));
	}

	public void testUnmakeEnPoissantBlackLeft() {
		Board board = enPoissantReadyBoard(Color.BLACK, Direction.LEFT);
		Piece blackPawn = board.getOccupant(4, 4); // Get black pawn on e4
		Move blackLeftEnPoissant = new Move(board, blackPawn, 5, 5,
				Move.Type.EN_POISSANT); // e4 - f3
		blackLeftEnPoissant.make();
		blackLeftEnPoissant.unmake();
		assertEquals(board, enPoissantReadyBoard(Color.BLACK, Direction.LEFT));
	}

	public void testUnmakeEnPoissantBlackRight() {
		Board board = enPoissantReadyBoard(Color.BLACK, Direction.RIGHT);
		Piece blackPawn = board.getOccupant(4, 4); // Get black pawn on e4
		Move blackRightEnPoissant = new Move(board, blackPawn, 5, 3,
				Move.Type.EN_POISSANT); // e4 - d3
		blackRightEnPoissant.make();
		blackRightEnPoissant.unmake();
		assertEquals(board, enPoissantReadyBoard(Color.BLACK, Direction.RIGHT));
	}

	public void testUnmakePawnPromotionQueenWhite() {
		Board board = pawnPromotionReadyBoard(Color.WHITE);
		Piece whitePawn = board.getOccupant(1, 0);
		Move promotion = new Move(board, whitePawn, 0, 0, Move.Type.PROMOTION_QUEEN);
		promotion.make();
		promotion.unmake();
		assertEquals(board, pawnPromotionReadyBoard(Color.WHITE));
	}
	
	public void testUnmakePawnPromotionKnightWhite() {
		Board board = pawnPromotionReadyBoard(Color.WHITE);
		Piece whitePawn = board.getOccupant(1, 0);
		Move promotion = new Move(board, whitePawn, 0, 0, Move.Type.PROMOTION_KNIGHT);
		promotion.make();
		promotion.unmake();
		assertEquals(board, pawnPromotionReadyBoard(Color.WHITE));
	}

	public void testUnmakePawnPromotionQueenBlack() {
		Board board = pawnPromotionReadyBoard(Color.BLACK);
		Piece whitePawn = board.getOccupant(6, 0);
		Move promotion = new Move(board, whitePawn, 7, 0, Move.Type.PROMOTION_QUEEN);
		promotion.make();
		promotion.unmake();
		assertEquals(board, pawnPromotionReadyBoard(Color.BLACK));
	}
	
	public void testUnmakePawnPromotionKnightBlack() {
		Board board = pawnPromotionReadyBoard(Color.BLACK);
		Piece whitePawn = board.getOccupant(6, 0);
		Move promotion = new Move(board, whitePawn, 7, 0, Move.Type.PROMOTION_KNIGHT);
		promotion.make();
		promotion.unmake();
		assertEquals(board, pawnPromotionReadyBoard(Color.BLACK));
	}

	public void testUnmakeBeforeMake() {
		Board board = Board.defaultBoard();
		Piece pawn = board.getOccupant(6, 4);
		Move e4 = new Move(board, pawn, new Square(4, 4));
		try {
			e4.unmake();
			fail("Cant unmake before making");
		} catch (Exception e) {
			// Should get here
		}
	}

	public void testMakeMake() {
		Board board = Board.defaultBoard();
		Piece pawn = board.getOccupant(6, 4);
		Move e4 = new Move(board, pawn, new Square(4, 4));
		try {
			e4.make();
			e4.make();
			fail("Cant make twice in a row");
		} catch (Exception e) {
			// Should get here
		}
	}

	public void testFindTypePawnPromotionWhite() {
		Board board = pawnPromotionReadyBoard(Color.WHITE);
		Piece whitePawn = board.getOccupant(1, 0);
		Move promotion = new Move(board, whitePawn, 0, 0);
		assertEquals(promotion.getType(), Move.Type.PROMOTION_QUEEN);
	}

	public void testFindTypePawnPromotionBlack() {
		Board board = pawnPromotionReadyBoard(Color.BLACK);
		Piece blackPawn = board.getOccupant(6, 0);
		Move promotion = new Move(board, blackPawn, 7, 0);
		assertEquals(promotion.getType(), Move.Type.PROMOTION_QUEEN);
	}

	public void testFindTypeEnPoissantWhiteRight() {
		Board board = enPoissantReadyBoard(Color.WHITE, Direction.RIGHT);
		Piece pawn = board.getOccupant(3, 4);
		Move enPoissant = new Move(board, pawn, 2, 5);
		assertEquals(enPoissant.getType(), Move.Type.EN_POISSANT);
	}

	public void testFindTypeEnPoissantWhiteLeft() {
		Board board = enPoissantReadyBoard(Color.WHITE, Direction.RIGHT);
		Piece whitePawn = board.getOccupant(3, 4); // Get white pawn on e5
		Move enPoissant = new Move(board, whitePawn, 2, 5); // e5 - f6
		assertEquals(enPoissant.getType(), Move.Type.EN_POISSANT);
	}

	public void testFindTypeEnPoissantBlackRight() {
		Board board = enPoissantReadyBoard(Color.BLACK, Direction.RIGHT);
		Piece blackPawn = board.getOccupant(4, 4); // Get black pawn on e4
		Move blackRightEnPoissant = new Move(board, blackPawn, 5, 3); // e4 - d3
		assertEquals(blackRightEnPoissant.getType(), Move.Type.EN_POISSANT);
	}

	public void testFindTypeEnPoissantBlackLeft() {
		Board board = enPoissantReadyBoard(Color.BLACK, Direction.LEFT);
		Piece blackPawn = board.getOccupant(4, 4); // Get black pawn on e4
		Move blackLeftEnPoissant = new Move(board, blackPawn, 5, 5); // e4 - f3
		assertEquals(blackLeftEnPoissant.getType(), Move.Type.EN_POISSANT);
	}

	public void testFindTypeWhiteShort() {
		Board board = castlingReadyBoard();
		Piece whiteKing = board.getOccupant(7, 4);
		Move whiteShortCastle = new Move(board, whiteKing, 7, 6);
		assertEquals(whiteShortCastle.getType(), Move.Type.WHITE_SHORT);
	}

	public void testFindTypeCastlingWhiteLong() {
		Board board = castlingReadyBoard();
		Piece whiteKing = board.getOccupant(7, 4);
		Move whiteLongCastle = new Move(board, whiteKing, 7, 2);
		assertEquals(whiteLongCastle.getType(), Move.Type.WHITE_LONG);
	}

	public void testFindTypeBlackShort() {
		Board board = castlingReadyBoard();
		Piece blackKing = board.getOccupant(0, 4);
		Move blackShortCastle = new Move(board, blackKing, 0, 6);
		assertEquals(blackShortCastle.getType(), Move.Type.BLACK_SHORT);
	}

	public void testFindTypeBlackLong() {
		Board board = castlingReadyBoard();
		Piece blackKing = board.getOccupant(0, 4);
		Move blackLongCastle = new Move(board, blackKing, 0, 2);
		assertEquals(blackLongCastle.getType(), Move.Type.BLACK_LONG);
	}

	public void testIsChecking() {
		Board board = fillWithTestMate(0);
		Piece rook = board.getOccupant(3, 2);
		Move move = new Move(board, rook, 0, 2);
		assertTrue(move.isChecking());
		move.make();
		assertTrue(move.isChecking());
	}

}
