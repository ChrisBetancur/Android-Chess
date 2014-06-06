package com.kdoherty.engine;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Queen;
import com.kdoherty.chess.Square;

/**
 * This class is responsible for evaluating a Queen. It takes into account its
 * position on the Board and its position relative to other Pieces on the Board.
 * 
 * @author Kevin Doherty
 * 
 */
public final class QueenEval extends PieceEval {

	/** Starting value assigned to a Queen */
	public static int START_VALUE = 1000;
	
	/** Penalty given for moving to early in the game. This can cause loss of tempo if opposing Pieces attack the Queen */
	private static int QUEEN_OUT_EARLY_PENALTY = -15;
	
	/**
	 * Bonuses and penalties added to a Rooks value based on its position on the
	 * Board
	 */
	private static int[] BOARD_VALUES = {
		-20, -10, -10, -5, -5, -10, -10, -20,
		-10,   0,   5,  0,  0,   0,   0, -10,
		-10,   5,   5,  5,  5,   5,   0, -10,
		  0,   0,   5,  5,  5,   5,   0,  -5,
		 -5,   0,   5,  5,  5,   5,   0,  -5,
		-10,   0,   5,  5,  5,   5,   0, -10,
		-10,   0,   0,  0,  0,   0,   0, -10,
		-20, -10, -10, -5, -5, -10, -10, -20
	};

	/**
	 * Constructor for QueenEval
	 * @param board The Board to evaluate the input Queen on
	 * @param queen The Queen to evaluate on the input Board
	 */
	public QueenEval(Board board, Queen queen) {
		super(board, queen);
	}

	/**
	 * Evaluates this QueenEval's Queen on its Board.
	 * 
	 * @return An integer rating of how good a Queen is on a Board. The higher
	 *         the rating the better the placement.
	 */
	@Override
	public int evaluate() {
		Square s = piece.getSq();
		int index = piece.getColor() == Color.WHITE ? s.toNum() : 63 - s
				.toNum();
		
		int value = START_VALUE;
		value += BOARD_VALUES[index];
		
		if (piece.getMoveCount() != 0 && board.getMoveCount() < 6) {
			value += QUEEN_OUT_EARLY_PENALTY;
		}
		
		return value;
	}
}
