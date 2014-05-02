package com.kdoherty.engine;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;

public class CpuPlayer {
	
	public Move negaMaxMove(Board board, Color color, int depth) {
		int max = Integer.MAX_VALUE;
		Move best = null;
		
		for (Move move : board.getMoves(color)) {
	    	move.makeMove(board);
	    	int score = negaMaxWithPruning(board, color.opp(), Integer.MIN_VALUE, Integer.MAX_VALUE, depth);
			if (score < max) {
	            max = score;
	            best = move;
	        }
			move.undo(board);
		}
		return best;
	}
	
	int negaMaxWithPruning(Board board, Color color, int alpha, int beta, int depth) {
		if (depth == 0 || board.isGameOver()) {
	    	return PosnEval.evaluate(board, color);
	    }
		int max = Integer.MIN_VALUE;
		for (Move move : board.getMoves(color)) {
	    	move.makeMove(board);
	    	int score = -negaMaxWithPruning(board, color.opp(), -beta, -alpha, depth - 1);
	    	move.undo(board);
	    	max = Math.max(max, score);
	    	alpha = Math.max(alpha, score);
	    	if (alpha >= beta) {
	    		return alpha;
	    	}
		}
		return max;
	}
}
