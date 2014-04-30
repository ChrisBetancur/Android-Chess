package com.kdoherty.engine;

import java.util.ArrayList;
import java.util.List;

import com.kdoherty.chess.AbstractPiece;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;

public class MateSolver {

	private MateSolver() {
	}

	/*
	 * If there is a move which color can make which puts the oppColor in
	 * check-mate it returns that move, otherwise returns null
	 */
	public static Move findMate1(Board b, Color color) {
		AbstractPiece cur = null;
		AbstractPiece taken = null;
		List<Move> moves = new ArrayList<Move>();
		moves = b.getMoves(color);
		for (Move firstMove : moves) {
			cur = firstMove.getPiece();
			int r = cur.getRow();
			int c = cur.getCol();
			taken = firstMove.makeMove(b);
			if (b.isCheckMate(color.opp())) {
				firstMove.undo(b, r, c, taken);
				return firstMove;
			} else {
				firstMove.undo(b, r, c, taken);
			}
		}
		return null;
	}

	// if there is a forced mate this will return A solution. The other side may
	// make different moves and other corresponding moves
	// will need to be made in the output but it is forced either way
	public static List<Move> SolveMateInN(Board b, Color color, int depth) {
		List<Move> mateMoves = new ArrayList<Move>();
		List<Move> nextMateMoves = new ArrayList<Move>();
		List<Move> moves = new ArrayList<Move>();
		List<Move> nextMoves = new ArrayList<Move>();
		AbstractPiece cur, nextCur, taken, nextTaken;
		boolean soonerMate = false;
		if (depth == 1) {
			mateMoves.add(findMate1(b, color));
			return mateMoves;
		} else {
			moves = b.getMoves(color);
			for (Move m : moves) {
				cur = m.getPiece();
				int curRow = cur.getRow();
				int curCol = cur.getCol();
				mateMoves.add(m);
				taken = m.makeMove(b);
				nextMoves = b.getMoves(color.opp());
				for (Move mo : nextMoves) {
					nextCur = mo.getPiece();
					int nextCurRow = nextCur.getRow();
					int nextCurCol = nextCur.getCol();
					if (mateMoves.size() != 0) {
						nextMateMoves = null;
						nextTaken = null;
						soonerMate = false;
						b.remove(nextCurRow, nextCurCol);
						nextTaken = mo.makeMove(b);
						// make sure a sooner mate can't be found
						for (int i = 1; i < depth - 1; i++) {
							List<Move> test = new ArrayList<Move>();
							test = SolveMateInN(b, color, i);
							if (!(test == null || test.get(0) == null)) {
								soonerMate = true;
							}
						}
						if (!soonerMate) {
							nextMateMoves = SolveMateInN(b, color, depth - 1);
							if (nextMateMoves == null
									|| nextMateMoves.get(0) == null) {
								mateMoves.clear();
							}
						}
						mo.undo(b, nextCurRow, nextCurCol, nextTaken);
					}
				}
				if ((!(nextMateMoves == null || nextMateMoves.size() == 0 || nextMateMoves
						.get(0) == null))) {
					m.undo(b, curRow, curCol, taken);
					mateMoves.addAll(nextMateMoves);
					return mateMoves;
				}
				m.undo(b, curRow, curCol, taken);
			}
		}
		return null;
	}

	public static List<Move> findMateUpToN(Board b, Color color, int n) {
		List<Move> mate = new ArrayList<Move>();
		for (int i = 1; i <= n; i++) {
			mate = SolveMateInN(b, color, i);
			if (!(mate == null || mate.get(0) == null))
				return mate;
		}
		return null;
	}
}
