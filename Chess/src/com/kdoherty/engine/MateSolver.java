package com.kdoherty.engine;

import java.util.ArrayList;
import java.util.List;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;

public class MateSolver {

	private MateSolver() {
		// Hide Constructor
	}

	/*
	 * If there is a move which color can make which puts the oppColor in
	 * check-mate it returns that move, otherwise returns null
	 */
	public static Move findMateInOne(Board b, Color color) {
		List<Move> moves = new ArrayList<Move>();
		moves = b.getMoves(color);
		for (Move move : moves) {
			move.make(b);
			if (b.isCheckMate(color.opp())) {
				move.unmake(b);
				return move;
			} else {
				move.unmake(b);
			}
		}
		return null;
	}

	// if there is a forced mate this will return A solution. The other side may
	// make different moves and other corresponding moves
	// will need to be made in the output but it is forced either way
	public static List<Move> findMateInN(Board b, Color color, int depth) {
		List<Move> mateMoves = new ArrayList<Move>();
		List<Move> nextMateMoves = new ArrayList<Move>();
		boolean soonerMate = false;
		if (depth == 1) {
			Move move = findMateInOne(b, color);
			if (move != null) {
				mateMoves.add(move);
			}
			return mateMoves;
		} else {
			for (Move m : b.getMoves(color)) {
				mateMoves.add(m);
				m.make(b);
				if (mateMoves.size() != 0) {
					for (Move mo : b.getMoves(color.opp())) {
						nextMateMoves = null;
						soonerMate = false;
						mo.make(b);
						// make sure a sooner mate can't be found
						for (int i = 1; i < depth - 1; i++) {
							List<Move> test = new ArrayList<Move>();
							test = findMateInN(b, color, i);
							if (!(test == null || test.size() == 0)) {
								soonerMate = true;
							}
						}
						if (!soonerMate) {
							nextMateMoves = findMateInN(b, color, depth - 1);
							if (nextMateMoves == null
									|| nextMateMoves.size() == 0) {
								mateMoves.clear();
							}
						}
						mo.unmake(b);
					}
				}
				if (!(nextMateMoves == null || nextMateMoves.size() == 0)) {
					m.unmake(b);
					mateMoves.addAll(nextMateMoves);
					return mateMoves;
				}
				m.unmake(b);
			}
		}
		// No mate was found... Return the empty list of moves
		return mateMoves;
	}

	public static List<Move> findMateUpToN(Board b, Color color, int n) {
		List<Move> mate = new ArrayList<Move>();
		for (int i = 1; i <= n; i++) {
			mate = findMateInN(b, color, i);
			if (mate != null && mate.size() != 0)
				return mate;
		}
		return null;
	}
}
