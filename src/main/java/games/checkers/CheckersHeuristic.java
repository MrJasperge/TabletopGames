package games.checkers;

import core.AbstractGameState;
import core.AbstractParameters;
import core.interfaces.IStateHeuristic;
import evaluation.TunableParameters;

public class CheckersHeuristic extends TunableParameters implements IStateHeuristic {

    //TODO: implement methods

    @Override
    protected AbstractParameters _copy() {
        return null;
    }

    @Override
    protected boolean _equals(Object o) {
        return false;
    }

    @Override
    public double evaluateState(AbstractGameState gs, int playerId) {
        return 0;
    }

    @Override
    public Object instantiate() {
        return null;
    }

    @Override
    public void _reset() {

    }
}
