package com.kdoherty.androidchess.test;

import java.util.List;

import android.util.Log;
import android.util.SparseArray;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;
import com.kdoherty.engine.MateSolver;

public class MateTester extends BaseTest {
		
		
		private static final int NUM_MATE_TESTS = 4;
		
		private static final SparseArray<String> expectedMoves;
		
		static {
			expectedMoves = new SparseArray<String>();
			expectedMoves.put(0, "[rc7, rd8]"); // verified
			expectedMoves.put(1, "[rh3]"); // verified
			expectedMoves.put(2, "[qh7, rh3]"); // verified
			expectedMoves.put(3, "[rh6, rh7]"); // verified
		}

//		public void testMateSolver() {
//			Board b;
//			long startTime = System.currentTimeMillis();
//			int numFails = 0;
//			boolean allPass = true;
//			for (int i = 0; i < NUM_MATE_TESTS; i++) {
//				long testStartTime = System.currentTimeMillis();
//				b = fillWithTestMate(i);
//				List<Move> mateMoves = MateSolver.findMateUpToN(b, Color.WHITE, 5);
//				
//				if (mateMoves == null || mateMoves.size() == 0) {
//					Log.d("mateTest", "Test mate number " + i + " failed testing");
//					allPass = false;
//					numFails++;
//					//assertNotNull(mateMoves);
//					//assertFalse(mateMoves.size() == 0);
//				}
//				Log.d("mateTest", "mateMoves are: " + mateMoves);
//				long testEndTime = System.currentTimeMillis();
//				long testTotalTime = testEndTime - testStartTime;
//				Log.d("mateTest", "Test mate number " + i + " took "
//						+ testTotalTime / 1000.0 + " seconds");
//				//assertEquals(mateMoves.toString(), expectedMoves.get(i));
//				b.clearBoard();
//			}
//			if (allPass) {
//				Log.d("mateTest", "All tests passed!");
//			} else {
//				Log.d("mateTest", "Tests Complete: " + numFails + " tests failed");
//			}
//			long endTime = System.currentTimeMillis();
//			long totalTime = endTime - startTime;
//			Log.d("mateTest", "Total execution time: " + totalTime / 1000.0
//					+ " seconds");
//		}
}
