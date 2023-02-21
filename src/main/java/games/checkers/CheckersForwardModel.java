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

    final boolean debug = false;
    private static int gridWidth, gridHeight;

    @Override
    protected void _setup(AbstractGameState firstState) {
        CheckersGameParameters chgp = (CheckersGameParameters) firstState.getGameParameters();
        gridWidth = chgp.gridWidth;
        gridHeight = chgp.gridHeight;
        CheckersGameState chgs = (CheckersGameState) firstState;
        chgs.gridBoard = new GridBoard<>(gridWidth, gridHeight, new Piece(CheckersConstants.emptyCell, false));

        for (int x = 0; x < chgs.getGridBoard().getWidth(); x++) {
            for (int y = 0; y < chgs.getGridBoard().getHeight(); y++) {
                if (y < 3) {    // black pieces
                    if ((x + y) % 2 == 1) {
                        chgs.gridBoard.setElement(x, y, CheckersConstants.playerMapping.get(0));
//                        chgs.gridBoard.getElement(x, y).makeKing();
                    }
                }
                if (y > (gridHeight - 4)) {    // white pieces
                    if ((x + y) % 2 == 1) {
                        chgs.gridBoard.setElement(x, y, CheckersConstants.playerMapping.get(1));
//                        chgs.gridBoard.getElement(x, y).makeKing();
                    }
                }
            }
        }
    }

    @Override
    protected List<AbstractAction> _computeAvailableActions(AbstractGameState gameState) {
        CheckersGameState chgs = (CheckersGameState) gameState;
        // list of available actions
        ArrayList<AbstractAction> actions = new ArrayList<>();
        int player = gameState.getTurnOrder().getCurrentPlayer(gameState);

        if (gameState.isNotTerminal()){

            // DONE: compute actual available actions correctly

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
            // DONE: check direction of player
            // DONE: remove piece after move
            // DONE: implement jump over opponent
            // DONE: implement multi-jumps
            // DONE: implement king piece backwards move
            // DONE: implement king piece unlimited distance move
            // DONE: implement king piece unlimited distance capture

            // calculate captures
            for (Pair<Integer, Integer> p : pPieces) {
                // calculate required captures
                ArrayList<Capture> captures = getCaptureActions(chgs, p);
                for (Capture c : captures) {
                    CheckersGameState gsCopy = (CheckersGameState) chgs.copy();
                    c.execute(gsCopy);
                    // check if capture possible after action is executed to determine end of turn
                    c.setEndOfTurn(getCaptureActions(gsCopy, c.getToCell()).isEmpty());
                    actions.add(c);
                }
            }

            // if no captures
            if (actions.isEmpty()) {
                // calculate available moves
                for (Pair<Integer, Integer> p : pPieces) {
                    ArrayList<Move> moves = getMoveActions(chgs, p);
                    for (Move m : moves) {
                        actions.add(m);
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

//        if (!actions.isEmpty())
            return actions;

        // TODO: no available moves, game ends with other player winning

//        return null;
    }

    private ArrayList<Move> getMoveActions(CheckersGameState gs, Pair<Integer, Integer> p) {
        ArrayList<Move> moves = new ArrayList<>();

        int player = gs.getCurrentPlayer();
        GridBoard<Piece> board = gs.getGridBoard();
        Pair<Integer, Integer> startPiece = new Pair<>(p.a, p.b);
        boolean isKing = board.getElement(p.a, p.b).isKing();

        for (int i = -1; i <= 1; i+=2) {
            for (int j = -1; j <= 1; j += 2) {
                int dist = 1;
                while (p.a+i*dist >= 0 && p.a+i*dist <= (gridWidth-1) && p.b+j*dist >= 0 && p.b+j*dist <= (gridHeight-1)) {
                    int x = p.a+i*dist, y = p.b+j*dist;
                    Piece piece = board.getElement(x, y);

                    // check if empty cell
                    if (!piece.getTokenType().equals(CheckersConstants.emptyCell)) {
                        break;
                    }

                    // only king can go backwards
                    if ((p.b < y == (player == 1)) && !isKing)  break;

                    // only one step for regular piece
                    if (dist == 1 || isKing) {
                        moves.add(new Move(player, startPiece, new Pair<>(p.a+i*dist, p.b+j*dist)));
                    }
                    dist++;
                }
            }
        }
        return moves;
    }

    private ArrayList<Capture> getCaptureActions (CheckersGameState gs, Pair<Integer, Integer> p) {
        ArrayList<Capture> captures = new ArrayList<>();

        int player = gs.getCurrentPlayer();
        GridBoard<Piece> board = gs.getGridBoard();
        Pair<Integer, Integer> startPiece = new Pair<>(p.a, p.b);
        boolean isKing = board.getElement(p.a, p.b).isKing();

        if (debug)
            System.out.println(": "+p.a +","+p.b);

        // 4 directions
        for (int i = -1; i <= 1; i+=2) {
            for (int j = -1; j<=1; j+=2) {
                int dist = 1;
                boolean markCaptured = false;
                boolean markAction = false;
                Pair<Integer, Integer> capturedPiece = new Pair<>(0,0);

                // check if inside board area
                while (p.a+i*dist >= 0 && p.a+i*dist <= (gridWidth-1) && p.b+j*dist >= 0 && p.b+j*dist <= (gridHeight-1)) {
                    Piece piece = board.getElement(p.a+i*dist, p.b+j*dist);

                    if (debug)
                        System.out.print("[" + (p.a+i*dist) + "," + (p.b+j*dist) + "]");

                    // check if own piece
                    if (piece.getTokenType().equals(CheckersConstants.playerMapping.get(player).getTokenType())) {
                        // stop checking this direction
                        if (debug)   System.out.print("p");
                        break;
                    }
                    // check if opponent piece
                    if (piece.getTokenType().equals(CheckersConstants.playerMapping.get(1 - player).getTokenType())) {
                        if (markCaptured) {
                            if (debug)  System.out.print("c");
                            break;
                        }
                        if (debug)  System.out.print("O");
                        capturedPiece = new Pair<>(p.a+i*dist, p.b+j*dist);
                        markCaptured = true;
                    }

                    // check if empty cell
                    if (piece.getTokenType().equals(CheckersConstants.emptyCell)) {
                        // if no king
                        if (!isKing && !markCaptured) {
                            if (debug)  System.out.print("nk");
                            break;
                        }
                        // capture action possible
                        if (markCaptured && (!markAction || isKing)) {
                            if (debug)  System.out.print("C");
                            Pair<Integer, Integer> endPiece = new Pair<>(p.a + i * dist, p.b + j * dist);
                            Capture c = new Capture(player, startPiece, endPiece, capturedPiece, false);
                            captures.add(c);
                            markAction = true;
                        }
                    }

                    // TODO: verwerken

                    dist++;
                }
            }
        }

        if (debug)
            System.out.println();

        if (debug)  System.out.println(captures.size() + " captures");
        return captures;
    }

    private boolean endOfTurn(CheckersGameState gs, Capture capture) {
        CheckersGameState gsCopy = (CheckersGameState) gs.copy();
        capture.execute(gsCopy);

        return false;
    }

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
        if(checkGameEnd((CheckersGameState) currentState)) {
//            CheckersFileManager chfm = new CheckersFileManager();
            System.out.println("Game end");
        }
//        ((CheckersGameState) currentState).printToConsole();
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

        // check if moves available
        if (_computeAvailableActions(gameState).isEmpty()) {
            registerWinner(gameState, 1-gameState.getCurrentPlayer());
            gameState.setGameStatus(Utils.GameResult.GAME_END);
            return true;
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
