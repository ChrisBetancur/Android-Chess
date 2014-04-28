package com.kdoherty.androidchess;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.androidchess.R;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Piece;

public class SquareAdapter extends BaseAdapter {

	private Context mContext;
	private Board board;

	private ArrayList<Integer> mPieceIds;

	public SquareAdapter(Context mContext) {
		this.mContext = mContext;
		board = new Board();
		mPieceIds = new ArrayList<Integer>();
		board.fillWithDefaultPieces();
		for (Piece p : board.getAllPieces()) {
			mPieceIds.add(PieceImages.getId(p));
		}
	}

	@Override
	public int getCount() {
		return mPieceIds.size();
	}

	@Override
	public Object getItem(int position) {
		return mPieceIds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	class ViewHolder {

		ImageView piece;
		ImageView square;

		ViewHolder(View v) {
			piece = (ImageView) v.findViewById(R.id.piece);
			square = (ImageView) v.findViewById(R.id.square_background);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View squareContainerView = convertView;

		if (convertView == null) {
			// Inflate the layout
			final LayoutInflater layoutInflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			squareContainerView = layoutInflater.inflate(R.layout.square, null);

			// Background
			final ImageView squareView = (ImageView) squareContainerView
					.findViewById(R.id.square_background);
			
			Piece piece = board.getOccupant(position / 8, position % 8);
			if (piece != null) {
				squareView.setImageResource(PieceImages.getId(piece));
				squareView.setOnTouchListener(new MainActivity());
			}
			//squareView.setOnDragListener(new MainActivity());
		}
		return squareContainerView;
	}
}
