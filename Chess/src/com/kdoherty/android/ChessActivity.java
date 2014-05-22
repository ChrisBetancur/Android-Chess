package com.kdoherty.android;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
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
 * This Activity is responsible for displaying the chess board and the player's
 * times. Nothing from this Class is visible to Classes not in the android
 * package.
 * 
 * @author Kevin Doherty
 * 
 */
public class ChessActivity extends Activity {

	// TODO: Get this data from previous Activity
	private static boolean IS_CPU_PLAYER = true;
	private static final Color CPU_COLOR = Color.BLACK;
	private static final int DEPTH = 1;
	/** The AI player if there is one */
	private CpuPlayer player = new CpuPlayer(CPU_COLOR, DEPTH);

	/** Adapter which binds the Board representation to the view of the Board */
	private SquareAdapter adapter;

	/** Used to represent the Board */
	private SquareGridView boardView;

	/**
	 * The timer which is currently ticking down. Also represents whose side it
	 * is to move
	 */
	private Color activeTimer = Color.WHITE;

	/** The starting game time in milliseconds */
	private static final long TIME = 300000; // 5 minutes

	/** The white count down timer */
	private CountDownTimerPausable whiteTimer;

	/** The black count down timer */
	private CountDownTimerPausable blackTimer;

	/** The view displaying the amount of time black has left to move */
	private TextView whiteTimerView;

	/** The view displaying the amount of time black has left to move */
	private TextView blackTimerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_chess);
		initBoard();
		if (IS_CPU_PLAYER && CPU_COLOR == Color.WHITE) {
			makeCpuMove();
		}
		initTimers();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Initializes the board adapter and the board view. It then connects the
	 * adapter to the view.
	 */
	private void initBoard() {
		adapter = new SquareAdapter(this);
		boardView = (SquareGridView) findViewById(R.id.chessboard);
		boardView.setAdapter(adapter);
	}

	/**
	 * Initializes the timers for both white and black and sets them to the
	 * starting time.
	 */
	private void initTimers() {

		blackTimerView = (TextView) findViewById(R.id.blackTimer);
		whiteTimerView = (TextView) findViewById(R.id.whiteTimer);

		String startTime = millisToString(TIME);

		whiteTimerView.setText(startTime);
		blackTimerView.setText(startTime);

		whiteTimer = new CountDownTimerPausable(TIME, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				whiteTimerView.setText(millisToString(millisUntilFinished));
			}

			@Override
			public void onFinish() {
				Toast.makeText(ChessActivity.this, "BLACK WINS!",
						Toast.LENGTH_LONG).show();
			}
		};

		blackTimer = new CountDownTimerPausable(TIME, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				blackTimerView.setText(millisToString(millisUntilFinished));
			}

			@Override
			public void onFinish() {
				Toast.makeText(ChessActivity.this, "WHITE WINS!",
						Toast.LENGTH_LONG).show();
			}
		};
	}

	/**
	 * Converts milliseconds into a String of format MM:SS where M is minutes
	 * and S is seconds
	 * 
	 * @param millis
	 *            The milliseconds to represent as a String
	 * @return The String representation of milliseconds in MM:SS format
	 */
	private String millisToString(long millis) {
		long seconds = millis / 1000;
		if (seconds < 60) {
			return "00:" + secondsToString(seconds);
		}
		long minutes = seconds / 60;
		long remainder = seconds % 60;
		return "0" + String.valueOf(minutes) + ":" + secondsToString(remainder);
	}

	/**
	 * Helper method for millisToString
	 * 
	 * @param seconds
	 *            The seconds to represent as a String
	 * @return The String representation of seconds in SS format
	 */
	private String secondsToString(long seconds) {
		if (seconds > 9) {
			return String.valueOf(seconds);
		} else {
			return "0" + String.valueOf(seconds);
		}
	}

	/**
	 * Pauses the timer which is currently active and starts the opposing
	 * Color's timer.
	 */
	void toggleTimer() {
		if (!getTimer(activeTimer).isPaused()) {
			getTimer(activeTimer).pause();
		}
		activeTimer = activeTimer.opp();
		getTimer(activeTimer).start();
	}

	/**
	 * Helper method for toggle timer. Gets the timer corresponding to the input
	 * color
	 * 
	 * @param color
	 *            The Color of the timer to get
	 * @return The timer corresponding to the input color
	 */
	private CountDownTimerPausable getTimer(Color color) {
		return color == Color.WHITE ? whiteTimer : blackTimer;
	}

	/**
	 * Updates the UI to represent the input Board
	 * 
	 * @param board
	 *            The Board to represent in the UI
	 */
	void refreshAdapter(Board board) {
		adapter = new SquareAdapter(this, board);
		boardView.setAdapter(adapter);
	}

	/**
	 * Asks the user which Piece to promote their Pawn to.
	 * 
	 * @param color
	 *            The Color of the Pawn which is promoting
	 * @return The Piece which the user choice to Promote their Piece to
	 */
	Piece askPromotion(Color color) {
		// TODO: Need pop up to determine type of Piece to promote to
		return new Queen(color);
	}

	/**
	 * Displays a message to the User that the game is over
	 */
	void showGameOver() {
		Board board = adapter.getBoard();
		Color sideToMove = board.getSideToMove();
		if (board.isCheckMate(sideToMove)) {
			Toast.makeText(this, "CHECKMATE! " + sideToMove.opp() + " WINS",
					Toast.LENGTH_LONG).show();
		} else if (board.isDraw(sideToMove)) {
			Toast.makeText(this, "DRAW!", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * Gets the computer player's move and plays it on the Board and handles
	 * passing the turn. This is an expensive call so it is run on a background
	 * Thread.
	 * 
	 * @author Kevin Doherty
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
			return player.negaMaxMove(board);
		}

		/**
		 * Plays the computer player's Move on the Board and handles passing the
		 * turn.
		 */
		@Override
		protected void onPostExecute(Move result) {
			super.onPostExecute(result);
			if (result == null) {
				Toast.makeText(ChessActivity.this, "YOU WIN, GOOD GAME!",
						Toast.LENGTH_LONG).show();
				return;
			}
			Board board = adapter.getBoard();
			result.make(board);
			refreshAdapter(board);
			toggleTimer();
			board.toggleSideToMove();
			board.addMove(result);
			if (board.isGameOver()) {
				showGameOver();
			}
		}

	}

	/**
	 * Makes the computer's move on the view of the Board and handles passing
	 * the turn.
	 */
	void makeCpuMove() {
		new GetCpuMove().execute();
	}

}
