package games.checkers;

import core.AbstractGameState;
import core.AbstractParameters;
import core.components.Component;
import core.interfaces.IStateHeuristic;
import core.turnorders.AlternatingTurnOrder;
import games.GameType;
import games.dotsboxes.DotsAndBoxesHeuristic;

import java.util.List;

public class CheckersGameState extends AbstractGameState {

    //TODO: implement methods

    IStateHeuristic heuristic;
    public CheckersGameState(AbstractParameters gameParameters, int nPlayers) {
        super(gameParameters, new AlternatingTurnOrder(nPlayers), GameType.Checkers);
    }

    @Override
    protected List<Component> _getAllComponents() {
        return null;
    }

    @Override
    protected AbstractGameState _copy(int playerId) {
        return null;
    }

    @Override
    protected double _getHeuristicScore(int playerId) {
        if (heuristic == null) { // lazy initialization
            heuristic = new DotsAndBoxesHeuristic();
        }
        return heuristic.evaluateState(this, playerId);
    }

    @Override
    public double getGameScore(int playerId) {
        return 0;
    }

    @Override
    protected void _reset() {

    }

    @Override
    protected boolean _equals(Object o) {
        return false;
    }
}
