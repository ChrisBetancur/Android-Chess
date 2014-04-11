package com.kdoherty.androidchess;

import java.util.HashMap;

import com.example.androidchess.R;
import com.kdoherty.chess.Bishop;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.King;
import com.kdoherty.chess.Knight;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Piece;
import com.kdoherty.chess.Queen;
import com.kdoherty.chess.Rook;

public final class PieceImages {
	
	private static HashMap<String, Integer> instance = new HashMap<String, Integer>();;
	
	private PieceImages() {
		// Hide constructor
		throw new AssertionError("Never call PieceImages constructor");
	}
	
	static {
		instance.put("p", R.drawable.whitepawn);
		instance.put("r", R.drawable.whiterook);
		instance.put("n", R.drawable.whiteknight);
		instance.put("b", R.drawable.whitebishop);
		instance.put("k", R.drawable.whiteking);
		instance.put("q", R.drawable.whitequeen);
		
		instance.put("P", R.drawable.blackpawn);
		instance.put("R", R.drawable.blackrook);
		instance.put("N", R.drawable.blackknight);
		instance.put("B", R.drawable.blackbishop);
		instance.put("K", R.drawable.blackking);
		instance.put("Q", R.drawable.blackqueen);
	}
	
	public static Integer getId(Piece piece) {
		return instance.get(piece.toString());
	}

}
