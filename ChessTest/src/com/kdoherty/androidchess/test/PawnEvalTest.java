package com.kdoherty.androidchess.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Pawn;
import com.kdoherty.engine.PawnEval;

public class PawnEvalTest extends BaseTest {

	private Method isIsolated;

	/**
	 * Using reflection to access private methods for testing. Just testing the
	 * public evaluate method is too difficult and would likely result in bad
	 * tests.
	 */
	@Override
	public void setUp() throws NoSuchMethodException {
		Class<? extends PawnEval> pawnEvalClass = PawnEval.class;

		isIsolated = pawnEvalClass.getDeclaredMethod("isIsolated",
				(Class<?>[]) null);
		isIsolated.setAccessible(true);

	}

	public void testIsIsolatedLeft() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Board board = leftIsolatedBoard();

		Pawn whitePawn = (Pawn) board.getOccupant(4, 0);
		Pawn blackPawn = (Pawn) board.getOccupant(3, 0);

		PawnEval whitePawnEval = new PawnEval(board, whitePawn);
		PawnEval blackPawnEval = new PawnEval(board, blackPawn);

		boolean whitePawnIsolated = (Boolean) isIsolated.invoke(whitePawnEval,
				(Object[]) null);
		boolean blackPawnIsolated = (Boolean) isIsolated.invoke(blackPawnEval,
				(Object[]) null);

		assertTrue(whitePawnIsolated);
		assertTrue(blackPawnIsolated);
	}

	public void testIsIsolatedRight() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Board board = rightIsolatedBoard();

		Pawn whitePawn = (Pawn) board.getOccupant(4, 7);
		Pawn blackPawn = (Pawn) board.getOccupant(3, 7);

		PawnEval whitePawnEval = new PawnEval(board, whitePawn);
		PawnEval blackPawnEval = new PawnEval(board, blackPawn);

		boolean whitePawnIsolated = (Boolean) isIsolated.invoke(whitePawnEval,
				(Object[]) null);
		boolean blackPawnIsolated = (Boolean) isIsolated.invoke(blackPawnEval,
				(Object[]) null);

		assertTrue(whitePawnIsolated);
		assertTrue(blackPawnIsolated);
	}

	public void testIsIsolatedMiddle() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Board board = middleIsolatedBoard();

		Pawn whitePawn = (Pawn) board.getOccupant(4, 4); // NULL
		Pawn blackPawn = (Pawn) board.getOccupant(3, 4); // NULL

		PawnEval whitePawnEval = new PawnEval(board, whitePawn);
		PawnEval blackPawnEval = new PawnEval(board, blackPawn);

		boolean whitePawnIsolated = (Boolean) isIsolated.invoke(whitePawnEval,
				(Object[]) null);
		boolean blackPawnIsolated = (Boolean) isIsolated.invoke(blackPawnEval,
				(Object[]) null);

		assertTrue(whitePawnIsolated);
		assertTrue(blackPawnIsolated);
	}

	public void testIsIsolatedNegative() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Board board = notIsolatedBoard();

		Pawn whitePawn = (Pawn) board.getOccupant(4, 4); // NULL
		Pawn blackPawn = (Pawn) board.getOccupant(3, 4); // NULL

		PawnEval whitePawnEval = new PawnEval(board, whitePawn);
		PawnEval blackPawnEval = new PawnEval(board, blackPawn);

		boolean whitePawnIsolated = (Boolean) isIsolated.invoke(whitePawnEval,
				(Object[]) null);
		boolean blackPawnIsolated = (Boolean) isIsolated.invoke(blackPawnEval,
				(Object[]) null);

		assertFalse(whitePawnIsolated);
		assertFalse(blackPawnIsolated);
	}
}
