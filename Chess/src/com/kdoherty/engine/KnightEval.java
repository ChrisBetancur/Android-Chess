package com.kdoherty.engine;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Knight;
import com.kdoherty.chess.Square;

public class KnightEval {

	private static int START_VALUE = 325;
	private static int ENDGAME_PENALTY = -10;
	private static int BLOCKING_CNTR_PAWN_PENALTY = -10;
	private static int LOSS_TEMPO_PENALTY = -30;
	private static int[] BOARD_VALUES = { -50, -40, -30, -30, -30, -30, -40, -50,
										  -40, -20,   0,   5,   5,   0, -20, -40,
										  -30,   5,  10,  12,  12,  10,   5, -30,
										  -30,   0,  15,  15,  15,  15,   0, -30,
										  -30,   5,  15,  15,  15,  15,   5, -30,
										  -30,   0,  10,  12,  12,  10,   0, -30,
										  -40, -20,   0,   0,   0,   0, -20, -40,
										  -50, -40, -30, -30, -30, -30, -40, -50 };

	public static int eval(Board b, Knight n) {
		int value = START_VALUE;
		Square s = n.getSq();
		int index = n.getColor() == Color.WHITE ? s.toNum() : 63 - s.toNum();
		if (Evaluate.isEndGame(b))
			value += ENDGAME_PENALTY;
		value += BOARD_VALUES[index];
		if (PieceEval.isBlockingCenterPawn(b, n))
			value += BLOCKING_CNTR_PAWN_PENALTY;
		if (n.getMoveCount() > 1 && b.getMoveCount() < 10) {
			value += LOSS_TEMPO_PENALTY;
		}
		return value;
	}

}
