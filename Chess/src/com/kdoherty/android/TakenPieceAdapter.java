package com.kdoherty.android;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.kdoherty.chess.Piece;

/**
 * This class is responsible for displaying the Pieces which are taken off the
 * Board.
 * 
 * @author Kevin Doherty
 * 
 */
final class TakenPieceAdapter extends BaseAdapter {

	private Context context;

	/** Contains all Pieces which have been taken off the Board */
	private List<Piece> takenPieces;

	public TakenPieceAdapter(Context context) {
		this.context = context;
		takenPieces = new ArrayList<Piece>();
	}

	@Override
	public int getCount() {
		return takenPieces.size();
	}

	@Override
	public Object getItem(int position) {
		return takenPieces.get(position);
	}

	@Override
	public long getItemId(int position) {
		Piece piece = (Piece) getItem(position);
		return PieceImages.getId(piece);
	}

	/**
	 * Adds a Piece to the List of Pieces to display and sorts the List based on
	 * the values assigned to each Piece
	 * 
	 * @param piece
	 *            The Piece to add to the view displaying taken Pieces
	 */
	public void addPiece(Piece piece) {
		if (piece == null) {
			throw new NullPointerException("Can't display a null taken piece");
		}
		takenPieces.add(piece);
		Collections.sort(takenPieces, PieceComparator.INSTANCE);
		notifyDataSetChanged();
	}

	/**
	 * A simple Piece Comparator which compares Pieces by there starting value.
	 * If used to sort a List of Pieces it will sort them from lowest value to
	 * highest value. An enumeration is used to ensure only one instance of this
	 * class is created.
	 * 
	 * @author Kevin Doherty
	 */
	private static enum PieceComparator implements Comparator<Piece> {

		INSTANCE;

		/**
		 * Compares two Pieces based on their starting values.
		 * 
		 * 
		 * @param lhs
		 *            Piece
		 * @param rhs
		 *            Piece
		 * @return A positive integer if the first Piece has a greater starting
		 *         value than the second Piece, 0 if the two Pieces have the
		 *         same starting value, and a negative number if the second
		 *         Piece has a greater starting value than the first.
		 */
		@Override
		public int compare(Piece lhs, Piece rhs) {
			return lhs.getStartingValue() - rhs.getStartingValue();
		}
	}

	/**
	 * Manually creates an ImageView to represent the Piece contained at the
	 * index specified by the input position.
	 * 
	 * @param position
	 *            The position in the GridView
	 * @param convertView
	 *            Used to so we don't recreate the ImageView every time this is
	 *            called
	 * @param parent
	 *            The parent of the view
	 * 
	 * @return The Image representation of the Piece contained at the index
	 *         specified by the input position
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView;

		if (convertView == null) {
			imageView = new ImageView(context);
			imageView.setLayoutParams(new GridView.LayoutParams(60, 60));
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ImageView) convertView;
		}

		int resId = (int) getItemId(position);
		imageView.setImageResource(resId);

		return imageView;
	}
}
