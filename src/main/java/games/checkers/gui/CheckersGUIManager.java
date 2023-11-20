package games.checkers.gui;

import core.AbstractGameState;
import core.AbstractPlayer;
import core.Game;
import core.actions.AbstractAction;
import games.checkers.CheckersGameState;
import games.checkers.actions.Capture;
import games.checkers.actions.Move;
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
        super(parent, ac, 4);
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

            view.setActions(actions);
            ArrayList<Rectangle> moveHighlight = view.getMoveHighlight();

            List<AbstractAction> actionList = new ArrayList<>();

            int start = actions.size();
            if (highlight.size() > 0) {
                Rectangle r = highlight.get(0);
                for (AbstractAction abstractAction : actions) {
                    if (abstractAction instanceof Move) {
                        Move action = (Move) abstractAction;
                        if (action.getToX() == r.x/defaultItemSize && action.getToY() == r.y/defaultItemSize) {
                            actionList.add(abstractAction);
//                            actionButtons[0].setVisible(true);
//                            actionButtons[0].setButtonAction(action, "Move "
//                                    + TicTacToeConstants.playerMapping.get(player.getPlayerID())
//                                    + " from[" + action.getFromX() + "," + action.getFromY() + "] to ["
//                                    + action.getToX() + "," + action.getToY() + "]");
                        }
                    }
                    if (abstractAction instanceof Capture) {
                        Capture action = (Capture) abstractAction;
                        if (action.getToX() == r.x/defaultItemSize && action.getToY() == r.y/defaultItemSize) {
                            actionList.add(abstractAction);
//                            actionButtons[0].setVisible(true);
//                            actionButtons[0].setButtonAction(action, "Capture "
//                                    + TicTacToeConstants.playerMapping.get(1 - player.getPlayerID())
//                                    + " from[" + action.getFromX() + "," + action.getFromY() + "] to["
//                                    + action.getToX() + "," + action.getToY() + "]");
                        }
                    }
                }
            }
            for (int i = 0; i < 4; i++) {
                actionButtons[i].setVisible(false);
                actionButtons[i].setButtonAction(null, "");
            }
            for (int i = 0; i < actionList.size(); i++) {
                AbstractAction abstractAction = actionList.get(i);
                if (abstractAction instanceof Move) {
                    Move action = (Move) abstractAction;
                    actionButtons[i].setVisible(true);
                    actionButtons[i].setButtonAction(action, "Move "
                            + TicTacToeConstants.playerMapping.get(player.getPlayerID())
                            + " from[" + action.getFromX() + "," + action.getFromY() + "] to ["
                            + action.getToX() + "," + action.getToY() + "]");
                }
                if (abstractAction instanceof Capture) {
                    Capture action = (Capture) abstractAction;
                    actionButtons[i].setVisible(true);
                    actionButtons[i].setButtonAction(action, "Capture "
                            + TicTacToeConstants.playerMapping.get(1 - player.getPlayerID())
                            + " from[" + action.getFromX() + "," + action.getFromY() + "] to["
                            + action.getToX() + "," + action.getToY() + "]");
                }
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
