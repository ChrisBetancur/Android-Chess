package com.kdoherty.engine;

import java.util.List;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;

/**
 * This class represents a Computer player and its though process.
 * 
 * @author Kevin Doherty
 */
public final class CpuPlayer {

	/** The Color which this computer CpuPlayer will make moves for */
	private Color color;

	/** The number of moves this CpuPlayer will think ahead */
	private int depth;

	/**
	 * Constructor for CpuPlayer.
	 * 
	 * @param color
	 *            The Color which this computer CpuPlayer will make moves for
	 * @param depth
	 *            The number of moves this CpuPlayer will think ahead
	 */
	public CpuPlayer(Color color, int depth) {
		this.color = color;
		this.depth = depth;
	}

	/**
	 * Determines the "best" move on the input Board. It starts by looking for a
	 * forced checkmate in the position at a depth varying depending on how
	 * close our Queen is to the other player's King. If it does not find
	 * anything, it will try each move on the Board, and then evaluate it at
	 * this CpuPlayer's depth. This assumes that the opponent makes the best
	 * possible reply to each Move. Whichever Move has the worst best reply is
	 * chosen.
	 * 
	 * 
	 * @param board
	 *            The Board determine the best move on
	 * @return Move What was determined to be the best move.
	 */
	public Move negaMaxMove(Board board) {
		if (board.getMoveCount() == 1) {
			Move lastMove = board.getLastMove();
			if (lastMove.toString().equals("pe4")) {
				return new Move(board, board.getOccupant(1, 4), 3, 4,
						Move.Type.NORMAL);
			} else if (lastMove.toString().equals("pd4")) {
				return new Move(board, board.getOccupant(1, 3), 3, 3,
						Move.Type.NORMAL);
			}
		}

		int mateDepth = Evaluate.queenCloseToKing(board, color) ? 3 : 2;
		List<Move> mateMoves = MateSolver
				.findMateUpToN(board, color, mateDepth);
		if (!mateMoves.isEmpty()) {
			return mateMoves.get(0);
		}

		int max = Integer.MIN_VALUE;
		Move bestMove = null;

		for (Move move : MoveSorter.sort(board, board.getMoves(color))) {
			move.make();
			int score = -negaMaxWithPruning(board, color.opp(),
					Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
			if (score > max) {
				max = score;
				bestMove = move;
			}
			move.unmake();
		}

		return bestMove;
	}

	/**
	 * Determines the maximum rating obtained by playing the best move in the
	 * position
	 * 
	 * @param board
	 *            The Board to determine the maximum rating of the best move on
	 * @param color
	 *            The Color to find the maximum rating for.
	 * @param alpha
	 *            Keeps track of the maximum evaluation
	 * @param beta
	 *            Keeps track of the minimum evaluation
	 * @param moveDepth
	 *            The number of moves to look ahead
	 * @return The maximum rating obtained by playing the best move in the
	 *         position.
	 */
	int negaMaxWithPruning(Board board, Color color, int alpha, int beta,
			int moveDepth) {
		if (moveDepth == 0) {
			return Evaluate.evaluate(board, color, false);
		}

		int max = Integer.MIN_VALUE;

		for (Move move : MoveSorter.sort(board, board.getMoves(color))) {

			move.make();
			int score = -negaMaxWithPruning(board, color.opp(), -beta, -alpha,
					moveDepth - 1);
			move.unmake();

			max = Math.max(max, score);
			alpha = Math.max(alpha, score);
			if (alpha >= beta) {
				return alpha;
			}
		}

		return max;
	}
}
