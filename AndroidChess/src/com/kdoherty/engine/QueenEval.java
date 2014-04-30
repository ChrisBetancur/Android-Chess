package com.kdoherty.engine;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Queen;
import com.kdoherty.chess.Square;

public class QueenEval {

	private static int START_VALUE = 1000;
	private static int[] BOARD_VALUES = { -20, -10, -10, -5, -5, -10, -10,
			-20, -10, 0, 5, 0, 0, 0, 0, -10, -10, 5, 5, 5, 5, 5, 0, -10, 0,
			0, 5, 5, 5, 5, 0, -5, -5, 0, 5, 5, 5, 5, 0, -5, -10, 0, 5, 5,
			5, 5, 0, -10, -10, 0, 0, 0, 0, 0, 0, -10, -20, -10, -10, -5,
			-5, -10, -10, -20 };

	public static int eval(Board b, Queen q) {
		int value = START_VALUE;
		Square s = q.getSq();
		int index = q.getColor() == Color.WHITE ? s.toNum() : 63 - s
				.toNum();
		value += BOARD_VALUES[index];
		return value;
	}
}
