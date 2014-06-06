package com.kdoherty.engine;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.King;
import com.kdoherty.chess.Square;

/**
 * This class is responsible for evaluating a King. It takes into account its
 * position on the Board, the phase of the game (middle / end) and its position
 * relative to other Pieces on the Board.
 * 
 * @author Kevin Doherty
 * 
 */
public final class KingEval extends PieceEval {

	/** Starting value assigned to a King */
	public static int START_VALUE = 10000;
	
	/**
	 * Bonuses and penalties added to a King's value during the middle game. In
	 * general, the King should stay out of the middle and stay in the corner
	 * areas of the board during a middle game.
	 */
	private static int[] MIDDLEGAME_BOARD_VALUES = {
		20,   30,  10,   0,   0,  10,  30,  20,
		20,   20,   0,   0,   0,   0,  20,  20,
		-10, -20, -20, -20, -20, -20, -20, -10,
		-20, -30, -30, -40, -40, -30, -30, -20,
		-30, -40, -40, -50, -50, -40, -40, -30,
		-30, -40, -40, -50, -50, -40, -40, -30,
		-30, -40, -40, -50, -50, -40, -40, -30,
		-30, -40, -40, -50, -50, -40, -40, -30
		};
	
	/**
	 * Bonuses and penalties added to a King's value during the end game. The
	 * King should get much more active during the end game and wants to get to
	 * a central Square.
	 */
	private static int[] ENDGAME_BOARD_VALUES = {
		-50, -30, -30, -30, -30, -30, -30, -50,
		-30, -30,   0,   0,   0,   0, -30, -30,
		-30, -10,  20,  30,  30,  20, -10, -30,
		-30, -10,  30,  40,  40,  30, -10, -30,
		-30, -10,  30,  40,  40,  30, -10, -30,
		-30, -10,  20,  30,  30,  20, -10, -30,
		-30, -20, -10,   0,   0, -10, -20, -30,
		-50, -40, -30, -20, -20, -30, -40, -50
	};

	/**
	 * Constructor for KingEval
	 * 
	 * @param board
	 *            The Board to evaluate the input King on
	 * @param king
	 *            The King to evaluate on the input Board
	 */
	public KingEval(Board board, King king) {
		super(board, king);
	}

	/**
	 * Evaluates this KingEval's King on its Board.
	 * 
	 * @return An integer rating of how good a King is on a Board. The higher
	 *         the rating the better the placement.
	 */
	@Override
	public int evaluate() {
		Square s = piece.getSq();
		int index = color == Color.WHITE ? s.toNum() : 63 - s.toNum();

		int value = START_VALUE;
		value += Evaluate.isEndGame(board) ? ENDGAME_BOARD_VALUES[index]
				: MIDDLEGAME_BOARD_VALUES[index];

		return value;
	}
}
