package games.checkers.components;

import core.CoreConstants;
import core.components.BoardNode;
import core.components.Token;
import dev.langchain4j.service.V;
import utilities.Pair;
import utilities.Vector2D;

import java.util.HashMap;
import java.util.Objects;

public class Piece extends BoardNode {

    protected boolean bKing;
    protected String name;
    protected Vector2D position;

    public Piece(String name) {
        super(name);
        this.name = name;
        this.bKing = false;
        this.position = new Vector2D(0, 0);

    }
    public Piece(String name, boolean bKing) {
        super(name);
        this.name = name;
        this.bKing = bKing;
        this.position = new Vector2D(0, 0);
    }

    public Piece(String name, boolean bKing, Vector2D position) {
        super(name);
        this.name = name;
        this.bKing = bKing;
        this.position = position;
    }

    public void makeKing() {
        this.bKing = true;
    }

    public boolean isKing() {
        return bKing;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Piece copy() {
        Piece copy = new Piece(name, bKing);
        copyComponentTo(copy);
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, bKing);
    }

    @Override
    public String toString() {
        return name;
    }

    public Vector2D getPiecePosition() {
        return position;
    }

    public void setPiecePosition(Vector2D position) {
        this.position = position;
    }
}
