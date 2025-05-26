package games.checkers;

import core.AbstractGameState;
import core.AbstractParameters;
import core.components.Component;
import core.components.GridBoard;
import games.GameType;
import games.checkers.components.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckersGameState extends AbstractGameState {

    GridBoard gridBoard;
    private boolean skipTurn = false;

    public CheckersGameState(AbstractParameters gameParameters, int nPlayers) {
        super(gameParameters, nPlayers);
    }

    public GridBoard getGridBoard() {
        return this.gridBoard;
    }

    @Override
    protected GameType _getGameType() {
        return GameType.Checkers;
    }

    @Override
    protected List<Component> _getAllComponents() {
        return new ArrayList<>() {{
            add(gridBoard);
        }};
    }

    @Override
    protected AbstractGameState _copy(int playerId) {
        CheckersGameState copy = new CheckersGameState(gameParameters.copy(), getNPlayers());
        copy.gridBoard = gridBoard.copy();
        copy.skipTurn = skipTurn;

        // Copy the grid board
        for (int x = 0; x < gridBoard.getWidth(); x++) {
            for (int y = 0; y < gridBoard.getHeight(); y++) {
                Piece piece = (Piece) gridBoard.getElement(x, y);
                if (piece != null) {
                    copy.gridBoard.setElement(x, y, piece.copy());
                }
            }
        }


        return copy;
    }

    public int getNextPlayer() {
        int nextPlayer = 1 - getCurrentPlayer();
        if (skipTurn) {
            nextPlayer = getCurrentPlayer();
        }
        return nextPlayer;
    }

    public boolean isSkipTurn() {
        return skipTurn;
    }

    public void setSkipTurn(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }


    @Override
    protected double _getHeuristicScore(int playerId) {
        return new CheckersHeuristic().evaluateState(this, playerId);
    }

    @Override
    public double getGameScore(int playerId) {
        return playerResults[playerId].value;
    }

    protected void _reset() {
        gridBoard = null;
    }

    @Override
    protected boolean _equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckersGameState that)) return false;
        return Objects.equals(gridBoard, that.gridBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gridBoard);
    }

    @Override
    public String toString() {
        String sb = Objects.hash(gameParameters) + "|" +
                Objects.hash(getAllComponents()) + "|" +
                Objects.hash(gameStatus) + "|" +
                Objects.hash(gamePhase) + "|*|" +
                Objects.hash(gridBoard);
        return sb;
    }

    public void printToConsole() {
        System.out.println(gridBoard.toString());
    }

}
