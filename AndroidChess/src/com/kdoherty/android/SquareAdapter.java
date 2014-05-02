package com.kdoherty.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kdoherty.androidchess.R;
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

	public Board getBoard() {
		return board;
	}

	static class ViewHolder {

		PieceImageView pieceViewItem;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			convertView = inflater.inflate(R.layout.square, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.pieceViewItem = (PieceImageView) convertView
					.findViewById(R.id.square_background);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		PieceImageView pieceView = viewHolder.pieceViewItem;
		int row = position / 8;
		int col = position % 8;
		pieceView.setRow(row);
		pieceView.setCol(col);
		Piece piece = board.getOccupant(row, col);
		// Checker the board
		if (!((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1))) {
			convertView.setBackgroundResource(R.color.light_grey);
		}
		if (piece != null) {
			int id = PieceImages.getId(piece);
			pieceView.setImageResource(id);
			pieceView.setId(id);
			pieceView.setOnTouchListener(OnPieceTouch.INSTANCE);
		}
		convertView.setOnDragListener(new OnPieceDrag(mContext, board,
				row, col));
		return convertView;
	}
}
