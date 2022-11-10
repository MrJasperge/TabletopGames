package games.checkers;

import core.AbstractForwardModel;
import core.AbstractGameState;
import core.actions.AbstractAction;
import core.actions.SetGridValueAction;
import core.components.GridBoard;
import core.components.Token;
import utilities.Utils;

import java.lang.reflect.Array;
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

        // TODO: add correct starting positions

        for (int x = 0; x < chgs.getGridBoard().getWidth(); x++) {
            for (int y = 0; y < chgs.getGridBoard().getHeight(); y++) {
                if (y < 3) {    // black pieces
                    if ((x + y) % 2 == 1) {
                        chgs.gridBoard.setElement(x, y, CheckersConstants.playerMapping.get(0));
                    }
                }
                if (y > 4) {    // white pieces
                    if ((x + y) % 2 == 1) {
                        chgs.gridBoard.setElement(x, y, CheckersConstants.playerMapping.get(1));
                    }
                }
            }
        }
    }


    // TODO: figure out actions return
    @Override
    protected List<AbstractAction> _computeAvailableActions(AbstractGameState gameState) {
        CheckersGameState chgs = (CheckersGameState) gameState;
        ArrayList<AbstractAction> action = new ArrayList<>();
        ArrayList<ArrayList<AbstractAction>> actions = new ArrayList<>();
        int player = gameState.getTurnOrder().getCurrentPlayer(gameState);

        if (gameState.isNotTerminal()){

            // TODO: compute actual available actions correctly

            // all pieces of the player
            ArrayList<IntPair> pPieces = new ArrayList<>();
            ArrayList<IntPair> oPieces = new ArrayList<>();

            for (int x = 0; x < chgs.gridBoard.getWidth(); x++)
                for (int y = 0; y < chgs.gridBoard.getHeight(); y++) {
                    if (chgs.gridBoard.getElement(x, y).equals(CheckersConstants.playerMapping.get(player))) {
                        pPieces.add(new IntPair(x, y));  // player's pieces
                    }
                    if (chgs.gridBoard.getElement(x, y).equals(CheckersConstants.playerMapping.get(1 - player))) {
                        oPieces.add(new IntPair(x, y));  // player's pieces
                    }
                }
            // TODO: soft code boundaries
            // TODO: check direction of player
            // TODO: remove piece after move
            // TODO: implement jump over opponent
            // TODO: implement multi-jumps
            // TODO: implement king piece
            for (IntPair p : pPieces) {
                if (p.x > 0 && p.y > 0) {   // up left
                    if (chgs.gridBoard.getElement(p.x-1, p.y-1).getTokenType().equals(CheckersConstants.emptyCell)) {
                        action.add(new SetGridValueAction<>(chgs.gridBoard.getComponentID(), p.x - 1, p.y - 1, CheckersConstants.playerMapping.get(player)));
                        action.add(new SetGridValueAction<>(chgs.gridBoard.getComponentID(), p.x, p.y, new Token(CheckersConstants.emptyCell)));
                        actions.add(action);
                    }
                }

                if (p.x < 7 && p.y > 0) {   // up right
                    if (chgs.gridBoard.getElement(p.x+1, p.y-1).getTokenType().equals(CheckersConstants.emptyCell)) {
                        action.add(new SetGridValueAction<>(chgs.gridBoard.getComponentID(), p.x + 1, p.y - 1, CheckersConstants.playerMapping.get(player)));
                        action.add(new SetGridValueAction<>(chgs.gridBoard.getComponentID(), p.x, p.y, new Token(CheckersConstants.emptyCell)));
                        actions.add(action);
                    }
                }

                if (p.x > 0 && p.y < 7) {   // down left
                    if (chgs.gridBoard.getElement(p.x-1, p.y+1).getTokenType().equals(CheckersConstants.emptyCell)) {
                        action.add(new SetGridValueAction<>(chgs.gridBoard.getComponentID(), p.x - 1, p.y + 1, CheckersConstants.playerMapping.get(player)));
                        action.add(new SetGridValueAction<>(chgs.gridBoard.getComponentID(), p.x, p.y, new Token(CheckersConstants.emptyCell)));
                        actions.add(action);
                    }
                }

                if (p.x < 7 && p.y < 7) {   // down right
                    if (chgs.gridBoard.getElement(p.x+1, p.y+1).getTokenType().equals(CheckersConstants.emptyCell)) {
                        action.add(new SetGridValueAction<>(chgs.gridBoard.getComponentID(), p.x + 1, p.y + 1, CheckersConstants.playerMapping.get(player)));
                        action.add(new SetGridValueAction<>(chgs.gridBoard.getComponentID(), p.x, p.y, new Token(CheckersConstants.emptyCell)));
                        actions.add(action);
                    }
                }
            }

        }
        return action;  // TODO: return actions
    }


    /**
     * Creates a pair of Integers (x, y).
     *
     * @param x - x value.
     * @param y - y value
     * @return - element (x,y).
     */
    class IntPair {
        // x-component
        final int x;

        // y-component
        final int y;

        // Pairs of Integers
        IntPair(int x, int y) {this.x = x; this.y = y;}
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
