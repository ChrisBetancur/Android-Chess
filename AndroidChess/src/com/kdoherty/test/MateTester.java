package com.kdoherty.test;

import android.util.Log;

import com.kdoherty.chess.Bishop;
import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.King;
import com.kdoherty.chess.Knight;
import com.kdoherty.chess.Pawn;
import com.kdoherty.chess.Queen;
import com.kdoherty.chess.Rook;
import com.kdoherty.engine.MateSolver;


public class MateTester {
	
	
	private static int NUM_MATE_TESTS = 9;

	public void fillWithTestMate(Board b, int x) {
		Pawn p1 = new Pawn(Color.WHITE);
		Pawn p2 = new Pawn(Color.WHITE);
		Pawn p3 = new Pawn(Color.WHITE);
		Pawn p4 = new Pawn(Color.WHITE);
		Pawn p5 = new Pawn(Color.WHITE);
		Pawn p6 = new Pawn(Color.WHITE);
		// Pawn p7 = new Pawn(Color.WHITE);
		// Pawn p8 = new Pawn(Color.WHITE);
		Pawn p9 = new Pawn(Color.BLACK);
		Pawn p10 = new Pawn(Color.BLACK);
		Pawn p11 = new Pawn(Color.BLACK);
		Pawn p12 = new Pawn(Color.BLACK);
		Pawn p13 = new Pawn(Color.BLACK);
		Pawn p14 = new Pawn(Color.BLACK);
		// Pawn p15 = new Pawn(Color.BLACK);
		// Pawn p16 = new Pawn(Color.BLACK);
		Rook r1 = new Rook(Color.WHITE);
		Rook r2 = new Rook(Color.WHITE);
		Rook r3 = new Rook(Color.BLACK);
		Rook r4 = new Rook(Color.BLACK);
		Knight n1 = new Knight(Color.WHITE);
		Knight n2 = new Knight(Color.WHITE);
		Knight n3 = new Knight(Color.BLACK);
		Knight n4 = new Knight(Color.BLACK);
		Bishop b1 = new Bishop(Color.WHITE);
		// Bishop b2 = new Bishop(Color.WHITE);
		Bishop b3 = new Bishop(Color.BLACK);
		Bishop b4 = new Bishop(Color.BLACK);
		Queen q1 = new Queen(Color.WHITE);
		Queen q2 = new Queen(Color.BLACK);
		King k1 = new King(Color.WHITE);
		King k2 = new King(Color.BLACK);
		b.clearBoard();

		switch (x) {

		case (0): {
			b.setPiece('h', 8, k2);
			b.setPiece('c', 5, r1);
			b.setPiece('d', 6, r2);
			b.setPiece('g', 1, k1);
			break;
		}

		case (1): {
			b.setPiece('a', 8, r3);
			b.setPiece('a', 5, q2);
			b.setPiece('g', 8, r4);
			b.setPiece('h', 7, k2);
			b.setPiece('g', 4, r1);
			b.setPiece('f', 5, n1);
			b.setPiece('b', 2, k1);
			b.setPiece('b', 3, p1);
			b.setPiece('c', 3, r2);
			break;
		}
		case (2): {
			b.setPiece('a', 8, r3);
			b.setPiece('a', 5, q2);
			b.setPiece('g', 8, r4);
			b.setPiece('h', 7, p9);
			b.setPiece('h', 8, k2);
			b.setPiece('h', 6, q1);
			b.setPiece('g', 4, r1);
			b.setPiece('f', 5, n1);
			b.setPiece('b', 2, k1);
			b.setPiece('b', 3, p1);
			b.setPiece('c', 3, r2);
			// b.setPiece('h', 7, k2);
			break;
		}
		case (3): {
			b.setPiece('h', 8, k2);
			b.setPiece('f', 8, k1);
			b.setPiece('g', 8, b3);
			b.setPiece('g', 7, p9);
			b.setPiece('h', 7, p10);
			b.setPiece('h', 1, r1);
			b.setPiece('g', 6, p1);
			break;
		}

		case (4): {
			b.setPiece('e', 8, k1);
			b.setPiece('e', 6, k2);
			b.setPiece('f', 4, q1);
			b.setPiece('c', 7, r1);
			b.setPiece('d', 4, p1);
			b.setPiece('c', 4, b1);
			b.setPiece('b', 5, n1);
			b.setPiece('a', 7, r3);
			b.setPiece('h', 7, r4);
			b.setPiece('a', 8, b3);
			b.setPiece('h', 8, b4);
			b.setPiece('f', 7, p9);
			b.setPiece('d', 5, n3);
			break;
		}
		case (5): {
			b.setPiece('a', 8, q2);
			b.setPiece('a', 7, p9);
			b.setPiece('f', 7, p10);
			b.setPiece('g', 7, p11);
			b.setPiece('b', 2, p1);
			b.setPiece('f', 2, p2);
			b.setPiece('g', 3, p3);
			b.setPiece('c', 3, q1);
			b.setPiece('d', 6, r1);
			b.setPiece('f', 8, r3);
			b.setPiece('e', 6, n3);
			b.setPiece('f', 5, n1);
			b.setPiece('f', 1, k1);
			b.setPiece('h', 7, k2);
			break;
		}
		case (6): {
			b.setPiece('a', 7, r3);
			b.setPiece('a', 6, p9);
			b.setPiece('a', 2, p1);
			b.setPiece('b', 8, n3);
			b.setPiece('b', 5, p10);
			b.setPiece('c', 3, p2);
			b.setPiece('d', 7, r4);
			b.setPiece('e', 6, q1);
			b.setPiece('e', 1, r1);
			b.setPiece('f', 8, q2);
			b.setPiece('f', 2, p3);
			b.setPiece('g', 3, r2);
			b.setPiece('g', 2, p4);
			b.setPiece('g', 1, k1);
			b.setPiece('h', 8, k2);
			b.setPiece('h', 7, p11);
			b.setPiece('h', 2, p5);
			break;
		}
		case (7): {
			b.setPiece('a', 2, p1);
			b.setPiece('b', 2, p2);
			b.setPiece('c', 2, p3);
			b.setPiece('e', 2, k1);
			b.setPiece('g', 2, b1);
			b.setPiece('e', 3, n1);
			b.setPiece('f', 1, r1);
			b.setPiece('f', 7, q1);
			b.setPiece('h', 2, q2);
			b.setPiece('h', 4, r3);
			b.setPiece('h', 6, k2);
			b.setPiece('a', 7, p11);
			b.setPiece('b', 7, p12);
			b.setPiece('g', 7, p13);
			b.setPiece('d', 4, p4);
			break;
		}
		case (8): {
			b.setPiece('a', 3, p5);
			b.setPiece('a', 5, p12);
			b.setPiece('a', 6, n4);
			b.setPiece('b', 1, k1);
			b.setPiece('b', 2, p3);
			b.setPiece('b', 5, k2);
			b.setPiece('b', 6, b3);
			b.setPiece('d', 1, b1);
			b.setPiece('d', 4, p6);
			b.setPiece('d', 5, p11);
			b.setPiece('e', 2, n2);
			b.setPiece('e', 6, p10);
			b.setPiece('e', 8, r4);
			b.setPiece('f', 1, n1);
			b.setPiece('f', 6, r3);
			b.setPiece('g', 2, p2);
			b.setPiece('g', 6, r1);
			b.setPiece('h', 5, p1);
			b.setPiece('h', 6, p9);
			b.setPiece('h', 7, q2);
			break;
		}
		case (9): {
			b.setPiece('a', 2, p1);
			b.setPiece('b', 2, p2);
			b.setPiece('c', 2, p3);
			b.setPiece('f', 2, p4);
			b.setPiece('g', 2, p5);
			b.setPiece('h', 2, p6);
			b.setPiece('a', 7, p9);
			b.setPiece('b', 7, p10);
			b.setPiece('c', 7, p11);
			b.setPiece('g', 7, p12);
			b.setPiece('h', 7, p13);
			b.setPiece('a', 1, r1);
			b.setPiece('g', 1, k1);
			b.setPiece('e', 1, r2);
			b.setPiece('g', 5, n1);
			b.setPiece('a', 8, r3);
			b.setPiece('e', 8, r4);
			b.setPiece('e', 2, q1);
			b.setPiece('d', 8, q2);
			b.setPiece('f', 5, n3);
			b.setPiece('g', 8, k2);
			b.setPiece('e', 4, p14);
			break;
		}
		}
	}

	public void runMateTests() {
		Board b = new Board();
		long startTime = System.currentTimeMillis();
		int numFails = 0;
		boolean allPass = true;
		for (int i = 0; i < NUM_MATE_TESTS; i++) {
			long testStartTime = System.currentTimeMillis();
			fillWithTestMate(b, i);
			if (MateSolver.findMateUpToN(b, Color.WHITE, 5) == null) {
				Log.d("MateTests", "Test mate number " + i + " failed testing");
				allPass = false;
				numFails++;
			}
			long testEndTime = System.currentTimeMillis();
			long testTotalTime = testEndTime - testStartTime;
			Log.d("MateTests", "Test mate number " + i + " took "
					+ testTotalTime / 1000.0 + " seconds");
			b.clearBoard();
		}
		if (allPass) {
			Log.d("MateTests", "All tests passed!");
		} else {
			Log.d("MateTests", "Tests Complete: " + numFails + " tests failed");
		}
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Log.d("MateTests", "Total execution time: " + totalTime / 1000.0
				+ " seconds");
	}
}
