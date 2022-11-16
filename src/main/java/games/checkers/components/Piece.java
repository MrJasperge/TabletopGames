package games.checkers.components;

import core.components.Token;

public class Piece extends Token {

    private boolean bKing;

    public Piece(String name) {
        super(name);
        bKing = false;
    }
    public Piece(String name, boolean bKing) {
        super(name);
        this.bKing = bKing;
    }

    public void makeKing() {
        bKing = true;
    }

    public boolean isKing() {
        return bKing;
    }
}
