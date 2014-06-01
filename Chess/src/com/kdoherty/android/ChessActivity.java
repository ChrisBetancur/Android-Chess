package com.kdoherty.android;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kdoherty.androidchess.R;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;
import com.kdoherty.chess.Piece;
import com.kdoherty.chess.Queen;
import com.kdoherty.engine.CpuPlayer;

/**
 * This Activity is responsible for displaying the chess board, the player's
 * times, and the Pieces removed from the Board.
 * 
 * @author Kevin Doherty
 * 
 */
public class ChessActivity extends Activity {

	// TODO: Make computer move in the background
	// TODO: Pawn Promotion
	// TODO: Wood images for Squares
	// TODO: Get configuration data from previous Activity
	// TODO: Landscape orientation view
	// TODO: White taken Piece #8 gets a tiny bit cut off

	boolean isCpuPlayer = true;
	private static final Color cpuColor = Color.BLACK;
	private static final int cpuDepth = 1;
	private CpuPlayer player = new CpuPlayer(cpuColor, cpuDepth);
	/** The starting game time for each player in milliseconds */
	private long startTime = 900000; // 15 minutes

	/**
	 * The timer which is currently ticking down. Also represents whose side it
	 * is to move
	 */
	private Color activeTimer = Color.WHITE;

	/** The white count down timer */
	private CountDownTimerPausable whiteTimer;

	/** The black count down timer */
	private CountDownTimerPausable blackTimer;

	/** The View displaying the amount of time black has left to move */
	private TextView whiteTimerView;

	/** The View displaying the amount of time black has left to move */
	private TextView blackTimerView;

	/** Used to represent the Board */
	//
	private GridView boardView;

	/** Adapter which binds the Board representation to the view of the Board */
	private SquareAdapter adapter;

	/** Responsible for holding the taken white Pieces */
	private GridView whiteTakenPieces;

	/** Responsible for holding the taken black Pieces */
	private GridView blackTakenPieces;

	/** Adapter used to display a List of white Pieces */
	private TakenPieceAdapter whiteTakenAdapter;

	/** Adapter used to display a List of black Pieces */
	private TakenPieceAdapter blackTakenAdapter;

	/** Keeps track of if its the first or second click on the Board */
	private boolean isFirstClick = true;

	/** Is either player out of time? */
	private boolean isOutOfTime = false;

