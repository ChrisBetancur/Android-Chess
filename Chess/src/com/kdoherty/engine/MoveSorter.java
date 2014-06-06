package com.kdoherty.engine;

import java.util.ArrayList;
import java.util.List;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;

/**
 * This class is responsible for sorting Moves in the most likely order to be
 * "good". This speeds up move making because it results in significantly more
 * pruning done in the negaMax algorithm in CpuPlayer.
 * 
 * @author Kevin Doherty
 */
final class MoveSorter {
	
	private MoveSorter() {
		// Hide constructor
	}

	/**
	 * Sorts the input list of moves by putting the likely to be good moves
	 * closer to the start of the list.
	 * 
	 * @param board
	 *            Board The Board used to guess how likely it is that the move
	 *            is good
	 * @param moves
	 *            The list of moves to sort.
	 * @return A sorted copy of the input list of with moves which are likely to
	 *         be good at the start. The order of Move priority is as follows:
	 *         1. Checking Moves 2. Taking Moves 3. Castling Moves 4. Normal
	 *         Moves
	 */
	public static List<Move> sort(Board board, List<Move> moves) {
		if (moves.isEmpty()) {
			return moves;
		}

		List<Move> sortedMoves = new ArrayList<Move>();
		int checkingIndex = 0;
		int takingIndex = 0;
		Color oppColor = moves.get(0).getPiece().getColor().opp();

		for (Move move : moves) {
			move.make();
			if (board.kingInCheck(oppColor)) {
				sortedMoves.add(0, move);
				checkingIndex++;
			} else if (move.getTaken() != null) {
				sortedMoves.add(checkingIndex, move);
				takingIndex++;
			} else if (move.getType().isCastling()) {
				sortedMoves.add(checkingIndex + takingIndex, move);
			} else {
				sortedMoves.add(move);
			}
			move.unmake();
		}

		return sortedMoves;
	}
}
