package games.checkers;

import core.AbstractGameState;
import core.AbstractParameters;
import core.components.Component;
import core.components.GridBoard;
import core.interfaces.IGridGameState;
import core.turnorders.AlternatingTurnOrder;
import games.GameType;
import games.checkers.components.Piece;
import utilities.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckersGameState extends AbstractGameState implements IGridGameState<Piece> {

    GridBoard<Piece> gridBoard;

    public CheckersGameState(AbstractParameters gameParameters, int nPlayers) {
        super(gameParameters, new AlternatingTurnOrder(nPlayers), GameType.Checkers);
    }

    @Override
    protected List<Component> _getAllComponents() {
        return new ArrayList<Component>() {{
            add(gridBoard);
        }};
    }

    @Override
    protected AbstractGameState _copy(int playerId) {
        CheckersGameState chgs = new CheckersGameState(gameParameters.copy(), getNPlayers());
        chgs.gridBoard = gridBoard.copy();
        return chgs;
    }

    @Override
    protected double _getHeuristicScore(int playerId) {
        return new CheckersHeuristic().evaluateState(this, playerId);
    }

    @Override
    public double getGameScore(int playerId) {
        return 0;
    }

    @Override
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
        sb.append(Objects.hash(turnOrder)).append("|");
        sb.append(Objects.hash(getAllComponents())).append("|");
        sb.append(Objects.hash(gameStatus)).append("|");
        sb.append(Objects.hash(gamePhase)).append("|*|");
        sb.append(Objects.hash(gridBoard));
        return sb.toString();
    }

    @Override
    public GridBoard<Piece> getGridBoard() {
        return gridBoard;
    }

    @Override
    public void printToConsole() {
        System.out.println(gridBoard.toString());
    }

    public class Capture extends games.checkers.actions.Capture {
        public Capture(int playerID, Pair<Integer, Integer> fromCell, Pair<Integer, Integer> toCell, ArrayList<Pair<Integer, Integer>> capturedCells, boolean endOfTurn) {
            super(playerID, fromCell, toCell, capturedCells, endOfTurn);
        }
    }
}
