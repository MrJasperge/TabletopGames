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
                    if (chgs.gridBoard.getElement(x, y).equals(CheckersConstants.playerMapping.get(player))) {
                        pPieces.add(new Pair<Integer, Integer>(x, y));  // player's pieces
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
                    if (p.a > 0 && p.b > 0) {   // up left
                        if (chgs.gridBoard.getElement(p.a - 1, p.b - 1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<Integer, Integer>(p.a, p.b), new Pair<Integer, Integer>(p.a - 1, p.b - 1)));
                        }
                    }

                    if (p.a < 7 && p.b > 0) {   // up right
                        if (chgs.gridBoard.getElement(p.a + 1, p.b - 1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<Integer, Integer>(p.a, p.b), new Pair<Integer, Integer>(p.a + 1, p.b - 1)));
                        }
                    }

                    if (p.a > 0 && p.b < 7) {   // down left
                        if (chgs.gridBoard.getElement(p.a - 1, p.b + 1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<Integer, Integer>(p.a, p.b), new Pair<Integer, Integer>(p.a - 1, p.b + 1)));
                        }
                    }

                    if (p.a < 7 && p.b < 7) {   // down right
                        if (chgs.gridBoard.getElement(p.a + 1, p.b + 1).getTokenType().equals(CheckersConstants.emptyCell)) {
                            actions.add(new Move(player, new Pair<Integer, Integer>(p.a, p.b), new Pair<Integer, Integer>(p.a + 1, p.b + 1)));
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

    private ArrayList<Integer> getMoves (CheckersGameState gs, Pair<Integer, Integer> p) {
        return null;
    }

    private ArrayList<Integer> getCaptures (CheckersGameState gs, Pair<Integer, Integer> p) {
        ArrayList<Integer> captures = new ArrayList<>();
        int player = gs.getCurrentPlayer();
        if (p == null) return captures;

        // up left
        if (p.a > 1 && p.b > 1) {
            if (gs.gridBoard.getElement(p.a - 1, p.b - 1).equals(CheckersConstants.playerMapping.get(1 - player))
                    && gs.gridBoard.getElement(p.a - 2, p.b - 2).getTokenType().equals(CheckersConstants.emptyCell)) {
                captures.add(1);
            }
        }
        // up right
        if (p.a < 6 && p.b > 1) {
            if (gs.gridBoard.getElement(p.a + 1, p.b - 1).equals(CheckersConstants.playerMapping.get(1 - player))
                    && gs.gridBoard.getElement(p.a + 2, p.b - 2).getTokenType().equals(CheckersConstants.emptyCell)) {
                captures.add(2);
            }
        }
        // down left
        if (p.a > 1 && p.b < 6) {
            if (gs.gridBoard.getElement(p.a - 1, p.b + 1).equals(CheckersConstants.playerMapping.get(1 - player))
                    && gs.gridBoard.getElement(p.a - 2, p.b + 2).getTokenType().equals(CheckersConstants.emptyCell)) {
                captures.add(3);
            }
        }
        // down right
        if (p.a < 6 && p.b < 6) {
            if (gs.gridBoard.getElement(p.a + 1, p.b + 1).equals(CheckersConstants.playerMapping.get(1 - player))
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

        boolean isEndOfTurn = true;

        // for every possible capture
        for (int i : possibleCaptures) {
            switch (i){
                case 1:     // up left
                    // create copy of gamestate
                    CheckersGameState newGSCopyUL = (CheckersGameState) gs.copy();
                    // captured cell
                    Pair<Integer, Integer> capCellUL = new Pair<>(p.a-1, p.b-1);
                    // array of captured cells
                    ArrayList<Pair<Integer, Integer>> rUL = new ArrayList<>();
                    rUL.add(capCellUL);
                    // to cell
                    Pair<Integer, Integer> qUL = new Pair<>(p.a-2, p.b-2);
                    // create capture action from p to q capturing r
                    Capture newCapUL = new Capture(player, p, qUL, rUL,false);
                    // execute action in copy
                    newCapUL.execute(newGSCopyUL);

                    // check if end of turn
                    isEndOfTurn = !getCaptures(newGSCopyUL, p).isEmpty();
                    actions.add(new Capture(player, p, qUL, rUL, isEndOfTurn));

                    break;
                case 2:     // up right
                    // create copy of gamestate
                    CheckersGameState newGSCopyUR = (CheckersGameState) gs.copy();
                    // captured cell
                    Pair<Integer, Integer> capCellUR = new Pair<>(p.a+1, p.b-1);
                    // array of captured cells
                    ArrayList<Pair<Integer, Integer>> rUR = new ArrayList<>();
                    rUR.add(capCellUR);
                    // to cell
                    Pair<Integer, Integer> qUR = new Pair<>(p.a+2, p.b-2);
                    // create capture action from p to q capturing r
                    Capture newCapUR = new Capture(player, p, qUR, rUR,false);
                    // execute action in copy
                    newCapUR.execute(newGSCopyUR);

                    // check if end of turn
                    isEndOfTurn = !getCaptures(newGSCopyUR, p).isEmpty();
                    actions.add(new Capture(player, p, qUR, rUR, isEndOfTurn));
                    break;
                case 3:     // down left
                    // create copy of gamestate
                    CheckersGameState newGSCopyDL = (CheckersGameState) gs.copy();
                    // captured cell
                    Pair<Integer, Integer> capCellDL = new Pair<>(p.a-1, p.b+1);
                    // array of captured cells
                    ArrayList<Pair<Integer, Integer>> rDL = new ArrayList<>();
                    rDL.add(capCellDL);
                    // to cell
                    Pair<Integer, Integer> qDL = new Pair<>(p.a-2, p.b+2);
                    // create capture action from p to q capturing r
                    Capture newCapDL = new Capture(player, p, qDL, rDL,false);
                    // execute action in copy
                    newCapDL.execute(newGSCopyDL);

                    // check if end of turn
                    isEndOfTurn = !getCaptures(newGSCopyDL, p).isEmpty();
                    actions.add(new Capture(player, p, qDL, rDL, isEndOfTurn));
                    break;
                case 4:     // down right
                    // create copy of gamestate
                    CheckersGameState newGSCopyDR = (CheckersGameState) gs.copy();
                    // captured cell
                    Pair<Integer, Integer> capCellDR = new Pair<>(p.a+1, p.b+1);
                    // array of captured cells
                    ArrayList<Pair<Integer, Integer>> rDR = new ArrayList<>();
                    rDR.add(capCellDR);
                    // to cell
                    Pair<Integer, Integer> qDR = new Pair<>(p.a+2, p.b+2);
                    // create capture action from p to q capturing r
                    Capture newCapDR = new Capture(player, p, qDR, rDR,false);
                    // execute action in copy
                    newCapDR.execute(newGSCopyDR);

                    // check if end of turn
                    isEndOfTurn = !getCaptures(newGSCopyDR, p).isEmpty();
                    actions.add(new Capture(player, p, qDR, rDR, isEndOfTurn));
                    break;
            }
        }

    }
    private void calculateCapturesRecursive (CheckersGameState prevGSCopy, Deque<Pair<Integer, Integer>> path) {

        if (debug) {
            System.out.println("\ncalculateCaptures path:");
            for (Pair<Integer, Integer> p : path) {
                System.out.print("([" + p.a + "," + p.b + "]) ");
            }
            System.out.println("");
        }

        // player
        int player = prevGSCopy.getCurrentPlayer();
        // piece at head of path
        Pair<Integer, Integer> p = path.peek();

        // all possible captures path head
        ArrayList<Integer> possibleCaptures = getCaptures(prevGSCopy, path.peekLast());

        // if no available actions
        if (possibleCaptures.isEmpty()) {
            // if path is not empty
            if (path.size() > 2) {
                Pair<Integer, Integer> fromCell, toCell;

                toCell = path.removeFirst();
                fromCell = path.removeLast();

                ArrayList<Pair<Integer, Integer>> capturedCells = new ArrayList<>(path);

                // add new Capture action
                actions.add(new Capture(player, fromCell, toCell, capturedCells, true));
                System.out.println("capture found");
            } else {
                System.out.println("no captures");
            }
            return;
        }

        // for every possible capture
        for (int i : possibleCaptures) {
            switch (i){
                case 1:     // up left
                    // create copy of gamestate
                    CheckersGameState newGSCopyUL = (CheckersGameState) prevGSCopy.copy();
                    // captured cell
                    Pair<Integer, Integer> capCellUL = new Pair<>(p.a-1, p.b-1);
                    // array of captured cells
                    ArrayList<Pair<Integer, Integer>> rUL = new ArrayList<>();
                    rUL.add(capCellUL);
                    // to cell
                    Pair<Integer, Integer> qUL = new Pair<>(p.a-2, p.b-2);
                    // create capture action from p to q capturing r
                    Capture newCapUL = new Capture(player, p, qUL, rUL,true);
                    // execute action in copy
                    newCapUL.execute(newGSCopyUL);

                    // create copy of path
                    Deque<Pair<Integer, Integer>> qCopyUL = new LinkedList<>(path);
                    // remove head of queue
                    qCopyUL.removeFirst();
                    // add new captured cell to path
                    qCopyUL.addFirst(capCellUL);
                    // add new head to queue
                    qCopyUL.addFirst(qUL);
                    // recursive call
                    calculateCaptures(newGSCopyUL, qCopyUL);
                    break;
                case 2:     // up right
                    // create copy of gamestate
                    CheckersGameState newGSCopyUR = (CheckersGameState) prevGSCopy.copy();
                    // captured cell
                    Pair<Integer, Integer> capCellUR = new Pair<>(p.a+1, p.b-1);
                    // array of captured cells
                    ArrayList<Pair<Integer, Integer>> rUR = new ArrayList<>();
                    rUR.add(capCellUR);
                    // to cell
                    Pair<Integer, Integer> qUR = new Pair<>(p.a+2, p.b-2);
                    // create capture action from p to q capturing r
                    Capture newCapUR = new Capture(player, p, qUR, rUR,true);
                    // execute action in copy
                    newCapUR.execute(newGSCopyUR);

                    // create copy of path
                    Deque<Pair<Integer, Integer>> qCopyUR = new LinkedList<>(path);
                    // remove head of queue
                    qCopyUR.removeFirst();
                    // add new captured cell to path
                    qCopyUR.addFirst(capCellUR);
                    // add new head to queue
                    qCopyUR.addFirst(qUR);
                    // recursive call
                    calculateCaptures(newGSCopyUR, qCopyUR);
                    break;
                case 3:     // down left
                    // create copy of gamestate
                    CheckersGameState newGSCopyDL = (CheckersGameState) prevGSCopy.copy();
                    // captured cell
                    Pair<Integer, Integer> capCellDL = new Pair<>(p.a-1, p.b+1);
                    // array of captured cells
                    ArrayList<Pair<Integer, Integer>> rDL = new ArrayList<>();
                    rDL.add(capCellDL);
                    // to cell
                    Pair<Integer, Integer> qDL = new Pair<>(p.a-2, p.b+2);
                    // create capture action from p to q capturing r
                    Capture newCapDL = new Capture(player, p, qDL, rDL,true);
                    // execute action in copy
                    newCapDL.execute(newGSCopyDL);

                    // create copy of path
                    Deque<Pair<Integer, Integer>> qCopyDL = new LinkedList<>(path);
                    // remove head of queue
                    qCopyDL.removeFirst();
                    // add new captured cell to path
                    qCopyDL.addFirst(capCellDL);
                    // add new head to queue
                    qCopyDL.addFirst(qDL);
                    // recursive call
                    calculateCaptures(newGSCopyDL, qCopyDL);
                    break;
                case 4:     // down right
                    // create copy of gamestate
                    CheckersGameState newGSCopyDR = (CheckersGameState) prevGSCopy.copy();
                    // captured cell
                    Pair<Integer, Integer> capCellDR = new Pair<>(p.a+1, p.b+1);
                    // array of captured cells
                    ArrayList<Pair<Integer, Integer>> rDR = new ArrayList<>();
                    rDR.add(capCellDR);
                    // to cell
                    Pair<Integer, Integer> qDR = new Pair<>(p.a+2, p.b+2);
                    // create capture action from p to q capturing r
                    Capture newCapDR = new Capture(player, p, qDR, rDR,true);
                    // execute action in copy
                    newCapDR.execute(newGSCopyDR);

                    // create copy of path
                    Deque<Pair<Integer, Integer>> qCopyDR = new LinkedList<>(path);
                    // remove head of queue
                    qCopyDR.removeFirst();
                    // add new captured cell to path
                    qCopyDR.addFirst(capCellDR);
                    // add new head to queue
                    qCopyDR.addFirst(qDR);
                    // recursive call
                    calculateCaptures(newGSCopyDR, qCopyDR);
                    break;
                default:    // no possible captures, this should not be reached
                    return;
            }
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
