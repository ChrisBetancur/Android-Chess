package com.kdoherty.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * This class ensures that the Squares in the Board view are always Square
 * @author Kevin Doherty
 *
 */
class SquareGridView extends GridView {

	public SquareGridView(final Context context) {
		super(context);
	}

	public SquareGridView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareGridView(final Context context, final AttributeSet attrs,
			final int defStyle) {
		super(context, attrs, defStyle);
	}

	/** 
	 * Only pass widthMeasureSpc to GridView's onMeasure method so that
	 * it stays Square.
	 */
	@Override public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

	/**
	 * Only pass width to GridView's onSizeChanged method so that
	 * it stays Square.
	 */
	@Override protected void onSizeChanged(final int w, final int h,
			final int oldw, final int oldh) {
		super.onSizeChanged(w, w, oldw, oldh);
	}
}
