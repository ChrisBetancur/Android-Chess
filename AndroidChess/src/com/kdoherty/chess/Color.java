package com.kdoherty.chess;

/**
 * Enumeration for the colors White and Black
 * @author Kevin
 * @version 10/14/2013
 *
 */
public enum Color {

    WHITE, BLACK;

    /**
     * gets the opposite color of this color
     * @return the opposite color of this color
     */
    public Color opp() {
        return this == WHITE ? BLACK : WHITE;
    }
}

