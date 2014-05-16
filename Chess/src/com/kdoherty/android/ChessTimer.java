package com.kdoherty.android;

import android.widget.TextView;

public class ChessTimer extends CountDownTimerPausable {

	TextView timerView;
	
	public ChessTimer(long millisInFuture, long countDownInterval,
			TextView timerView) {
		super(millisInFuture, countDownInterval);
		this.timerView = timerView;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		String timerText = millisToString(millisUntilFinished);
		timerView.setText(timerText);
	}

	@Override
	public void onFinish() {
		
	}
	
	/**
	 * Converts milliseconds into a String of format MM:SS where M is minutes
	 * and S is seconds
	 * 
	 * @param millis
	 *            The milliseconds to convert to a String
	 * @return
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

	private String secondsToString(long seconds) {
		if (seconds > 9) {
			return String.valueOf(seconds);
		} else {
			return "0" + String.valueOf(seconds);
		}
	}

}
