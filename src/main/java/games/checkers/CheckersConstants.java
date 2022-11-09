package games.checkers;

import core.components.Token;

import java.util.ArrayList;

public class CheckersConstants {
    public static final ArrayList<Token> playerMapping = new ArrayList<Token>() {{
        add(new Token("x"));    // black piece
        add(new Token("o"));    // white piece
    }};

    public static final String emptyCell = ".";
}
