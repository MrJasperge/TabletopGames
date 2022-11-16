package games.checkers;

import core.AbstractForwardModel;
import core.AbstractGameState;
import core.actions.AbstractAction;
import core.components.GridBoard;
import games.checkers.actions.Capture;
import games.checkers.actions.Move;
import games.checkers.components.Piece;
import utilities.Pair;
import utilities.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckersForwardModel extends AbstractForwardModel {

    //TODO: implement available actions

    final boolean debug = false;
    private boolean hasRequiredCapture = false;

    @Override
    protected void _setup(AbstractGameState firstState) {
        CheckersGameParameters chgp = (CheckersGameParameters) firstState.getGameParameters();
        int gridWidth = chgp.gridWidth, gridHeight = chgp.gridHeight;
        CheckersGameState chgs = (CheckersGameState) firstState;
        chgs.gridBoard = new GridBoard<>(gridWidth, gridHeight, new Piece(CheckersConstants.emptyCell));

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

    @Override
    protected List<AbstractAction> _computeAvailableActions(AbstractGameState gameState) {
        CheckersGameState chgs = (CheckersGameState) gameState;
        ArrayList<AbstractAction> actions = new ArrayList<>();
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
            if (debug)  System.out.println("Pieces:\n" + pPieces);

            // TODO: soft code boundaries
            // TODO: check direction of player
            // TODO: remove piece after move
            // TODO: implement jump over opponent
            // TODO: implement multi-jumps
            // TODO: implement king piece



            for (IntPair p : pPieces) {
                // calculate required captures
                if (p.x > 1 && p.y > 1) {   // up left
                    if (chgs.gridBoard.getElement(p.x - 1, p.y - 1).equals(CheckersConstants.playerMapping.get(1 - player))
                            && chgs.gridBoard.getElement(p.x - 2, p.y - 2).getTokenType().equals(CheckersConstants.emptyCell)) {

                        hasRequiredCapture = true;

                        actions.add(new Capture(player
                                , new Pair<Integer, Integer>(p.x, p.y)
                                , new Pair<Integer, Integer>(p.x - 2, p.y - 2)
                                , new Pair<Integer, Integer>(p.x - 1, p.y - 1)
                                ,true));
                    }
                }
                if (p.x < 6 && p.y > 1) {   // up right
                    if (chgs.gridBoard.getElement(p.x + 1, p.y - 1).equals(CheckersConstants.playerMapping.get(1 - player))
                            && chgs.gridBoard.getElement(p.x + 2, p.y - 2).getTokenType().equals(CheckersConstants.emptyCell)) {

                        hasRequiredCapture = true;

                        actions.add(new Capture(player
                                , new Pair<Integer, Integer>(p.x, p.y)
                                , new Pair<Integer, Integer>(p.x + 2, p.y - 2)
                                , new Pair<Integer, Integer>(p.x + 1, p.y - 1)
                                ,true));
                    }
                }
                if (p.x > 1 && p.y < 6) {   // down left
                    if (chgs.gridBoard.getElement(p.x - 1, p.y + 1).equals(CheckersConstants.playerMapping.get(1 - player))
                            && chgs.gridBoard.getElement(p.x - 2, p.y + 2).getTokenType().equals(CheckersConstants.emptyCell)) {

                        hasRequiredCapture = true;

                        actions.add(new Capture(player
                                , new Pair<Integer, Integer>(p.x, p.y)
                                , new Pair<Integer, Integer>(p.x - 2, p.y + 2)
                                , new Pair<Integer, Integer>(p.x - 1, p.y + 1)
                                ,true));
                    }
                }
                if (p.x < 6 && p.y < 6) {   // down right
                    if (chgs.gridBoard.getElement(p.x + 1, p.y + 1).equals(CheckersConstants.playerMapping.get(1 - player))
                            && chgs.gridBoard.getElement(p.x + 2, p.y + 2).getTokenType().equals(CheckersConstants.emptyCell)) {

                        hasRequiredCapture = true;

                        actions.add(new Capture(player
                                , new Pair<Integer, Integer>(p.x, p.y)
                                , new Pair<Integer, Integer>(p.x + 2, p.y + 2)
                                , new Pair<Integer, Integer>(p.x + 1, p.y + 1)
                                ,true));
                    }
                }
            }

            for (IntPair p : pPieces) {
                // calculate available moves
                if (!hasRequiredCapture) {
                    if (p.x > 0 && p.y > 0) {   // up left
                        if (chgs.gridBoard.getElement(p.x-1, p.y-1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<Integer,Integer>(p.x, p.y), new Pair<Integer, Integer>(p.x-1, p.y-1)));
                        }
                    }

                    if (p.x < 7 && p.y > 0) {   // up right
                        if (chgs.gridBoard.getElement(p.x+1, p.y-1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<Integer,Integer>(p.x, p.y), new Pair<Integer, Integer>(p.x+1, p.y-1)));
                        }
                    }

                    if (p.x > 0 && p.y < 7) {   // down left
                        if (chgs.gridBoard.getElement(p.x-1, p.y+1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<Integer,Integer>(p.x, p.y), new Pair<Integer, Integer>(p.x-1, p.y+1)));
                        }
                    }

                    if (p.x < 7 && p.y < 7) {   // down right
                        if (chgs.gridBoard.getElement(p.x+1, p.y+1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<Integer,Integer>(p.x, p.y), new Pair<Integer, Integer>(p.x+1, p.y+1)));
                        }
                    }
                }
            }
        }
        hasRequiredCapture = false;
        if (debug) {
            System.out.println("Actions:");
            for (AbstractAction a : actions) {
                if (a instanceof Move) {
                    Move m = (Move) a;
                    System.out.print("([" + m.getFromX() + "," + m.getFromY() + "] to ["
                            + m.getToX() + "," + m.getToY() + "]) ");
                }
            }
            System.out.println("");
        }

        if (actions.size() == 0) actions.add(new Move(player, new Pair<Integer, Integer>(0, 0), new Pair<Integer, Integer>(0, 0)));
        return actions;  // TODO: return actions
    }

    private Pair<Integer, Integer> calculateCaptures (Pair<Integer, Integer> curr) {

        // up left
        if (curr.a > 1 && curr.b > 1) {
            return calculateCaptures(new Pair<Integer, Integer>(curr.a-2, curr.b-2));
        }


        // up right

        // bottom left

        // bottom right

        return curr;
    }

    /**
     * Creates a pair of Integers (x, y).
     *
     * @param x - x value.
     * @param y - y value
     * @return - element (x,y).
     */
    private static class IntPair {
        // x-component
        final int x;

        // y-component
        final int y;

        // Pairs of Integers
        IntPair(int x, int y) {this.x = x; this.y = y;}

        @Override
        public String toString() {
            return "[" + this.x + "," + this.y + "]";
        }
    }

    @Override
    protected AbstractForwardModel _copy() {
        return new CheckersForwardModel();
    }

    @Override
    protected void _next(AbstractGameState currentState, AbstractAction action) {
        action.execute(currentState);
//        CheckersGameParameters chgp = (CheckersGameParameters) currentState.getGameParameters();
//        int gridWidth = chgp.gridWidth, gridHeight = chgp.gridHeight;
//
//        if (checkGameEnd((CheckersGameState) currentState)) {
//            return;
//        }
//        currentState.getTurnOrder().endPlayerTurn(currentState);
    }

    private boolean checkGameEnd(CheckersGameState gameState) {
        GridBoard<Piece> gridBoard = gameState.getGridBoard();
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
