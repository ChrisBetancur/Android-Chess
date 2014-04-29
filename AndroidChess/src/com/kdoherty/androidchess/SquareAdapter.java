package com.kdoherty.androidchess;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.androidchess.R;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Piece;

public class SquareAdapter extends BaseAdapter {

	private Context mContext;
	private Board board;

	private ArrayList<Integer> mPieceIds;

	public SquareAdapter(Context mContext) {
		this(mContext, Board.defaultBoard());
	}
	
	public SquareAdapter(Context mContext, Board board) {
		this.mContext = mContext;
		this.board = board;
		mPieceIds = new ArrayList<Integer>();
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

		PieceImageView square;

		ViewHolder(View v) {
			square = (PieceImageView) v.findViewById(R.id.square_background);
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
			final PieceImageView squareView = (PieceImageView) squareContainerView
					.findViewById(R.id.square_background);
			int row = position / 8;
			int col = position % 8;
			squareView.setRow(row);
			squareView.setCol(col);
			Piece piece = board.getOccupant(row, col);
			// Checker the board
			if (!((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1))) {
				squareContainerView.setBackgroundResource(R.color.light_grey);
			}
			if (piece != null) {
				int id = PieceImages.getId(piece);
				squareView.setImageResource(id);
				squareView.setId(id);
				squareView.setOnTouchListener(OnPieceTouch.INSTANCE);
			}
			squareContainerView.setOnDragListener(new OnPieceDrag(mContext, board, row, col));
		}
		return squareContainerView;
	}
}
