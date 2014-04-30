package com.kdoherty.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class PieceImageView extends ImageView {
	
	private int row;
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
	
	public void setRow(int row) {
		this.row = row;
	}
	
	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}
}
