package com.kdoherty.android;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class SquareGridView extends GridView {

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

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, widthMeasureSpec);
	}

	@Override
	protected void onSizeChanged(final int w, final int h, final int oldw,
			final int oldh) {
		super.onSizeChanged(w, w, oldw, oldh);
	}
}
