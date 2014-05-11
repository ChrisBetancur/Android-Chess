package com.kdoherty.androidchess.test;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;
import com.kdoherty.chess.Piece;

public class MoveTest extends BaseTest {

	public void testMake() {
		Board board = Board.defaultBoard();
		Piece piece = board.getOccupant(6, 5); // f2 Pawn
		Move move = new Move(piece, 4, 5); // pf4
		move.make(board);
		Piece movedPiece = board.getOccupant(4, 5);

		assertNotNull(movedPiece);
		assertNull(board.getOccupant(6, 5));
		assertNull(move.getTaken());
	}

	public void testUnmakeNormal() {
		Board board = Board.defaultBoard();
		Piece piece = board.getOccupant(6, 5); // f2 Pawn
		Move move = new Move(piece, 4, 5); // pf4
		move.make(board);
		move.unmake(board);
		assertEquals(board, Board.defaultBoard());
	}

	public void testUnmakeCastlingWhiteShort() {
		Board board = castlingReadyBoard();
		Piece whiteKing = board.getOccupant(7, 4);
		Move whiteShortCastle = new Move(whiteKing, 7, 6, Move.Type.WHITE_SHORT);
		whiteShortCastle.make(board);
		whiteShortCastle.unmake(board);
		assertEquals(board, castlingReadyBoard());
	}

	public void testUnmakeCastlingWhiteLong() {
		Board board = castlingReadyBoard();
		Piece whiteKing = board.getOccupant(7, 4);
		Move whiteLongCastle = new Move(whiteKing, 7, 2, Move.Type.WHITE_LONG);
		whiteLongCastle.make(board);
		whiteLongCastle.unmake(board);
		assertEquals(board, castlingReadyBoard());
	}

	public void testUnmakeCastlingBlackShort() {
		Board board = castlingReadyBoard();
		Piece blackKing = board.getOccupant(0, 4);
		Move blackShortCastle = new Move(blackKing, 0, 6, Move.Type.BLACK_SHORT);
		blackShortCastle.make(board);
		blackShortCastle.unmake(board);
		assertEquals(board, castlingReadyBoard());
	}

	public void testUnmakeCastlingBlackLong() {
		Board board = castlingReadyBoard();
		Piece blackKing = board.getOccupant(0, 4);
		Move blackLongCastle = new Move(blackKing, 0, 2, Move.Type.BLACK_LONG);
		blackLongCastle.make(board);
		blackLongCastle.unmake(board);
		assertEquals(board, castlingReadyBoard());
	}

	public void testUnmakeEnPoissantWhiteLeft() {
		Board board = enPoissantReadyBoard(Color.WHITE, Direction.LEFT);
		Piece whitePawn = board.getOccupant(3, 4); // Get white pawn on e5
		Move whiteLeftEnPoissant = new Move(whitePawn, 2, 3,
				Move.Type.EN_POISSANT); // e5 - d6
		whiteLeftEnPoissant.make(board);
		whiteLeftEnPoissant.unmake(board);
		assertEquals(board, enPoissantReadyBoard(Color.WHITE, Direction.LEFT));
	}

	public void testUnmakeEnPoissantWhiteRight() {
		Board board = enPoissantReadyBoard(Color.WHITE, Direction.RIGHT);
		Piece whitePawn = board.getOccupant(3, 4); // Get white pawn on e5
		Move whiteRightEnPoissant = new Move(whitePawn, 2, 5,
				Move.Type.EN_POISSANT); // e5 - f6
		whiteRightEnPoissant.make(board);
		whiteRightEnPoissant.unmake(board);
		assertEquals(board, enPoissantReadyBoard(Color.WHITE, Direction.RIGHT));
	}

	public void testUnmakeEnPoissantBlackLeft() {
		Board board = enPoissantReadyBoard(Color.BLACK, Direction.LEFT);
		Piece blackPawn = board.getOccupant(4, 4); // Get black pawn on e4
		Move blackLeftEnPoissant = new Move(blackPawn, 5, 5,
				Move.Type.EN_POISSANT); // e4 - f3
		blackLeftEnPoissant.make(board);
		blackLeftEnPoissant.unmake(board);
		assertEquals(board, enPoissantReadyBoard(Color.BLACK, Direction.LEFT));
	}

	public void testUnmakeEnPoissantBlackRight() {
		Board board = enPoissantReadyBoard(Color.BLACK, Direction.RIGHT);
		Piece blackPawn = board.getOccupant(4, 4); // Get black pawn on e4
		Move blackRightEnPoissant = new Move(blackPawn, 5, 3,
				Move.Type.EN_POISSANT); // e4 - d3
		blackRightEnPoissant.make(board);
		blackRightEnPoissant.unmake(board);
		assertEquals(board, enPoissantReadyBoard(Color.BLACK, Direction.RIGHT));
	}

	public void testUnmakePawnPromotionWhite() {
		Board board = pawnPromotionReadyBoard(Color.WHITE);
		Piece whitePawn = board.getOccupant(1, 0);
		Move promotion = new Move(whitePawn, 0, 0, Move.Type.PAWN_PROMOTION);
		promotion.make(board);
		promotion.unmake(board);
		assertEquals(board, pawnPromotionReadyBoard(Color.WHITE));
	}
	
	public void testUnmakePawnPromotionBlack() {
		Board board = pawnPromotionReadyBoard(Color.BLACK);
		Piece whitePawn = board.getOccupant(6, 0);
		Move promotion = new Move(whitePawn, 7, 0, Move.Type.PAWN_PROMOTION);
		promotion.make(board);
		promotion.unmake(board);
		assertEquals(board, pawnPromotionReadyBoard(Color.BLACK));
	}

}
