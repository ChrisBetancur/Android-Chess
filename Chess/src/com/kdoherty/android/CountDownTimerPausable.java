package com.kdoherty.android;

import android.os.CountDownTimer;

/**
 * This class uses the native CountDownTimer to create a timer which could be
 * paused and then started again from the previous point. See
 * http://stackoverflow.com/a/10055480/3258892
 */
abstract class CountDownTimerPausable {

	private long millisInFuture = 0;
	private long countDownInterval = 0;
	private long millisRemaining = 0;

	private CountDownTimer countDownTimer = null;

	private boolean isPaused = true;

	public CountDownTimerPausable(long millisInFuture, long countDownInterval) {
		super();
		this.millisInFuture = millisInFuture;
		this.countDownInterval = countDownInterval;
		this.millisRemaining = this.millisInFuture;
	}

	private void createCountDownTimer() {
		countDownTimer = new CountDownTimer(millisRemaining, countDownInterval) {

			@Override
			public void onTick(long millisUntilFinished) {
				millisRemaining = millisUntilFinished;
				CountDownTimerPausable.this.onTick(millisUntilFinished);
			}

			@Override
			public void onFinish() {
				CountDownTimerPausable.this.onFinish();
			}
		};
	}

	/**
	 * Callback fired on regular interval.
	 * 
	 * @param millisUntilFinished
	 *            The amount of time until finished.
	 */
	public abstract void onTick(long millisUntilFinished);

	/**
	 * Callback fired when the time is up.
	 */
	public abstract void onFinish();

	/**
	 * Cancel the countdown.
	 */
	public final void cancel() {
		if (countDownTimer != null) {
			countDownTimer.cancel();
		}
		this.millisRemaining = 0;
	}

	/**
	 * Start or Resume the count down.
	 * 
	 * 
	 * @return CountDownTimerPausable current instance
	 */
	public synchronized final CountDownTimerPausable start() {
		if (isPaused) {
			createCountDownTimer();
			countDownTimer.start();
			isPaused = false;
		}
		return this;
	}

	/**
	 * Pauses the CountDownTimerPausable, so it could be resumed / started later
	 * from the same point where it was paused.
	 * 
	 * @throws IllegalStateException
	 */
	public void pause() throws IllegalStateException {
		if (!isPaused) {
			countDownTimer.cancel();
		} else {
			throw new IllegalStateException(
					"CountDownTimerPausable is already in pause state, start counter before pausing it.");
		}
		isPaused = true;
	}

	public boolean isPaused() {
		return isPaused;
	}
}
