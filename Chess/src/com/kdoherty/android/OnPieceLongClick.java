package com.kdoherty.android;

import android.content.ClipData;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnLongClickListener;

class OnPieceLongClick implements OnLongClickListener {
	
	private ChessActivity chessContext;
	
	OnPieceLongClick(ChessActivity context) {
		this.chessContext = context;
	}

	@Override
	public boolean onLongClick(View view) {
		if (chessContext.isGameOver() || chessContext.isCpuMove()) {
			return false;
		}
		ClipData data = ClipData.newPlainText("", "");
		DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		view.startDrag(data, shadowBuilder, view, 0);
		view.setVisibility(View.INVISIBLE);
		return true;
	}
}
