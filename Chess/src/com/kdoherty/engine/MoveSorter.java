package com.kdoherty.engine;

import java.util.ArrayList;
import java.util.List;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;

public class MoveSorter {
	
	List<Move> checkingMoves = new ArrayList<Move>();
	List<Move> takingMoves = new ArrayList<Move>();
	List<Move> normalMoves = new ArrayList<Move>();
	
	public static List<Move> sort(Board board, List<Move> moves) {
		if (moves.isEmpty()) {
			return moves;
		}
		List<Move> sortedMoves = new ArrayList<Move>();
		int checkingIndex = 0;
		Color oppColor = moves.get(0).getPiece().getColor().opp();
		
		for (Move move : moves) {
			move.make(board);
			if (board.kingInCheck(oppColor)) {
				sortedMoves.add(0, move);
				checkingIndex++;
			} else if (move.getTaken() != null) {
				sortedMoves.add(checkingIndex, move);
			} else {
				sortedMoves.add(move);
			}
			move.unmake(board);
		}
		if (moves.size() != sortedMoves.size()) {
			throw new AssertionError();
		}
		return sortedMoves;
	}
}
