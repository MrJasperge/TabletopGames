package games.checkers;

import core.AbstractForwardModel;
import core.AbstractGameState;
import core.actions.AbstractAction;

import java.util.List;

public class CheckersForwardModel extends AbstractForwardModel {

    //TODO: implement methods

    @Override
    protected void _setup(AbstractGameState firstState) {

    }

    @Override
    protected void _next(AbstractGameState currentState, AbstractAction action) {

    }

    @Override
    protected List<AbstractAction> _computeAvailableActions(AbstractGameState gameState) {
        return null;
    }

    @Override
    protected AbstractForwardModel _copy() {
        return null;
    }
}
