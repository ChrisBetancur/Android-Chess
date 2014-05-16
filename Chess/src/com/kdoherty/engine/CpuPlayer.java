package com.kdoherty.engine;

import java.util.List;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;

public class CpuPlayer {

	public Move negaMaxMove(Board board, Color color, int depth) {
		Move lastMove = board.getLastMove();
		if (board.getMoveCount() == 1) {
			if (lastMove.toString().equals("pe4")) {
				return new Move(board.getOccupant(1, 4), 3, 4);
			} else if (lastMove.toString().equals("pd4")) {
				return new Move(board.getOccupant(1, 3), 3, 3);
			}
		}
		int max = Integer.MAX_VALUE;
		Move bestMove = null;
		List<Move> mateMoves = MateSolver.findMateUpToN(board, color, 2);
		if (mateMoves != null && !mateMoves.isEmpty()) {
			return mateMoves.get(0);
		}
		for (Move move : board.getMoves(color)) {
			move.make(board);
			int score = negaMaxWithPruning(board, color.opp(),
					Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
			if (score < max) {
				max = score;
				bestMove = move;
			}
			move.unmake(board);
		}
		return bestMove;
	}

	int negaMaxWithPruning(Board board, Color color, int alpha, int beta,
			int depth) {
		if (board.isGameOver()) {
			return Evaluate.evaluate(board, color);
		}
		if (depth == 0) {
			return Evaluate.evaluate(board, color);
		}

		int max = Integer.MIN_VALUE;
		for (Move move : board.getMoves(color)) {
			move.make(board);
			int score = -negaMaxWithPruning(board, color.opp(), -beta, -alpha,
					depth - 1);
			move.unmake(board);
			max = Math.max(max, score);
			alpha = Math.max(alpha, score);
			if (alpha >= beta) {
				return alpha;
			}
		}
		return max;
	}
}
