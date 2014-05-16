package com.kdoherty.engine;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.King;
import com.kdoherty.chess.Square;

public class KingEval {

	private static int START_VALUE = 10000;
	
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

	public static int eval(Board b, King k) {
		int value = START_VALUE;
		Square s = k.getSq();
		int index = k.getColor() == Color.WHITE ? s.toNum() : 63 - s
				.toNum();
		value += Evaluate.isEndGame(b) ? ENDGAME_BOARD_VALUES[index]
				: MIDDLEGAME_BOARD_VALUES[index];
		return value;
	}
}
