package games.checkers;

import core.AbstractForwardModel;
import core.AbstractGameState;
import core.actions.AbstractAction;
import core.actions.SetGridValueAction;
import core.components.GridBoard;
import core.components.Token;
import utilities.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckersForwardModel extends AbstractForwardModel {

    //TODO: implement available actions

    @Override
    protected void _setup(AbstractGameState firstState) {
        CheckersGameParameters chgp = (CheckersGameParameters) firstState.getGameParameters();
        int gridWidth = chgp.gridWidth, gridHeight = chgp.gridHeight;
        CheckersGameState chgs = (CheckersGameState) firstState;
        chgs.gridBoard = new GridBoard<>(gridWidth, gridHeight, new Token(CheckersConstants.emptyCell));
    }

    @Override
    protected List<AbstractAction> _computeAvailableActions(AbstractGameState gameState) {
        CheckersGameState chgs = (CheckersGameState) gameState;
        ArrayList<AbstractAction> actions = new ArrayList<>();
        int player = gameState.getTurnOrder().getCurrentPlayer(gameState);

        if (gameState.isNotTerminal())
            // NOTE: for now, every empty cell is marked as available action
            for (int x = 0; x < chgs.gridBoard.getWidth(); x++)
                for (int y = 0; y < chgs.gridBoard.getHeight(); y++)
                    if (chgs.gridBoard.getElement(x, y).getTokenType().equals(CheckersConstants.emptyCell))
                        actions.add(new SetGridValueAction<>(chgs.gridBoard.getComponentID(), x, y, CheckersConstants.playerMapping.get(player)));
            // TODO: compute actual available actions

        return actions;
    }

    @Override
    protected AbstractForwardModel _copy() {
        return new CheckersForwardModel();
    }

    @Override
    protected void _next(AbstractGameState currentState, AbstractAction action) {
        action.execute(currentState);
        CheckersGameParameters chgp = (CheckersGameParameters) currentState.getGameParameters();
        int gridWidth = chgp.gridWidth, gridHeight = chgp.gridHeight;

        if (checkGameEnd((CheckersGameState) currentState)) {
            return;
        }
        currentState.getTurnOrder().endPlayerTurn(currentState);
    }

    private boolean checkGameEnd(CheckersGameState gameState) {
        GridBoard<Token> gridBoard = gameState.getGridBoard();
        // count number of pieces
        int bPiece = 0, wPiece = 0;
        for (int x = 0; x < gridBoard.getWidth(); x++)
            for (int y = 0; y < gridBoard.getHeight(); y++) {
                if (gridBoard.getElement(x, y).equals(CheckersConstants.playerMapping.get(0))) bPiece++;
                if (gridBoard.getElement(x, y).equals(CheckersConstants.playerMapping.get(1))) wPiece++;
            }

        if (bPiece == 0) {
            registerWinner(gameState, 0);
            return true;
        }
        if (wPiece == 0) {
            registerWinner(gameState, 1);
            return true;
        }
        return false;
    }

    @Override
    protected void endGame(AbstractGameState gameState) {
        if (gameState.getCoreGameParameters().verbose) {
            System.out.println(Arrays.toString(gameState.getPlayerResults()));
        }
    }

    private void registerWinner(CheckersGameState gameState, int winningPlayer) {
        gameState.setGameStatus(Utils.GameResult.GAME_END);
        gameState.setPlayerResult(Utils.GameResult.WIN, winningPlayer);
        gameState.setPlayerResult(Utils.GameResult.LOSE, 1 - winningPlayer);
    }
}
