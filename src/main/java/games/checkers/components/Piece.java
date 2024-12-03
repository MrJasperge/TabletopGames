package games.checkers.components;

import core.components.Token;
import utilities.Pair;

public class Piece extends Token {

    private boolean canCaptureBackwards;
    private boolean canJumpAsKing;
    private boolean bKing;
    private Pair<Integer, Integer> currentPosition;

    public Piece(String name) {
        super(name);
        this.bKing = false;
        this.currentPosition = new Pair<>(0, 0);
    }
    public Piece(String name, boolean bKing) {
        super(name);
        this.bKing = bKing;
        this.currentPosition = new Pair<>(0, 0);
    }

    public Piece(String name, boolean bKing, Pair<Integer, Integer> position) {
        super(name);
        this.bKing = bKing;
        this.currentPosition = position;
    }

    public void makeKing() {
        bKing = true;
    }
    public boolean isKing() {
        return bKing;
    }

    public void setCurrentPosition(Pair<Integer, Integer> newPosition) {
        currentPosition = newPosition;
    }
    public void setCurrentPosition(int a, int b) {
        currentPosition.a = a;
        currentPosition.b = b;
    }
    public Pair<Integer, Integer> getCurrentPosition() {
        return currentPosition;
    }
    public int getCurrentA() {
        return currentPosition.a;
    }
    public int getCurrentB() {
        return currentPosition.b;
    }
}
