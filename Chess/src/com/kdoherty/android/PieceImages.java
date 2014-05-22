package com.kdoherty.android;

import java.util.HashMap;
import java.util.Map;

import com.kdoherty.androidchess.R;
import com.kdoherty.chess.Piece;

/**
 * This class connects Pieces to their corresponding image.
 * 
 * @author Kevin Doherty
 * 
 */
final class PieceImages {

	/**
	 * Maps the Pieces represented as Strings to their image resource IDs.
	 * Strings are used as keys in place of Pieces because Pieces are mutable
	 */
	private static final Map<String, Integer> instance = new HashMap<String, Integer>();

	private PieceImages() {
		// Should never be instantiated
	}

	/**
	 * Fills the map with entries which contain String representation of Pieces
	 * and the image of the Piece.
	 */
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

	/**
	 * Gets the resource id of the image which represents the input Piece.
	 * 
	 * @param piece
	 *            The Piece to get the image ID of
	 * @return The resource id of the input Piece
	 */
	static Integer getId(Piece piece) {
		// A null piece represents an empty square
		if (piece == null) {
			return R.color.transparent;
		}
		return instance.get(piece.toString());
	}
}
