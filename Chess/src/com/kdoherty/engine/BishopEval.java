package com.kdoherty.engine;

import com.kdoherty.chess.Bishop;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Square;

public class BishopEval {

	private static int START_VALUE = 330;
	private static int BLOCKING_CNTR_PAWN_PENALTY = -10;
	private static int[] BOARD_VALUES = { 
		-20, -10, -10, -10, -10, -10, -10, -20,
		-10,   5,   0,   0,  0,   0,    5, -10,
		-10,  10,  10,  10,  10,  10,  10, -10,
		-10,   0,  10,  10,  10,  10,   0, -10,
		-10,   5,   5,  10,  10,   5,   5, -10,
		-10,   0,   5,  10,  10,   5,   0, -10,
		-10,   0,   0,   0,   0,   0,   0, -10,
		-20, -10, -10, -10, -10, -10, -10, -20 };

	public static int eval(Board b, Bishop bi) {
		int value = START_VALUE;
		Square s = bi.getSq();
		int index = bi.getColor() == Color.WHITE ? s.toNum() : 63 - s
				.toNum();
		value += BOARD_VALUES[index];
		if (Evaluate.isBlockingCenterPawn(b, bi))
			value += BLOCKING_CNTR_PAWN_PENALTY;
		return value;
	}
}