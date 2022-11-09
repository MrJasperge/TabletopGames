package games.checkers.gui;

import core.AbstractGameState;
import core.AbstractPlayer;
import core.Game;
import gui.AbstractGUIManager;
import gui.GamePanel;
import players.human.ActionController;

public class CheckersGUIManager extends AbstractGUIManager {

    //TODO: implement methods

    public CheckersGUIManager(GamePanel parent, Game game, ActionController ac) {
        super(parent, ac, 100);
    }

    @Override
    protected void _update(AbstractPlayer player, AbstractGameState gameState) {

    }
}
