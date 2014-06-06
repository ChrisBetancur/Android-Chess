package com.kdoherty.engine;

import com.kdoherty.chess.Bishop;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Square;

/**
 * This class is responsible for evaluating a Bishop. It takes into account its
 * position on the Board and its position relative to other Pieces on the Board.
 * 
 * @author Kevin Doherty
 * 
 */
public final class BishopEval extends PieceEval {

	/** The starting value of a Bishop */
	public static int START_VALUE = 330;

	/**
	 * The penalty given to a Bishop if it is blocking a center pawn from moving
	 * from its home square
	 */
	private static int BLOCKING_CNTR_PAWN_PENALTY = -10;

	/**
	 * Bonuses and penalties added to a Rooks value based on its position on the
	 * Board
	 */
	private static int[] BOARD_VALUES = { 
		-20, -10, -10, -10, -10, -10, -10, -20,
		-10,   5,   0,   0,  0,   0,    5, -10,
		-10,  10,  10,  10,  10,  10,  10, -10,
		-10,   0,  10,  10,  10,  10,   0, -10,
		-10,   5,   5,  10,  10,   5,   5, -10,
		-10,   0,   5,  10,  10,   5,   0, -10,
		-10,   0,   0,   0,   0,   0,   0, -10,
		-20, -10, -10, -10, -10, -10, -10, -20
	};

	/**
	 * Constructor for BishopEval
	 * 
	 * @param board
	 *            The Board to evaluate the input Bishop on
	 * @param bishop
	 *            The Bishop to evaluate on the input Board
	 */
	public BishopEval(Board board, Bishop bishop) {
		super(board, bishop);
	}

	/**
	 * Evaluates this BishopEval's Bishop on its Board.
	 * 
	 * @return An integer rating of how good a Bishop is on a Board. The higher
	 *         the rating the better the placement.
	 */
	@Override
	public int evaluate() {
		Square s = piece.getSq();
		int index = color == Color.WHITE ? s.toNum() : 63 - s.toNum();

		int value = START_VALUE;
		value += BOARD_VALUES[index];

		if (isBlockingCenterPawn()) {
			value += BLOCKING_CNTR_PAWN_PENALTY;
		}

		return value;
	}

}