package com.kdoherty.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * This class is an ImageView which can keep track of its position on a grid
 * view.
 * 
 * @author Kevin Doherty
 * 
 */
class PieceImageView extends ImageView {

	/** The row this is located on */
	private int row;

	/** The column this is located on */
	private int col;

	public PieceImageView(Context context) {
		super(context);
	}

	public PieceImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PieceImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Sets the row of this to the input row.
	 * 
	 * @param row
	 *            The row to set this row to
	 */
	void setRow(int row) {
		this.row = row;
	}

	/**
	 * Sets the column of this to the input column.
	 * 
	 * @param col
	 *            The column to set this column to
	 */
	void setCol(int col) {
		this.col = col;
	}

	/**
	 * Gets the row of the Board that this image is located on
	 * 
	 * @return The row of the Board that this image is located on
	 */
	int getRow() {
		return row;
	}

	/**
	 * Gets the column of the Board that this image is located on
	 * 
	 * @return The column of the Board that this image is located on
	 */
	int getCol() {
		return col;
	}
}
