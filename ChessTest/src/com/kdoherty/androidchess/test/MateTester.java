package com.kdoherty.androidchess.test;

import java.util.List;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.chess.Move;
import com.kdoherty.engine.MateSolver;

public class MateTester extends BaseTest {
	
	public void testMateSenerioOne() {
		Board board = fillWithTestMate(0);
		assertEquals(MateSolver.findMateUpToN(board, Color.WHITE, 5).toString(), "[rc7, rd8]");
	}
	
	public void testMateSenerioTwo() {
		Board board = fillWithTestMate(1);
		assertEquals(MateSolver.findMateUpToN(board, Color.WHITE, 5).toString(), "[rh3]");
	}
	
	public void testMateSenerioThree() {
		Board board = fillWithTestMate(2);
		List<Move> mateMoves = MateSolver.findMateUpToN(board, Color.WHITE, 5);
		assertTrue(mateMoves.toString().equals("[qh7, rh3]") || mateMoves.toString().equals("[qf6, qg7]"));
	}
	
	public void testMateSenerioFour() {
		Board board = fillWithTestMate(3);
		assertEquals(MateSolver.findMateUpToN(board, Color.WHITE, 5).toString(), "[rh6, rh7]");
	}
	
	public void testMateSenerioFive() {
		Board board = fillWithTestMate(4);
		List<Move> mateMoves = MateSolver.findMateUpToN(board, Color.WHITE, 5);
		assertTrue(mateMoves.toString().equals("[bb3, qd6]") || mateMoves.toString().equals("[bb3, re7]"));
	}
	
	public void testMateSenerioSix() {
		Board board = fillWithTestMate(5);
		List<Move> mateMoves = MateSolver.findMateUpToN(board, Color.WHITE, 5);
		assertEquals(MateSolver.findMateUpToN(board, Color.WHITE, 5).toString(), "[qg7, rh6, ne7]");
	}
	
	public void testMateSenerioSeven() {
		Board board = fillWithTestMate(6);
		List<Move> mateMoves = MateSolver.findMateUpToN(board, Color.WHITE, 5);
		assertTrue(mateMoves.toString().equals("[qf6, re8, rf8]") || mateMoves.toString().equals("[qf6, qf8, rg8]"));
	}
	
	public void testMateSenerioEight() {
		Board board = fillWithTestMate(7);
		assertEquals(MateSolver.findMateUpToN(board, Color.WHITE, 5).toString(), "[nf5, qg7, qh6, qg6]");
	}
		
	public void testMateSenerioNine() {
		Board board = fillWithTestMate(8);
		assertEquals(MateSolver.findMateUpToN(board, Color.WHITE, 5).toString(), "[ba4, pb3, bb5, rg4, ne3]");
	}
}
