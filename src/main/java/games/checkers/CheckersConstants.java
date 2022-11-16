package games.checkers;

import games.checkers.components.Piece;

import java.util.ArrayList;

public class CheckersConstants {
    public static final ArrayList<Piece> playerMapping = new ArrayList<Piece>() {{
        add(new Piece("x"));    // black piece
        add(new Piece("o"));    // white piece
    }};

    public static final String emptyCell = ".";
}
