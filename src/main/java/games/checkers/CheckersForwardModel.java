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

import java.util.*;

public class CheckersForwardModel extends AbstractForwardModel {

    //TODO: implement available actions

    final boolean debug = true;
    private boolean hasRequiredCapture = false;
    private ArrayList<AbstractAction> actions;

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
        actions = new ArrayList<>();
        int player = gameState.getTurnOrder().getCurrentPlayer(gameState);

        if (gameState.isNotTerminal()){

            // TODO: compute actual available actions correctly

            // all pieces of the player
            ArrayList<Pair<Integer, Integer>> pPieces = new ArrayList<>();
            ArrayList<Pair<Integer, Integer>> oPieces = new ArrayList<>();

            for (int x = 0; x < chgs.gridBoard.getWidth(); x++)
                for (int y = 0; y < chgs.gridBoard.getHeight(); y++) {
                    if (chgs.gridBoard.getElement(x, y).equals(CheckersConstants.playerMapping.get(player))) {
                        pPieces.add(new Pair<Integer, Integer>(x, y));  // player's pieces

                    }
                    if (chgs.gridBoard.getElement(x, y).equals(CheckersConstants.playerMapping.get(1 - player))) {
                        oPieces.add(new Pair<Integer, Integer>(x, y));  // player's pieces
                    }
                }

            if (debug) {
                System.out.println("\nCompute Available Actions:\n");
                System.out.println("Pieces:");
                for (Pair<Integer, Integer> p : pPieces) {
                    System.out.print("([" + p.a + "," + p.b + "]) ");
                }
                System.out.println("");
            }

            // TODO: soft code boundaries
            // TODO: check direction of player
            // TODO: remove piece after move
            // TODO: implement jump over opponent
            // TODO: implement multi-jumps
            // TODO: implement king piece



            for (Pair<Integer, Integer> p : pPieces) {
                // calculate required captures
                if (p.a > 1 && p.b > 1) {   // up left
                    if (chgs.gridBoard.getElement(p.a - 1, p.b - 1).equals(CheckersConstants.playerMapping.get(1 - player))
                            && chgs.gridBoard.getElement(p.a - 2, p.b - 2).getTokenType().equals(CheckersConstants.emptyCell)) {

                        hasRequiredCapture = true;

                        Deque<Pair<Integer, Integer>> path = new LinkedList<>();
                        path.add(new Pair<Integer, Integer>(p.a, p.b));
                        path.add(new Pair<Integer, Integer>(p.a-1, p.b-1));
                        path.add(new Pair<Integer, Integer>(p.a-2, p.b-2));

                        CheckersGameState gsCopy = (CheckersGameState) chgs.copy();

                        calculateCaptures(gsCopy, path);

//                        new Capture(player
//                                , new Pair<Integer, Integer>(p.x, p.y)
//                                , new Pair<Integer, Integer>(p.x - 2, p.y - 2)
//                                , new Pair<Integer, Integer>(p.x - 1, p.y - 1)
//                                ,true);
                    }
                }
                /*
                if (p.a < 6 && p.b > 1) {   // up right
                    if (chgs.gridBoard.getElement(p.a + 1, p.b - 1).equals(CheckersConstants.playerMapping.get(1 - player))
                            && chgs.gridBoard.getElement(p.a + 2, p.b - 2).getTokenType().equals(CheckersConstants.emptyCell)) {

                        hasRequiredCapture = true;

//                        actions.add(new Capture(player
//                                , new Pair<Integer, Integer>(p.x, p.y)
//                                , new Pair<Integer, Integer>(p.x + 2, p.y - 2)
//                                , new Pair<Integer, Integer>(p.x + 1, p.y - 1)
//                                ,true));
                    }
                }
                if (p.a > 1 && p.b < 6) {   // down left
                    if (chgs.gridBoard.getElement(p.a - 1, p.b + 1).equals(CheckersConstants.playerMapping.get(1 - player))
                            && chgs.gridBoard.getElement(p.a - 2, p.b + 2).getTokenType().equals(CheckersConstants.emptyCell)) {

                        hasRequiredCapture = true;

//                        actions.add(new Capture(player
//                                , new Pair<Integer, Integer>(p.x, p.y)
//                                , new Pair<Integer, Integer>(p.x - 2, p.y + 2)
//                                , new Pair<Integer, Integer>(p.x - 1, p.y + 1)
//                                ,true));
                    }
                }
                if (p.a < 6 && p.b < 6) {   // down right
                    if (chgs.gridBoard.getElement(p.a + 1, p.b + 1).equals(CheckersConstants.playerMapping.get(1 - player))
                            && chgs.gridBoard.getElement(p.a + 2, p.b + 2).getTokenType().equals(CheckersConstants.emptyCell)) {

                        hasRequiredCapture = true;

//                        actions.add(new Capture(player
//                                , new Pair<Integer, Integer>(p.x, p.y)
//                                , new Pair<Integer, Integer>(p.x + 2, p.y + 2)
//                                , new Pair<Integer, Integer>(p.x + 1, p.y + 1)
//                                ,true));
                    }
                }*/
            }

            for (Pair<Integer, Integer> p : pPieces) {
                // calculate available moves
                if (!hasRequiredCapture) {
                    if (p.a > 0 && p.b > 0) {   // up left
                        if (chgs.gridBoard.getElement(p.a-1, p.b-1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<Integer,Integer>(p.a, p.b), new Pair<Integer, Integer>(p.a-1, p.b-1)));
                        }
                    }

                    if (p.a < 7 && p.b > 0) {   // up right
                        if (chgs.gridBoard.getElement(p.a+1, p.b-1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<Integer,Integer>(p.a, p.b), new Pair<Integer, Integer>(p.a+1, p.b-1)));
                        }
                    }

                    if (p.a > 0 && p.b < 7) {   // down left
                        if (chgs.gridBoard.getElement(p.a-1, p.b+1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<Integer,Integer>(p.a, p.b), new Pair<Integer, Integer>(p.a-1, p.b+1)));
                        }
                    }

