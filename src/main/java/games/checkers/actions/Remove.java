package games.checkers.actions;

import core.AbstractGameState;
import core.actions.AbstractAction;
import games.checkers.CheckersConstants;
import games.checkers.CheckersGameState;
import games.checkers.components.Piece;
import utilities.Pair;

import java.util.ArrayList;
import java.util.Objects;

public class Remove extends AbstractAction {

    private final ArrayList<Pair<Integer, Integer>> piecesToRemove;

    public Remove(ArrayList<Pair<Integer, Integer>> pPieces) {
        this.piecesToRemove = pPieces;
    }

    @Override
    public boolean execute(AbstractGameState gs) {
        CheckersGameState chgs = (CheckersGameState) gs;

        for (Pair<Integer, Integer> piece : piecesToRemove)
            chgs.getGridBoard().setElement(piece.a, piece.b, new Piece(CheckersConstants.emptyCell));

        return true;
    }

    @Override
    public AbstractAction copy() {
        return new Remove(new ArrayList<>(piecesToRemove));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Remove && Objects.equals(piecesToRemove, ((Remove) obj).piecesToRemove);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piecesToRemove);
    }

    @Override
    public String getString(AbstractGameState gameState) {
        return "Remove " + piecesToRemove.size() + " pieces";
    }
}
