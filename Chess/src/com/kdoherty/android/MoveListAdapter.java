package com.kdoherty.android;

import java.util.ArrayList;
import java.util.List;

import com.kdoherty.chess.Move;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

final class MoveListAdapter extends BaseAdapter {
	
	private final List<Move> moves = new ArrayList<Move>();
	private final Context context;
	
	MoveListAdapter(Context context) {
		this.context = context;
	}
	
	void addMove(Move move) {
		if (move == null) {
			throw new NullPointerException("Can't display a null move");
		}
		moves.add(move);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return moves.size();
	}

	@Override
	public Object getItem(int position) {
		return moves.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView;

		if (convertView == null) {
			textView = new TextView(context);
			textView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.WRAP_CONTENT, 60));
		} else {
			textView = (TextView) convertView;
		}
		
		String moveNum = position % 2 == 0 ? String.valueOf(position / 2 + 1) + ". " : ""; 
		textView.setText(moveNum + getItem(position).toString());
		textView.setTextSize(8);

		return textView;
	}
	
}
