package com.kdoherty.engine;

import com.kdoherty.chess.Board;


public interface Evaluable {

	public int evaluate(Board board);

}