                    if (p.a < 7 && p.b < 7) {   // down right
                        if (chgs.gridBoard.getElement(p.a+1, p.b+1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<Integer,Integer>(p.a, p.b), new Pair<Integer, Integer>(p.a+1, p.b+1)));
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
                if (a instanceof Capture) {
                    Capture c = (Capture) a;
                    ArrayList<Pair<Integer, Integer>> cells = c.getCapturedCells();
                    System.out.print("([" + c.getFromX() + "," + c.getFromY() + "] to ["
                            + c.getToX() + "," + c.getToY() + "] capturing [");
                    for (Pair<Integer, Integer> cell : cells) {
                        System.out.print("[" + cell.a + "," + cell.b + "] ");
                    }
                    System.out.print("]) ");
                }
            }
            System.out.println("");
        }

        return actions;  // TODO: return actions
    }

    private void calculateCaptures (CheckersGameState prevGSCopy, Deque<Pair<Integer, Integer>> path) {

        if (debug) {
            System.out.println("calculateCaptures path:");
            for (Pair<Integer, Integer> p : path) {
                System.out.print("([" + p.a + "," + p.b + "]) ");
            }
            System.out.println("");
        }

        // boolean to check if this is the end of a path
        boolean pathIsComplete = true;
        int player = prevGSCopy.getCurrentPlayer();

        if (path.peek() == null) return;
        Pair<Integer, Integer> p = path.peek();

        // up left
        if (p.a > 1 && p.b > 1) {
            if (prevGSCopy.gridBoard.getElement(p.a - 1, p.b - 1).equals(CheckersConstants.playerMapping.get(1 - player))
                    && prevGSCopy.gridBoard.getElement(p.a - 2, p.b - 2).getTokenType().equals(CheckersConstants.emptyCell)) {
                // don't return incomplete path
                pathIsComplete = false;
                // create copy of gamestate
                CheckersGameState newGSCopy = (CheckersGameState) prevGSCopy.copy();
                // captured cell
                Pair<Integer, Integer> capCell = new Pair<>(p.a-1, p.b-1);
                // array of captured cells
                ArrayList<Pair<Integer, Integer>> r = new ArrayList<>();
                r.add(capCell);
                // to cell
                Pair<Integer, Integer> q = new Pair<>(p.a-2, p.b-2);
                // create capture action from p to q capturing r
                Capture newCap = new Capture(player, p, q, r,false);
                // execute action in copy
                newCap.execute(newGSCopy);

                // create copy of path
                Deque<Pair<Integer, Integer>> qCopy = new LinkedList<>(path);
                // remove head of queue
                qCopy.remove();
                // add new captured cell to path
                qCopy.add(capCell);
                // add new head to queue
                qCopy.add(q);
                // recursive call
                calculateCaptures(newGSCopy, qCopy);
            }
        }


        // up right
        // bottom left
        // bottom right

        // if this is a leaf
        // which is when the end of a path is reached
        if (pathIsComplete) {
            Pair<Integer, Integer> toCell = path.removeLast();
            Pair<Integer, Integer> fromCell = path.removeFirst();
            ArrayList<Pair<Integer, Integer>> capturedCells = new ArrayList<>();
            capturedCells.addAll(path);
            actions.add(new Capture(player, fromCell, toCell, capturedCells, true));
        }
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
        CheckersGameParameters chgp = (CheckersGameParameters) currentState.getGameParameters();
        int gridWidth = chgp.gridWidth, gridHeight = chgp.gridHeight;

        checkGameEnd((CheckersGameState) currentState);
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
            gameState.setGameStatus(Utils.GameResult.GAME_END);
            return true;
        }
        if (wPiece == 0) {
            registerWinner(gameState, 1);
            gameState.setGameStatus(Utils.GameResult.GAME_END);
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
