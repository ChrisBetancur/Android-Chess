package com.kdoherty.androidchess;

import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.androidchess.R;

public class MainActivity extends Activity implements OnDragListener,
		OnTouchListener, OnLongClickListener {

	// TODO: Drag and drop

	SquareAdapter adapter;

	GridView boardView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initBoard();
		// GridLayout gridLayout = (GridLayout) findViewById(R.id.board_layout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void initBoard() {
		adapter = new SquareAdapter(this);
		boardView = (GridView) findViewById(R.id.chessboard);
		boardView.setAdapter(adapter);
		boardView.setOnDragListener(this);
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		if (v instanceof GridView) {
			Log.d("kdoherty", "GridView onDrag");
		} else if (v instanceof ImageView) {
			Log.d("kdoherty", "ImageView onDrag");
		}
		Log.d("kdoherty", "Drag local state: " + event.getLocalState());
		int action = event.getAction();
		switch (action) {
		case DragEvent.ACTION_DRAG_STARTED:
			//Log.d("kdoherty", "Drag Started");
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			//Log.d("kdoherty", "Drag Entered");
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			//Log.d("kdoherty", "Drag Ended");
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			//Log.d("kdoherty", "Drag Exited");
			break;
		case DragEvent.ACTION_DROP:
			Log.d("kdoherty", "Drag Dropped");
			View view = (View) event.getLocalState();
			ViewGroup owner = (ViewGroup) view.getParent();
			owner.removeView(view);
			FrameLayout container = (FrameLayout) v;
			container.addView(view);
			view.setVisibility(View.VISIBLE);
			break;
		default:
			//Log.d("kdoherty", "Drag Default");
			break;
		}
		return false;
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if (view instanceof GridView) {
			Log.d("kdoherty", "GridView onTouch");
		} else if (view instanceof ImageView) {
			Log.d("kdoherty", "ImageView onTouch");
		}
		
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.d("kdoherty", "onTouch ActionDown Case");
			ClipData data = ClipData.newPlainText("", "");
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
			view.startDrag(data, shadowBuilder, view, 0);
			view.setVisibility(View.INVISIBLE);
			return true;
		default:
			Log.d("kdoherty", "onTouch Default Case");
			return true;
		}
	}

	@Override
	public boolean onLongClick(View view) {
		ClipData data = ClipData.newPlainText("", "");
		DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
		view.startDrag(data, shadowBuilder, view, 0);
		view.setVisibility(View.INVISIBLE);
		return true;
	}
}
