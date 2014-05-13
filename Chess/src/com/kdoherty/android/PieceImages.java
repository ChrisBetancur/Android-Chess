package com.kdoherty.android;

import java.util.HashMap;
import java.util.Map;

import com.kdoherty.androidchess.R;
import com.kdoherty.chess.Piece;

public final class PieceImages {

	private static final HashMap<String, Integer> instance = new HashMap<String, Integer>();

	private PieceImages() {
	}

	static {
		// Note: Not using Pieces for keys because they are mutable
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
		// A null piece represents an empty square
		if (piece == null) {
			return R.color.transparent;
		}
		return instance.get(piece.toString());
	}
	
	public static String getPieceString(int id) {
		for (Map.Entry<String, Integer> entry : instance.entrySet()) {
			if (entry.getValue().equals(id)) {
				return entry.getKey();
			}
		}
		throw new IllegalArgumentException("Key " + id + " not found in PieceImages");
	}
}
