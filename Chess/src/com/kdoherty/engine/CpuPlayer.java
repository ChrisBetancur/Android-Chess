package com.kdoherty.engine;

import java.util.List;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;

/**
 * This class represents a Computer player and its though process.
 * Enum ensures only one instance of each player is created.
 * @author Kevin Doherty
 */
public enum CpuPlayer {
	
	/** White computer player */
	WHITE_INSTANCE(Color.WHITE),
	
	/** Black computer player */
	BLACK_INSTANCE(Color.BLACK); 
	
	/** The Color which this computer CpuPlayer will make moves for */
	private Color color;

	/**
	 * Constructor for CpuPlayer.
	 * 
	 * @param color
	 *            The Color which this computer CpuPlayer will make moves for
	 */
	private CpuPlayer(Color color) {
		this.color = color;
	}
	
	/**
	 * Factory method for a CpuPlayer
	 * @param color The Color of the Piece's this player will be controlling
	 * @return A CpuPlayer which will play moves for the input Color.
	 */
	public static CpuPlayer getInstance(Color color) {
		return color == Color.WHITE ? WHITE_INSTANCE : BLACK_INSTANCE;
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
	public Move negaMaxMove(Board board, long millisRemaining) {
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
		int depth = 1;
		if (millisRemaining > 600000) {
			depth = 3;
		} else if (millisRemaining > 60000) {
			depth = 2;
		}

		int max = Integer.MIN_VALUE;
		Move bestMove = null;

		List<Move> sortedMoves = MoveSorter.sort(board, board.getMoves(color));
		for (Move move : sortedMoves) {
			move.make();
			if (!MateSolver.findMateUpToN(board, color.opp(), 1).isEmpty()) {
				move.unmake();
				continue;
			}
			int score = -negaMaxWithPruning(board, color.opp(),
					Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
			if (score > max) {
				max = score;
				bestMove = move;
			}
			move.unmake();
		}
		
		if (bestMove == null && !sortedMoves.isEmpty()) {
			bestMove = sortedMoves.get(0);
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
	private int negaMaxWithPruning(Board board, Color color, int alpha, int beta,
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

	public Move negaMaxMove(Board board) {
		return negaMaxMove(board, 1000000);
	}
}
