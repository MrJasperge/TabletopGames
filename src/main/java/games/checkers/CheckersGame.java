package games.checkers;

import core.AbstractPlayer;
import core.Game;
import games.GameType;
import players.simple.RandomPlayer;

import java.util.ArrayList;
import java.util.List;

public class CheckersGame extends Game {

    public CheckersGame(CheckersGameParameters params) {
        super(GameType.Checkers, new CheckersForwardModel(), new CheckersGameState(params, 2));
    }
    public CheckersGame(List<AbstractPlayer> agents, CheckersGameParameters params) {
        super(GameType.Checkers, agents, new CheckersForwardModel(), new CheckersGameState(params, agents.size()));
    }

    public static void main(String[] args) {
        ArrayList<AbstractPlayer> agents = new ArrayList<>();
        agents.add(new RandomPlayer());
        agents.add(new RandomPlayer());

        CheckersGameParameters params = new CheckersGameParameters();
        Game game = new CheckersGame(agents, params);
        game.run();
    }
}