	/**
	 * Represents the Piece which is currently clicked and will attempt to move
	 * to the next clicked square
	 */
	private Piece activePiece;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_chess);

		initTimers();
		initBoard();
		initPieceHolders();
		
		if (isCpuMove()) {
			makeCpuMove();         
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Initializes the timers for both white and black and sets them to the
	 * starting time.
	 */
	private void initTimers() {

		blackTimerView = (TextView) findViewById(R.id.blackTimer);
		whiteTimerView = (TextView) findViewById(R.id.whiteTimer);

		final SimpleDateFormat timeFormat = new SimpleDateFormat("m:ss",
				Locale.getDefault());

		String startTimeStr = timeFormat.format(startTime);
		whiteTimerView.setText(startTimeStr);
		blackTimerView.setText(startTimeStr);

		whiteTimer = new CountDownTimerPausable(startTime, 100) {

			@Override
			public void onTick(long millisUntilFinished) {
				whiteTimerView.setText(timeFormat.format(millisUntilFinished));
			}

			@Override
			public void onFinish() {
				Toast.makeText(ChessActivity.this, "OUT OF TIME. BLACK WINS!",
						Toast.LENGTH_LONG).show();
				isOutOfTime = true;
			}
		};

		blackTimer = new CountDownTimerPausable(startTime, 100) {

			@Override
			public void onTick(long millisUntilFinished) {
				blackTimerView.setText(timeFormat.format(millisUntilFinished));
			}

			@Override
			public void onFinish() {
				Toast.makeText(ChessActivity.this, "OUT OF TIME. WHITE WINS!",
						Toast.LENGTH_LONG).show();
				isOutOfTime = true;
			}
		};
	}

	/**
	 * Initializes the board adapter and the board view. It then connects the
	 * adapter to the view.
	 */
	private void initBoard() {
		adapter = new SquareAdapter(this);
		boardView = (GridView) findViewById(R.id.chessboard);
		boardView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		boardView.setAdapter(adapter);
	}

	/**
	 * Initializes the holders of taken Pieces
	 */
	private void initPieceHolders() {
		whiteTakenPieces = (GridView) findViewById(R.id.whiteTakenPieces);
		whiteTakenAdapter = new TakenPieceAdapter(this);
		whiteTakenPieces.setAdapter(whiteTakenAdapter);

		blackTakenPieces = (GridView) findViewById(R.id.blackTakenPieces);
		blackTakenAdapter = new TakenPieceAdapter(this);
		blackTakenPieces.setAdapter(blackTakenAdapter);
	}

	/**
	 * Responsible for the following functionality: 
	 * 1. Makes the input move on the Board 
	 * 2. Refreshes the Board so it is displaying the most up to date version 
	 * 3. Toggles the timers 
	 * 4. Toggles the side to move 
	 * 5. Adds the input move to the stack of Moves contained in the Board 
	 * 6. Checks and notifies players of the end of the game 
	 * 7. If a Piece was taken, display that Piece with the other taken Pieces 
	 * 8. If it is then the computer's turn, make the computer's move
	 * 
	 * @param move
	 *            The move to make on the Board
	 */
	void passTurn(Move move) {
		Board board = adapter.getBoard();
		move.make(board);
		// TODO: En poissant sq is null after 1. pe4
		refreshAdapter(board);
		board.toggleSideToMove();
		board.addMove(move);
		Piece taken = move.getTaken();
		if (taken != null) {
			addToTakenPieces(taken);
		}
		if (board.isGameOver()) {
			showGameOver();
		} else {
			toggleTimer();
		}
		if (isCpuMove()) {
			makeCpuMove();
		}
	}

	/**
	 * Updates the UI to represent the input Board
	 * 
	 * @param board
	 *            The Board to represent in the UI
	 */
	private void refreshAdapter(Board board) {
		adapter = new SquareAdapter(this, board);
		boardView.setAdapter(adapter);
	}

	/**
	 * Pauses the timer which is currently active and starts the opposing
	 * Color's timer.
	 */
	private void toggleTimer() {
		if (!getTimer(activeTimer).isPaused()) {
			getTimer(activeTimer).pause();
		}
		activeTimer = activeTimer.opp();
		getTimer(activeTimer).start();
	}

	/**
	 * Displays a message to the User that the game is over
	 */
	private void showGameOver() {
		Board board = adapter.getBoard();
		Color sideToMove = board.getSideToMove();
		if (board.isCheckMate(sideToMove)) {
			Toast.makeText(this, "CHECKMATE! " + sideToMove.opp() + " WINS",
					Toast.LENGTH_LONG).show();
		} else if (board.isDraw(sideToMove)) {
			Toast.makeText(this, "DRAW!", Toast.LENGTH_LONG).show();
		}
		getTimer(activeTimer).cancel();
	}

	/**
	 * Gets the timer corresponding to the input color
	 * 
	 * @param color
	 *            The Color of the timer to get
	 * @return The timer corresponding to the input color
	 */
	private CountDownTimerPausable getTimer(Color color) {
		return color == Color.WHITE ? whiteTimer : blackTimer;
	}

	/**
	 * Displays the input Piece with the other taken Pieces of the same color
	 * 
	 * @param piece
	 *            The Piece which was taken
	 */
	private void addToTakenPieces(Piece piece) {
		ImageView takenPieceView = new ImageView(this);
		int resId = PieceImages.getId(piece);
		takenPieceView.setImageResource(resId);
		if (piece.getColor() == Color.WHITE) {
			whiteTakenAdapter.addPiece(piece);
			whiteTakenPieces.setAdapter(whiteTakenAdapter);
		} else {
			blackTakenAdapter.addPiece(piece);
			blackTakenPieces.setAdapter(blackTakenAdapter);
		}
	}

	/**
	 * Makes the computer's move on the view of the Board and handles passing
	 * the turn.
	 */
	void makeCpuMove() {
		new GetCpuMove().execute();
	}

	/**
	 * Gets the computer player's move and plays it on the Board and handles
	 * passing the turn. This is an expensive call so it is run on a background
	 * Thread instead of the UI thread. This also allows the computer player's
	 * timer to update while this call is executing.
	 * 
	 */
	private class GetCpuMove extends AsyncTask<Void, Void, Move> {

		/**
		 * Gets the computer player's Move
		 * 
		 * @return The computer player's Move
		 */
		@Override
		protected Move doInBackground(Void... params) {
			Board board = adapter.getBoard();
			return player.negaMaxMove(board.clone());
		}

		/**
		 * Plays the computer player's Move on the Board and handles passing the
		 * turn if it has not yet ran out of time.
		 */
		@Override
		protected void onPostExecute(Move result) {
			super.onPostExecute(result);
			if (!isOutOfTime) {
				if (result == null) {
					showGameOver();
				} else {
					passTurn(result);
				}
			}
		}
	}

	/**
	 * Toggles the boolean value of isFirstClick.
	 */
	void toggleClick() {
		isFirstClick = !isFirstClick;
	}

	/**
	 * Is this the first click on the chess board?
	 * 
	 * @return Is this the first click on the chess board?
	 */
	boolean isFirstClick() {
		return isFirstClick;
	}

	/**
	 * Sets the Piece which is clicked on. The next click will attempt to move
	 * this Piece to the clicked Square
	 * 
	 * @param activePiece
	 *            The Piece which will attempt to move to the next clicked
	 *            square on the Board
	 */
	void setActivePiece(Piece activePiece) {
		this.activePiece = activePiece;
	}

	/**
	 * Gets the Piece which is currently selected.
	 * 
	 * @return The Piece which will attempt to move to the next clicked square
	 *         on the Board
	 */
	Piece getActivePiece() {
		return activePiece;
	}

	/**
	 * Is either play out of time?
	 * 
	 * @return true if one of the players has no time left and false otherwise
	 */
	boolean isOutOfTime() {
		return isOutOfTime;
	}

	/**
	 * Asks the user which Piece to promote their Pawn to.
	 * 
	 * @param color
	 *            The Color of the Pawn which is promoting
	 * @return The Piece which the user choice to Promote their Piece to
	 */
	Piece askPromotion(Color color) {
		return new Queen(color);
	}

	/**
	 * Is it the computer's turn to move?
	 * @return true if its the computer's turn to move and false otherwise
	 */
	boolean isCpuMove() {
		return isCpuPlayer && cpuColor == activeTimer;
	}	
}
