package com.kdoherty.engine;

import java.util.ArrayList;
import java.util.List;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Move;

public class MoveSorter {
	
	public static List<Move> sort(Board board, List<Move> moves) {
		List<Move> sortedMoves = new ArrayList<Move>();
		int checkingIndex = 0;
		for (Move move : moves) {
			if (move.isChecking(board)) {
				sortedMoves.add(0, move);
				checkingIndex++;
			} else if (move.isTaking()) {
				sortedMoves.add(checkingIndex, move);
			} else {
				sortedMoves.add(move);
			}
		}
		return sortedMoves;
	}
}
