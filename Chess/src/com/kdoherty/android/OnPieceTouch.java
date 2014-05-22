package com.kdoherty.android;

import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;

/**
 * This is responsible for starting to drag a Piece once it is touched.
 * Only one instance of this Class should ever be needed.
 * 
 * @author Kevin Doherty
 * 
 */
enum OnPieceTouch implements OnTouchListener {

	INSTANCE;

	/**
	 * Starts the dragging of a Piece. As soon as dragging is started, the Piece
	 * itself becomes invisible on its current Square, but a shadow of the Piece
	 * is seen where the Piece is being dragged.
	 */
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			ClipData data = ClipData.newPlainText("", "");
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
			view.startDrag(data, shadowBuilder, view, 0);
			view.setVisibility(View.INVISIBLE);
			return true;
		default:
			return false;
		}
	}

}
