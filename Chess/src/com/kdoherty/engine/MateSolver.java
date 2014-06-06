package com.kdoherty.engine;

import java.util.ArrayList;
import java.util.List;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;

/**
 * 
 * This class is responsible for finding forced checkmates in positions. Note:
 * This class does not use the move sorted because it hurts performance in
 * position where a forced checkmate does not exists which is the majority of
 * actual chess positions.
 * 
 * @author Kevin Doherty
 */
public final class MateSolver {

	private MateSolver() {
		// Hide Constructor
	}
	
	/**
	 * If there is a forced checkmate in the input Board's position in the input
	 * depth or under moves this will return a solution. The other side may make
	 * different responses and other corresponding moves will be needed to be
	 * made but there will be a mate in the input depth moves. If no forced
	 * checkmate is found, an empty list of moves will be returned.
	 * 
	 * @param b
	 *            The Board to find a forced checkmate on.
	 * @param color
	 *            The Color to look for the forced checkmate from.
	 * @param n
	 *            The number of moves to look for a checkmate in or less.
	 * @return The moves needed to ensure a forced mate if one was found, or an
	 *         empty list of none was found.
	 */
	public static List<Move> findMateUpToN(Board b, Color color, int n) {
		List<Move> mateMoves = new ArrayList<Move>();
		for (int i = 1; i <= n; i++) {
			mateMoves = findMateInN(b, color, i);
			if (!mateMoves.isEmpty()) {
				return mateMoves;
			}
		}
		return mateMoves;
	}
	
	/**
	 * If there is a forced checkmate in the input Board position this will
	 * return a solution. The other side may make different responses and other
	 * corresponding moves will be needed to be made but there will be a mate in
	 * the input depth moves. If no forced checkmate is found, an empty list of
	 * moves will be returned. Note: If a depth of 3 is given and there is only
	 * a forced checkmate in 2 moves. Nothing will be found.
	 * 
	 * @param b
	 *            The Board to find a forced checkmate on.
	 * @param color
	 *            The Color to look for the forced checkmate from.
	 * @param depth
	 *            The number of moves to look ahead.
	 * @return If there is a forced checkmate in the input Board position this
	 *         will return a solution. The actual moves returned other than the
	 *         first one may vary depending on the response of the other player
	 *         to the first move being made. If no forced checkmate is found, an
	 *         empty list of moves will be returned.
	 */
	private static List<Move> findMateInN(Board b, Color color, int depth) {
		final List<Move> mateMoves = new ArrayList<Move>();
		boolean soonerMate = false;
		if (depth == 1) {
			Move move = findMateInOne(b, color);
			if (move != null) {
				mateMoves.add(move);
			}
			return mateMoves;
		} else {
			final List<Move> nextMateMoves = new ArrayList<Move>();
			for (Move m : b.getMoves(color)) {
				mateMoves.add(m);
				m.make();
				for (Move mo : b.getMoves(color.opp())) {
					nextMateMoves.clear();
					soonerMate = false;
					mo.make();

					// make sure a sooner mate can't be found
					for (int i = 1; i < depth - 1; i++) {
						List<Move> test = new ArrayList<Move>();
						test = findMateInN(b, color, i);
						if (!(test == null || test.size() == 0)) {
							soonerMate = true;
						}
					}

					if (!soonerMate) {

						nextMateMoves.addAll(findMateInN(b, color, depth - 1));

						if (nextMateMoves.isEmpty()) {
							// A way to stop mate was found. Don't need to keep
							// checking this move
							mateMoves.clear();
							mo.unmake();
							break;
						}
					}

					mo.unmake();
				}
				if (!nextMateMoves.isEmpty()) {
					m.unmake();
					mateMoves.addAll(nextMateMoves);
					return mateMoves;
				}
				m.unmake();
			}
		}
		// No forced mate was found... Return the empty list of moves
		return mateMoves;
	}

	/**
	 * Finds a checkmate move looking at a depth of one. This is the base case
	 * for findMateInN.
	 * 
	 * @param b
	 *            The Board to find the forced checkmate on.
	 * @param color
	 *            The Color to find the forced checkmate from
	 * @return Move The Move which results in a checkmate on the opposite color
	 *         from the input Color.
	 */
	private static Move findMateInOne(Board b, Color color) {
		List<Move> moves = b.getMoves(color);
		for (Move move : moves) {
			move.make();
			if (b.isCheckMate(color.opp())) {
				move.unmake();
				return move;
			} else {
				move.unmake();
			}
		}
		return null;
	}
}