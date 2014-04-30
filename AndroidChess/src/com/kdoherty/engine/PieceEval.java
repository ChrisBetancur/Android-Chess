package com.kdoherty.engine;

import com.kdoherty.chess.AbstractPiece;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Pawn;

public class PieceEval {

	public static boolean isBlockingCenterPawn(Board b, AbstractPiece p) {
		int pawnRow = (p.getColor() == Color.WHITE) ? 6 : 1;
		int blockingRow = (p.getColor() == Color.WHITE) ? pawnRow - 1
				: pawnRow + 1;
		return p.getRow() == blockingRow
				&& ((p.getCol() == 3 && b.getOccupant(pawnRow, 3) instanceof Pawn) || (p
						.getCol() == 4 && b.getOccupant(pawnRow, 4) instanceof Pawn));
	}
}
