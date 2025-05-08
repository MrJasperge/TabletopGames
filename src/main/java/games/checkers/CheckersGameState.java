package games.checkers;

import core.AbstractGameState;
import core.AbstractParameters;
import core.components.BoardNode;
import core.components.Component;
import core.components.GridBoard;
import games.GameType;
import games.checkers.components.Piece;
import utilities.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckersGameState extends AbstractGameState {

//    public CheckersBoard checkersBoard;
    GridBoard gridBoard;

    public CheckersGameState(AbstractParameters gameParameters, int nPlayers) {
        super(gameParameters, nPlayers);
    }

    public GridBoard getGridBoard() {
        return this.gridBoard;
    }

//    public CheckersBoard getCheckersBoard() {
//        return checkersBoard;
//    }

    @Override
    protected GameType _getGameType() {
        return GameType.Checkers;
    }

    @Override
    protected List<Component> _getAllComponents() {
        return new ArrayList<Component>() {{
            add(gridBoard);
        }};
    }

    @Override
    protected AbstractGameState _copy(int playerId) {
        CheckersGameState copy = new CheckersGameState(gameParameters.copy(), getNPlayers());
        copy.gridBoard = gridBoard.copy();

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

    @Override
    protected double _getHeuristicScore(int playerId) {
        return new CheckersHeuristic().evaluateState(this, playerId);
    }

    @Override
    public double getGameScore(int playerId) {
        return 0;
    }

    protected void _reset() {
        gridBoard = null;
    }

    @Override
    protected boolean _equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckersGameState)) return false;
        CheckersGameState that = (CheckersGameState) o;
        return Objects.equals(gridBoard, that.gridBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gridBoard);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Objects.hash(gameParameters)).append("|");
//        sb.append(Objects.hash(turnOrder)).append("|");
        sb.append(Objects.hash(getAllComponents())).append("|");
        sb.append(Objects.hash(gameStatus)).append("|");
        sb.append(Objects.hash(gamePhase)).append("|*|");
        sb.append(Objects.hash(gridBoard));
        return sb.toString();
    }

    public void printToConsole() {
        System.out.println(gridBoard.toString());
    }

    public class Capture extends games.checkers.actions.Capture {
        public Capture(int playerID, Pair<Integer, Integer> fromCell, Pair<Integer, Integer> toCell, ArrayList<Pair<Integer, Integer>> capturedCells, boolean endOfTurn) {
            super(playerID, fromCell, toCell, capturedCells, endOfTurn);
        }
    }

    public int getNumberOfPieces(int player) {

        return 0;
    }
}
