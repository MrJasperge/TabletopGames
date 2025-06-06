package games.checkers;

import games.checkers.components.Piece;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CheckersConstants {
    public static final ArrayList<Piece> playerMapping = new ArrayList<Piece>() {{
        add(new Piece("x", false));    // black piece
        add(new Piece("o", false));    // white piece
    }};

    public static final String emptyCell = ".";
    public static final boolean DEBUG = false;
}
