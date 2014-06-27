package com.kdoherty.androidchess.test;

import com.kdoherty.chess.Board;
import com.kdoherty.chess.Color;
import com.kdoherty.engine.CpuPlayer;

public class CpuMoveTest extends BaseTest {
	
	CpuPlayer blackPlayer = CpuPlayer.getInstance(Color.BLACK);
	
	public void testMateSenerioOne() {
		Board board = fillWithTestMate(0);
		System.out.println(blackPlayer.negaMaxMove(board));
	}
	
	public void testMateSenerioTwo() {
		Board board = fillWithTestMate(1);
		System.out.println(blackPlayer.negaMaxMove(board));
	}
	
	public void testMateSenerioThree() {
		Board board = fillWithTestMate(2);
		System.out.println(blackPlayer.negaMaxMove(board));
	}
	
	public void testMateSenerioFour() {
		Board board = fillWithTestMate(3);
		System.out.println(blackPlayer.negaMaxMove(board));
	}
	
	public void testMateSenerioFive() {
		Board board = fillWithTestMate(4);
		System.out.println(blackPlayer.negaMaxMove(board));
	}
	
	public void testMateSenerioSix() {
		Board board = fillWithTestMate(5);
		System.out.println(blackPlayer.negaMaxMove(board));
	}
	
	public void testMateSenerioSeven() {
		Board board = fillWithTestMate(6);
		System.out.println(blackPlayer.negaMaxMove(board));
	}
	
	public void testMateSenerioEight() {
		Board board = fillWithTestMate(7);
		System.out.println(blackPlayer.negaMaxMove(board));
	}
		
	public void testMateSenerioNine() {
		Board board = fillWithTestMate(8);
		System.out.println(blackPlayer.negaMaxMove(board));
		
	}

}
