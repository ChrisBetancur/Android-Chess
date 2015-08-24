package com.kdoherty.engine;

import java.util.List;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;

/**
 * This class represents a Computer player and its though process.
 * Using Enum to ensure only one instance of each player is created.
 * @author Kevin Doherty
 */
public enum CpuPlayer {
	
	/** White computer player */
	WHITE_INSTANCE(Color.WHITE),
	
	/** Black computer player */
	BLACK_INSTANCE(Color.BLACK); 
	
	private static final long THREE_DEPTH_THRESHOLD = 60 * 10 * 1000;
	
	private static final long TWO_DEPTH_THRESHOLD = 60 * 1000;
	
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
	
	private int getMoveDepth(long millisRemaining) {
		if (millisRemaining > THREE_DEPTH_THRESHOLD) {
			return 3;
		} else if (millisRemaining > TWO_DEPTH_THRESHOLD) {
			return 2;
		}
		return 1;
	}
	
	private int getMateSearchDepth(Board board) {
		return Evaluate.queenCloseToKing(board, color) ? 3 : 2;
	}

	/**
	 * Determines the "best" move on the input Board. It starts by looking for a
	 * forced checkmate in the position at a depth varying depending on how
	 * close our Queen is to the other player's King. If it does not find
	 * anything, it will try each move on the Board, and then evaluate it at
	 * a depth based on the remaining time. This assumes that the opponent makes the best
	 * possible reply to each Move. Whichever Move has the worst best reply is
	 * chosen.
	 * 
	 * 
	 * @param board
	 *            The Board determine the best move on
	 * @param millisRemaining
	 * 	      The amount of time remaining to make a move
	 * @return Move What was determined to be the best move
	 */
	public Move negaMaxMove(Board board, long millisRemaining) {
		int mateDepth = getMateSearchDepth(board);
		List<Move> mateMoves = MateSolver
				.findMateUpToN(board, color, mateDepth);
		if (!mateMoves.isEmpty()) {
			// There is a forced checkmate
			return mateMoves.get(0);
		}
		
		int depth = getMoveDepth(millisRemaining);
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
			return Evaluate.evaluate(board, color);
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
