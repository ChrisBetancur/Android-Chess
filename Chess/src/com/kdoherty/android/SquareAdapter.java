package com.kdoherty.android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kdoherty.androidchess.R;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Piece;

/**
 * This is responsible for binding the Board representation to the view of the
 * Board and each of its Pieces.
 * 
 * @author Kevin Doherty
 * 
 */
final class SquareAdapter extends BaseAdapter {

	/** The context which this adapter is called from */
	private ChessActivity context;

	/** The Board that this will represent */
	private Board board;

	/**
	 * Starts with a Board in which all Pieces are in their default positions.
	 * 
	 * @param context
	 *            The context which this adapter is called from
	 */
	public SquareAdapter(ChessActivity context) {
		this(context, Board.defaultBoard());
	}

	/**
	 * Creates a new instance to represent the input Board.
	 * 
	 * @param context
	 *            The context which this adapter is called from
	 * @param board
	 *            The Board to display
	 */
	public SquareAdapter(ChessActivity context, Board board) {
		this.context = context;
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

	/**
	 * Gets the Board this adapter is representing
	 * 
	 * @return The Board this adapter is representing
	 */
	Board getBoard() {
		return board;
	}

	/**
	 * Used to hold PieceImageViews so we don't have to inflate the same View
	 * multiple times
	 */
	static class ViewHolder {

		PieceImageView pieceViewItem;

	}

	/**
	 * Displays each Square in the Boards grid view. This is where the Piece
	 * images are set and the Board is checkered.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;

		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.square, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.pieceViewItem = (PieceImageView) convertView
					.findViewById(R.id.squareView);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		int row = position / 8;
		int col = position % 8;

		PieceImageView pieceView = viewHolder.pieceViewItem;
		pieceView.setRow(row);
		pieceView.setCol(col);

		// Checker the board
		if (!((row % 2 == 0 && col % 2 == 0) || (row % 2 == 1 && col % 2 == 1))) {
			convertView.setBackgroundResource(R.color.wood_brown);
		} else {
			convertView.setBackgroundResource(R.color.light_brown);
		}

		Piece piece = board.getOccupant(row, col);
		if (piece != null) {
			int id = PieceImages.getId(piece);
			pieceView.setImageResource(id);
			pieceView.setId(id);
			pieceView.setOnTouchListener(OnPieceTouch.INSTANCE);
		}

		convertView.setOnDragListener(new OnPieceDrag(context, board, row, col));

		return convertView;
	}
}
