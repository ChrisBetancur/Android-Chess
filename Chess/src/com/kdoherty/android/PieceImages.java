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
	private static final Map<String, Integer> pieceImages= new HashMap<String, Integer>();

	private PieceImages() {
		// Hide constructor
	}

	/**
	 * Fills the map with entries which contain String representation of Pieces
	 * and the image of the Piece.
	 */
	static {
		pieceImages.put("p", R.drawable.whitepawn);
		pieceImages.put("r", R.drawable.whiterook);
		pieceImages.put("n", R.drawable.whiteknight);
		pieceImages.put("b", R.drawable.whitebishop);
		pieceImages.put("k", R.drawable.whiteking);
		pieceImages.put("q", R.drawable.whitequeen);

		pieceImages.put("P", R.drawable.blackpawn);
		pieceImages.put("R", R.drawable.blackrook);
		pieceImages.put("N", R.drawable.blackknight);
		pieceImages.put("B", R.drawable.blackbishop);
		pieceImages.put("K", R.drawable.blackking);
		pieceImages.put("Q", R.drawable.blackqueen);
	}

	/**
	 * Gets the resource id of the image which represents the input Piece.
	 * 
	 * @param piece
	 *            The Piece to get the image ID of
	
	 * @return The resource id of the input Piece */
	static Integer getId(Piece piece) {
		// A null piece represents an empty square
		if (piece == null) {
			return R.color.transparent;
		}
		return pieceImages.get(piece.toString());
	}
}
