package com.kdoherty.engine;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Knight;
import com.kdoherty.chess.Square;

/**
 * This class is responsible for evaluating a Knight. It takes into account its
 * position on the Board and its position relative to other Pieces on the Board.
 * 
 * @author Kevin Doherty
 * 
 */
public final class KnightEval extends PieceEval {

	/** The starting value assigned to a Knight */
	public static int START_VALUE = 325;

	/**
	 * Penalty given because knights get worse in end games because they are bad
	 * at stopping Pawns
	 */
	private static int ENDGAME_PENALTY = -10;

	/** Penalty given for blocking a central Pawn on its home squares */
	private static int BLOCKING_CNTR_PAWN_PENALTY = -10;

	/** Penalty given for moving too many times early in the game */
	private static int LOSS_TEMPO_PENALTY = -30;

	/**
	 * Bonuses and penalties added to a Knight's value based on its position on
	 * the Board
	 */
	private static int[] BOARD_VALUES = {
		  -50, -40, -30, -30, -30, -30, -40, -50,
		  -40, -20,   0,   5,   5,   0, -20, -40,
		  -30,   5,  10,  12,  12,  10,   5, -30,
		  -30,   0,  15,  15,  15,  15,   0, -30,
		  -30,   5,  15,  15,  15,  15,   5, -30,
		  -30,   0,  10,  12,  12,  10,   0, -30,
		  -40, -20,   0,   0,   0,   0, -20, -40,
		  -50, -40, -30, -30, -30, -30, -40, -50 };

	/**
	 * Constructor for KnightEval
	 * 
	 * @param board
	 *            The Board to evaluate the input Knight on
	 * @param knight
	 *            The Knight to evaluate on the input Board
	 */
	public KnightEval(Board board, Knight knight) {
		super(board, knight);
	}

	/**
	 * Evaluates this KnightEval's Knight on its Board.
	 * 
	 * @return An integer rating of how good a Knight is on a Board. The higher
	 *         the rating the better the placement.
	 */
	@Override
	public int evaluate() {

		Square s = piece.getSq();
		int index = color == Color.WHITE ? s.toNum() : 63 - s.toNum();

		int value = START_VALUE;
		value += BOARD_VALUES[index];

		if (Evaluate.isEndGame(board)) {
			value += ENDGAME_PENALTY;
		}
		if (isBlockingCenterPawn()) {
			value += BLOCKING_CNTR_PAWN_PENALTY;
		}
		if (piece.getMoveCount() > 1 && board.getMoveCount() < 10) {
			value += LOSS_TEMPO_PENALTY;
		}

		return value;
	}

}
