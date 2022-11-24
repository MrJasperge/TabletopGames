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

import java.lang.reflect.Array;
import java.util.*;

public class CheckersForwardModel extends AbstractForwardModel {

    //TODO: implement available actions

    final boolean debug = true;
    private ArrayList<AbstractAction> actions;
    private static int gridWidth, gridHeight;

    @Override
    protected void _setup(AbstractGameState firstState) {
        CheckersGameParameters chgp = (CheckersGameParameters) firstState.getGameParameters();
        gridWidth = chgp.gridWidth;
        gridHeight = chgp.gridHeight;
        CheckersGameState chgs = (CheckersGameState) firstState;
        chgs.gridBoard = new GridBoard<>(gridWidth, gridHeight, new Piece(CheckersConstants.emptyCell, false));

        // TODO: add correct starting positions

        for (int x = 0; x < chgs.getGridBoard().getWidth(); x++) {
            for (int y = 0; y < chgs.getGridBoard().getHeight(); y++) {
                if (y < 3) {    // black pieces
                    if ((x + y) % 2 == 1) {
                        chgs.gridBoard.setElement(x, y, CheckersConstants.playerMapping.get(0));
                    }
                }
                if (y > (gridHeight - 4)) {    // white pieces
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
        // list of available actions
        actions = new ArrayList<>();
        int player = gameState.getTurnOrder().getCurrentPlayer(gameState);

        if (gameState.isNotTerminal()){

            // TODO: compute actual available actions correctly

            // all pieces of the player
            ArrayList<Pair<Integer, Integer>> pPieces = new ArrayList<>();

            // get all pieces
            for (int x = 0; x < chgs.gridBoard.getWidth(); x++)
                for (int y = 0; y < chgs.gridBoard.getHeight(); y++) {
                    if (chgs.gridBoard.getElement(x, y).getTokenType().equals(CheckersConstants.playerMapping.get(player).getTokenType())) {
                        pPieces.add(new Pair<>(x, y));  // player's pieces
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

            // DONE: soft code boundaries
            // TODO: check direction of player
            // DONE: remove piece after move
            // DONE: implement jump over opponent
            // DONE: implement multi-jumps
            // TODO: implement king piece


            // calculate captures
            for (Pair<Integer, Integer> p : pPieces) {
                // calculate required captures
                Deque<Pair<Integer, Integer>> path = new LinkedList<>();
                path.addFirst(p);
                path.addFirst(p);
                calculateCaptures(chgs, path);
            }

            // if no captures
            if (actions.isEmpty()) {
                // calculate available moves
                for (Pair<Integer, Integer> p : pPieces) {
                    boolean isKing = chgs.gridBoard.getElement(p.a, p.b).isKing();
                    if ((player == 0 && isKing || player == 1) && (p.a > 0 && p.b > 0)) {   // up left
                        if (chgs.gridBoard.getElement(p.a - 1, p.b - 1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<>(p.a, p.b), new Pair<>(p.a - 1, p.b - 1)));
                        }
                    }

                    if ((player == 0 && isKing || player == 1) && (p.a < 7 && p.b > 0)) {   // up right
                        if (chgs.gridBoard.getElement(p.a + 1, p.b - 1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<>(p.a, p.b), new Pair<>(p.a + 1, p.b - 1)));
                        }
                    }

                    if ((player == 1 && isKing || player == 0) && (p.a > 0 && p.b < 7)) {   // down left
                        if (chgs.gridBoard.getElement(p.a - 1, p.b + 1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<>(p.a, p.b), new Pair<>(p.a - 1, p.b + 1)));
                        }
                    }

                    if ((player == 1 && isKing || player == 0) && (p.a < 7 && p.b < 7)) {   // down right
                        if (chgs.gridBoard.getElement(p.a + 1, p.b + 1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<>(p.a, p.b), new Pair<>(p.a + 1, p.b + 1)));
                        }
                    }
                }
            }
        }

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

    private ArrayList<Integer> getCaptures (CheckersGameState gs, Pair<Integer, Integer> p) {
        ArrayList<Integer> captures = new ArrayList<>();
        int player = gs.getCurrentPlayer();
        if (p == null) return captures;

        // up left
        if (p.a > 1 && p.b > 1) {
            if (gs.gridBoard.getElement(p.a - 1, p.b - 1).getTokenType().equals(CheckersConstants.playerMapping.get(1 - player).getTokenType())
                    && gs.gridBoard.getElement(p.a - 2, p.b - 2).getTokenType().equals(CheckersConstants.emptyCell)) {
                captures.add(1);
            }
        }
        // up right
        if (p.a < (gridWidth-2) && p.b > 1) {
            if (gs.gridBoard.getElement(p.a + 1, p.b - 1).getTokenType().equals(CheckersConstants.playerMapping.get(1 - player).getTokenType())
                    && gs.gridBoard.getElement(p.a + 2, p.b - 2).getTokenType().equals(CheckersConstants.emptyCell)) {
                captures.add(2);
                System.out.println("up right gridWidth: " + gridWidth);
            }
        }
        // down left
        if (p.a > 1 && p.b < (gridHeight-2)) {
            if (gs.gridBoard.getElement(p.a - 1, p.b + 1).getTokenType().equals(CheckersConstants.playerMapping.get(1 - player).getTokenType())
                    && gs.gridBoard.getElement(p.a - 2, p.b + 2).getTokenType().equals(CheckersConstants.emptyCell)) {
                captures.add(3);
            }
        }
        // down right
        if (p.a < (gridWidth-2) && p.b < (gridHeight-2)) {
            if (gs.gridBoard.getElement(p.a + 1, p.b + 1).getTokenType().equals(CheckersConstants.playerMapping.get(1 - player).getTokenType())
                    && gs.gridBoard.getElement(p.a + 2, p.b + 2).getTokenType().equals(CheckersConstants.emptyCell)) {
                captures.add(4);
            }
        }
        return captures;
    }

    private void calculateCaptures (CheckersGameState gs, Deque<Pair<Integer, Integer>> path) {

        // player
        int player = gs.getCurrentPlayer();
        // piece at head of path
        Pair<Integer, Integer> p = path.peek();

        // all possible captures path head
        ArrayList<Integer> possibleCaptures = getCaptures(gs, path.peekLast());

        boolean isEndOfTurn;

        // for every possible capture
        for (int i : possibleCaptures) {
            // create copy of gamestate
            CheckersGameState newGSCopy = (CheckersGameState) gs.copy();
            Pair<Integer, Integer> capCell;
            // array of captured cells
            ArrayList<Pair<Integer, Integer>> r = new ArrayList<>();
            Pair<Integer, Integer> q;
            Capture newCap;
            ArrayList<Integer> dir;
            switch (i){
                case 1:     // up left
                    // captured cell
                    capCell = new Pair<>(p.a-1, p.b-1);
                    // array of captured cells
                    r.add(capCell);
                    // to cell
                    q = new Pair<>(p.a-2, p.b-2);
                    break;
                case 2:     // up right
                    // captured cell
                    capCell = new Pair<>(p.a+1, p.b-1);
                    r.add(capCell);
                    // to cell
                    q = new Pair<>(p.a+2, p.b-2);
                    break;
                case 3:     // down left
                    // captured cell
                    capCell = new Pair<>(p.a-1, p.b+1);
                    r.add(capCell);
                    // to cell
                    q = new Pair<>(p.a-2, p.b+2);
                    break;
                case 4:     // down right
                    // captured cell
                    capCell = new Pair<>(p.a+1, p.b+1);
                    r.add(capCell);
                    // to cell
                    q = new Pair<>(p.a+2, p.b+2);
                    break;
                default:
                    q = null;
            }
            // create capture action from p to q capturing r
            newCap = new Capture(player, p, q, r,false);
            // execute action in copy
            newCap.execute(newGSCopy);

            // check if end of turn
            isEndOfTurn = (dir = getCaptures(newGSCopy, q)).isEmpty();
            if (debug) {
                if (!dir.isEmpty())
                    System.out.println("Possible capture: " + dir);
                else System.out.println("No possible capture");
            }
            actions.add(new Capture(player, p, q, r, isEndOfTurn));
        }

    }

//    private void calculateCapturesRecursive (CheckersGameState prevGSCopy, Deque<Pair<Integer, Integer>> path) {
//
//        if (debug) {
//            System.out.println("\ncalculateCaptures path:");
//            for (Pair<Integer, Integer> p : path) {
//                System.out.print("([" + p.a + "," + p.b + "]) ");
//            }
//            System.out.println("");
//        }
//
//        // player
//        int player = prevGSCopy.getCurrentPlayer();
//        // piece at head of path
//        Pair<Integer, Integer> p = path.peek();
//
//        // all possible captures path head
//        ArrayList<Integer> possibleCaptures = getCaptures(prevGSCopy, path.peekLast());
//
//        // if no available actions
//        if (possibleCaptures.isEmpty()) {
//            // if path is not empty
//            if (path.size() > 2) {
//                Pair<Integer, Integer> fromCell, toCell;
//
//                toCell = path.removeFirst();
//                fromCell = path.removeLast();
//
//                ArrayList<Pair<Integer, Integer>> capturedCells = new ArrayList<>(path);
//
//                // add new Capture action
//                actions.add(new Capture(player, fromCell, toCell, capturedCells, true));
//                System.out.println("capture found");
//            } else {
//                System.out.println("no captures");
//            }
//            return;
//        }
//
//        // for every possible capture
//        for (int i : possibleCaptures) {
//            switch (i){
//                case 1:     // up left
//                    // create copy of gamestate
//                    CheckersGameState newGSCopyUL = (CheckersGameState) prevGSCopy.copy();
//                    // captured cell
//                    Pair<Integer, Integer> capCellUL = new Pair<>(p.a-1, p.b-1);
//                    // array of captured cells
//                    ArrayList<Pair<Integer, Integer>> rUL = new ArrayList<>();
//                    rUL.add(capCellUL);
//                    // to cell
//                    Pair<Integer, Integer> qUL = new Pair<>(p.a-2, p.b-2);
//                    // create capture action from p to q capturing r
//                    Capture newCapUL = new Capture(player, p, qUL, rUL,true);
//                    // execute action in copy
//                    newCapUL.execute(newGSCopyUL);
//
//                    // create copy of path
//                    Deque<Pair<Integer, Integer>> qCopyUL = new LinkedList<>(path);
//                    // remove head of queue
//                    qCopyUL.removeFirst();
//                    // add new captured cell to path
//                    qCopyUL.addFirst(capCellUL);
//                    // add new head to queue
//                    qCopyUL.addFirst(qUL);
//                    // recursive call
//                    calculateCaptures(newGSCopyUL, qCopyUL);
//                    break;
//                case 2:     // up right
//                    // create copy of gamestate
//                    CheckersGameState newGSCopyUR = (CheckersGameState) prevGSCopy.copy();
//                    // captured cell
//                    Pair<Integer, Integer> capCellUR = new Pair<>(p.a+1, p.b-1);
//                    // array of captured cells
//                    ArrayList<Pair<Integer, Integer>> rUR = new ArrayList<>();
//                    rUR.add(capCellUR);
//                    // to cell
//                    Pair<Integer, Integer> qUR = new Pair<>(p.a+2, p.b-2);
//                    // create capture action from p to q capturing r
//                    Capture newCapUR = new Capture(player, p, qUR, rUR,true);
//                    // execute action in copy
//                    newCapUR.execute(newGSCopyUR);
//
//                    // create copy of path
//                    Deque<Pair<Integer, Integer>> qCopyUR = new LinkedList<>(path);
//                    // remove head of queue
//                    qCopyUR.removeFirst();
//                    // add new captured cell to path
//                    qCopyUR.addFirst(capCellUR);
//                    // add new head to queue
//                    qCopyUR.addFirst(qUR);
//                    // recursive call
//                    calculateCaptures(newGSCopyUR, qCopyUR);
//                    break;
//                case 3:     // down left
//                    // create copy of gamestate
//                    CheckersGameState newGSCopyDL = (CheckersGameState) prevGSCopy.copy();
//                    // captured cell
//                    Pair<Integer, Integer> capCellDL = new Pair<>(p.a-1, p.b+1);
//                    // array of captured cells
//                    ArrayList<Pair<Integer, Integer>> rDL = new ArrayList<>();
//                    rDL.add(capCellDL);
//                    // to cell
//                    Pair<Integer, Integer> qDL = new Pair<>(p.a-2, p.b+2);
//                    // create capture action from p to q capturing r
//                    Capture newCapDL = new Capture(player, p, qDL, rDL,true);
//                    // execute action in copy
//                    newCapDL.execute(newGSCopyDL);
//
//                    // create copy of path
//                    Deque<Pair<Integer, Integer>> qCopyDL = new LinkedList<>(path);
//                    // remove head of queue
//                    qCopyDL.removeFirst();
//                    // add new captured cell to path
//                    qCopyDL.addFirst(capCellDL);
//                    // add new head to queue
//                    qCopyDL.addFirst(qDL);
//                    // recursive call
//                    calculateCaptures(newGSCopyDL, qCopyDL);
//                    break;
//                case 4:     // down right
//                    // create copy of gamestate
//                    CheckersGameState newGSCopyDR = (CheckersGameState) prevGSCopy.copy();
//                    // captured cell
//                    Pair<Integer, Integer> capCellDR = new Pair<>(p.a+1, p.b+1);
//                    // array of captured cells
//                    ArrayList<Pair<Integer, Integer>> rDR = new ArrayList<>();
//                    rDR.add(capCellDR);
//                    // to cell
//                    Pair<Integer, Integer> qDR = new Pair<>(p.a+2, p.b+2);
//                    // create capture action from p to q capturing r
//                    Capture newCapDR = new Capture(player, p, qDR, rDR,true);
//                    // execute action in copy
//                    newCapDR.execute(newGSCopyDR);
//
//                    // create copy of path
//                    Deque<Pair<Integer, Integer>> qCopyDR = new LinkedList<>(path);
//                    // remove head of queue
//                    qCopyDR.removeFirst();
//                    // add new captured cell to path
//                    qCopyDR.addFirst(capCellDR);
//                    // add new head to queue
//                    qCopyDR.addFirst(qDR);
//                    // recursive call
//                    calculateCaptures(newGSCopyDR, qCopyDR);
//                    break;
//                default:    // no possible captures, this should not be reached
//                    return;
//            }
//        }
//    }

    @Override
    protected AbstractForwardModel _copy() {
        return new CheckersForwardModel();
    }

    @Override
    protected void _next(AbstractGameState currentState, AbstractAction action) {
        action.execute(currentState);
        CheckersGameParameters chgp = (CheckersGameParameters) currentState.getGameParameters();
        gridWidth = chgp.gridWidth;
        gridHeight = chgp.gridHeight;
        checkGameEnd((CheckersGameState) currentState);
    }

    private boolean checkGameEnd(CheckersGameState gameState) {
        GridBoard<Piece> gridBoard = gameState.getGridBoard();
        // count number of pieces
        int bPiece = 0, wPiece = 0;
        for (int x = 0; x < gridBoard.getWidth(); x++)
            for (int y = 0; y < gridBoard.getHeight(); y++) {
                if (gridBoard.getElement(x, y).getTokenType().equals(CheckersConstants.playerMapping.get(0).getTokenType())) bPiece++;
                if (gridBoard.getElement(x, y).getTokenType().equals(CheckersConstants.playerMapping.get(1).getTokenType())) wPiece++;
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
