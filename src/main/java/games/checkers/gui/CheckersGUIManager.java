package games.checkers.gui;

import core.AbstractGameState;
import core.AbstractPlayer;
import core.Game;
import core.actions.AbstractAction;
import core.actions.SetGridValueAction;
import core.components.Token;
import games.checkers.CheckersGameState;
import games.tictactoe.TicTacToeConstants;
import gui.AbstractGUIManager;
import gui.GamePanel;
import gui.ScreenHighlight;
import players.human.ActionController;
import utilities.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CheckersGUIManager extends AbstractGUIManager {

    //TODO: improve methods

    CheckersBoardView view;

    public CheckersGUIManager(GamePanel parent, Game game, ActionController ac) {
        super(parent, ac, 1);
        if (game == null) return;

        // Checkers GameState
        CheckersGameState chgs = (CheckersGameState) game.getGameState();
        view = new CheckersBoardView(chgs.getGridBoard());

        // Set width and height of display
        this.width = Math.max(defaultDisplayWidth, defaultItemSize * chgs.getGridBoard().getWidth());
        this.height = Math.max(defaultDisplayHeight, defaultItemSize * chgs.getGridBoard().getHeight());

        JPanel infoPanel = createGameStateInfoPanel("Checkers", chgs, width, defaultInfoPanelHeight);
        JComponent actionPanel = createActionPanel(new ScreenHighlight[]{view},
                width, defaultActionPanelHeight, true);

        parent.setLayout(new BorderLayout());
        parent.add(view, BorderLayout.CENTER);
        parent.add(infoPanel, BorderLayout.NORTH);
        parent.add(actionPanel, BorderLayout.SOUTH);
        parent.setPreferredSize(new Dimension(width, height + defaultActionPanelHeight + defaultInfoPanelHeight + defaultCardHeight + 20));
        parent.revalidate();
        parent.setVisible(true);
        parent.repaint();
    }

    @Override
    protected void updateActionButtons(AbstractPlayer player, AbstractGameState gameState) {
        if (gameState.getGameStatus() == Utils.GameResult.GAME_ONGOING) {
            List<core.actions.AbstractAction> actions = player.getForwardModel().computeAvailableActions(gameState);
            ArrayList<Rectangle> highlight = view.getHighlight();

            int start = actions.size();
            if (highlight.size() > 0) {
                Rectangle r = highlight.get(0);
                for (AbstractAction abstractAction : actions) {
                    SetGridValueAction<Token> action = (SetGridValueAction<Token>) abstractAction;
                    if (action.getX() == r.x/defaultItemSize && action.getY() == r.y/defaultItemSize) {
                        actionButtons[0].setVisible(true);
                        actionButtons[0].setButtonAction(action, "Play " + TicTacToeConstants.playerMapping.get(player.getPlayerID()));
                        break;
                    }
                }
            } else {
                actionButtons[0].setVisible(false);
                actionButtons[0].setButtonAction(null, "");
            }
        }
    }

    @Override
    protected void _update(AbstractPlayer player, AbstractGameState gameState) {
        if (gameState != null) {
            view.updateComponent(((CheckersGameState)gameState).getGridBoard());
        }
    }
}
