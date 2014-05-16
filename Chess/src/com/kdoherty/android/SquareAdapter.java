package com.kdoherty.android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kdoherty.androidchess.R;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Piece;

public class SquareAdapter extends BaseAdapter {

	private ChessActivity mContext;
	private Board board;

	public SquareAdapter(ChessActivity mContext) {
		this(mContext, Board.defaultBoard());
	}

	public SquareAdapter(ChessActivity mContext, Board board) {
		this.mContext = mContext;
		this.board = board;
	}

	@Override
	public int getCount() {
		// Board size stays constant
		return Board.NUM_ROWS * Board.NUM_COLS;
	}

	@Override
	public Object getItem(int position) {
		return null;
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
