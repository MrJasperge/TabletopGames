package games.checkers;

import core.AbstractForwardModel;
import core.AbstractGameState;
import core.AbstractPlayer;
import core.Game;
import games.GameType;

import java.util.List;

public class CheckersGame extends Game {

    //TODO: implement methods

    public CheckersGame(GameType type, List<AbstractPlayer> players, AbstractForwardModel realModel, AbstractGameState gameState) {
        super(type, players, realModel, gameState);
    }
}
