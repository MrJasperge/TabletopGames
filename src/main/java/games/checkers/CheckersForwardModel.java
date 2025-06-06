package games.checkers;

import core.*;
import core.actions.AbstractAction;
import core.components.BoardNode;
import core.components.GridBoard;
import games.checkers.actions.Capture;
import games.checkers.actions.Move;
import games.checkers.actions.Remove;
import games.checkers.components.Piece;
import utilities.Pair;

import java.util.*;

public class CheckersForwardModel extends StandardForwardModel {

    final boolean debug = false;
    private static int gridWidth, gridHeight;
    private CheckersFileManager chfm;
    private int moves = 0;
    public ArrayList<AbstractAction> prevActions;

    @Override
    protected void _setup(AbstractGameState firstState) {
        CheckersGameParameters chgp = (CheckersGameParameters) firstState.getGameParameters();
        chfm = new CheckersFileManager();
//        System.out.println("CheckersForwardModel: CreateFile");

        CheckersGameState chgs = (CheckersGameState) firstState;


        if (debug) System.out.println("CheckersForwardModel: inputFileName = " + chgp.inputFileName);

        if(chfm.ReadFile(chgp.getInputPath())) {

            // read 2d array from file
            String[][] boardData = chfm.getData();
            gridWidth = boardData[0].length;
            gridHeight = boardData.length;
            chgs.gridBoard = new GridBoard(gridWidth, gridHeight);

            for (int x = 0; x < gridWidth; x++) {
                for (int y = 0; y < gridHeight; y++) {
                    String pieceName = boardData[y][x];
                    if (!pieceName.equals(CheckersConstants.emptyCell)) {
                        if (!pieceName.equals(CheckersConstants.playerMapping.get(0).getName()) &&
                                !pieceName.equals(CheckersConstants.playerMapping.get(1).getName())) {
                            System.out.println("CheckersForwardModel: Invalid piece name: " + pieceName);
                            // set empty cell if invalid piece name
                            chgs.gridBoard.setElement(x, y, new Piece(CheckersConstants.emptyCell));
                        }
                        Piece piece = new Piece(pieceName);
                        chgs.gridBoard.setElement(x, y, piece);
                    } else {
                        chgs.gridBoard.setElement(x, y, new Piece(CheckersConstants.emptyCell));
                    }
                }
            }
            if (debug) {
                System.out.println("CheckersForwardModel: Read board from file: " + chgp.inputFileName);
                System.out.println("Grid width: " + gridWidth + ", height: " + gridHeight);
                System.out.println("Grid board: \n" + chgs.gridBoard.toString());
            }

        } else {
            if (debug)
                System.out.println("CheckersForwardModel: File not found: " + chgp.getInputPath());

//            chfm.CreateFile(chgp.getInputPath());
            gridWidth = chgp.gridWidth;
            gridHeight = chgp.gridHeight;
            prevActions = new ArrayList<>();

            chgs.gridBoard = new GridBoard(gridWidth, gridHeight);

            // Initialize empty cells first
            for (int x = 0; x < chgs.getGridBoard().getWidth(); x++) {
                for (int y = 0; y < chgs.getGridBoard().getHeight(); y++) {
                    chgs.gridBoard.setElement(x, y, new Piece(CheckersConstants.emptyCell));
                }
            }

    //        System.out.println("gridboard w+h: " + chgs.getGridBoard().getWidth() + "+" + chgs.getGridBoard().getHeight());

            for (int x = 0; x < chgs.getGridBoard().getWidth(); x++) {
                for (int y = 0; y < chgs.getGridBoard().getHeight(); y++) {
                    if (y < 3) {    // black pieces
                        if ((x + y) % 2 == 1) {
                            Piece p = new Piece(CheckersConstants.playerMapping.get(0).getName());
                            chgs.gridBoard.setElement(x, y, p);
                        }
                    }
                    if (y > (gridHeight - 4)) {    // white pieces
                        if ((x + y) % 2 == 1) {
    //                        chgs.checkersBoard.setElement(x, y, CheckersConstants.playerMapping.get(1));
                            Piece p = new Piece(CheckersConstants.playerMapping.get(1).getName());
                            chgs.gridBoard.setElement(x, y, p);
                        }
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
        ArrayList<Pair<Integer, Integer>> pPieces = new ArrayList<>();

        int player = chgs.getCurrentPlayer();
        GridBoard board = chgs.getGridBoard();

        if (chgs.isNotTerminal()){
            // all pieces of the player

            // check if player has pieces
            for (int x = 0; x < board.getWidth(); x++)
                for (int y = 0; y < board.getHeight(); y++) {
                    // check if piece of player its own piece
                    if (((Piece) chgs.getGridBoard().getElement(x, y)).getName().equals(CheckersConstants.playerMapping.get(player).getName())) {
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

            // calculate available captures
            for (Pair<Integer, Integer> p : pPieces) {
                ArrayList<Capture> captures = chgs.getCaptureActions(p);
                actions.addAll(captures);
            }

            // if no captures, calculate available moves
            if (actions.isEmpty()) {
                for (Pair<Integer, Integer> p : pPieces) {
                    ArrayList<Move> moves = chgs.getMoveActions(p);
                    actions.addAll(moves);
                }
            }
        }

        if (debug) {
            System.out.println("Actions:");
            for (AbstractAction a : actions) {
                if (a instanceof Move m) {
                    System.out.print("([" + m.getFromX() + "," + m.getFromY() + "] to ["
                            + m.getToX() + "," + m.getToY() + "]) ");
                }
//                if (a instanceof Capture c) {
//                    ArrayList<Pair<Integer, Integer>> cells = c.getCapturedCells();
//                    System.out.print("([" + c.getFromX() + "," + c.getFromY() + "] to ["
//                            + c.getToX() + "," + c.getToY() + "] capturing [");
//                    for (Pair<Integer, Integer> cell : cells) {
//                        System.out.print("[" + cell.a + "," + cell.b + "] ");
//                    }
//                    System.out.print("]) ");
//                }
            }
            System.out.println("");
        }

        prevActions = actions; // store previous actions

        if (actions.isEmpty()) {
            actions.add(new Remove(pPieces)); // no actions available, remove all pieces
            if(debug)
                System.out.println("No actions available, removing " + pPieces.size() + " pieces of player " + player);
        }

        boolean HumanGUI = false; // local variable, no access to players via gamestate
        if (HumanGUI && actions.size() == 1) actions.add(actions.get(0)); // add a dummy action to avoid issues with single action
        return actions;
    }

    @Override
    protected void _afterAction(AbstractGameState currentState, AbstractAction action) {
        if (currentState.getGameStatus() == CoreConstants.GameResult.GAME_END || currentState.isActionInProgress()) {
            return;
        }

        CheckersGameState chgs = (CheckersGameState) currentState;
        checkGameEnd(chgs);

        // print current player
        if (debug) {
            System.out.println("Current player: " + chgs.getCurrentPlayer());
//            System.out.println("Action: " + action);
        }

        // check if turn should be skipped
        if (action instanceof Capture c) {
            chgs.setSkipTurn(!chgs.getCaptureActions(c.getToCell()).isEmpty());
        }

        endPlayerTurn(chgs, chgs.getNextPlayer());
        chgs.setSkipTurn(false);

//        moves++;

    }

    protected AbstractForwardModel _copy() {
        return new CheckersForwardModel();
    }

    /*
    protected void _next(AbstractGameState currentState, AbstractAction action) {
        moves++;
        action.execute(currentState);
        CheckersGameState chgs = (CheckersGameState) currentState;
        CheckersGameParameters chgp = (CheckersGameParameters) currentState.getGameParameters();
        gridWidth = chgp.gridWidth;
        gridHeight = chgp.gridHeight;
        checkGameEnd(chgs);
    }
     */

    private void checkGameEnd(CheckersGameState gameState) {
        GridBoard board = gameState.getGridBoard();
        // count number of pieces
        int xPiece = 0, oPiece = 0;
        for (int x = 0; x < board.getWidth(); x++)
            for (int y = 0; y < board.getHeight(); y++) {
                if (((Piece)board.getElement(x, y)).getName().equals(CheckersConstants.playerMapping.get(0).getName())) xPiece++;
                if (((Piece)board.getElement(x, y)).getName().equals(CheckersConstants.playerMapping.get(1).getName())) oPiece++;
            }

//        System.out.println("bPiece: " + bPiece + ", wPiece: " + wPiece + "\n");

        // black "X" player wins
        if (oPiece == 0) {
            registerWinner(gameState, 0);
//            if (chfm != null)
//                chfm.WriteData(Integer.toString(xPiece) + "," + moves + '\n'); // number of pieces and moves
//            System.out.println("1," + bPiece + "," + moves);
            return;
        }
        // white "O" player wins
        if (xPiece == 0) {
            registerWinner(gameState, 1);
//            if (chfm != null)
//                chfm.WriteData(Integer.toString(oPiece) + "," + moves + '\n');
//            System.out.println("0," + wPiece + "," + moves);
            return;
        }
        // check if draw
        if (prevActions.isEmpty()) {
            int winner = 1 - gameState.getCurrentPlayer();
            System.out.println("Winner: " + winner);
            registerWinner(gameState, winner);
            return;
//            if (chfm != null) chfm.WriteData(Integer.toString(xPiece + oPiece) + "," + moves + '\n');
        }
    }

    @Override
    protected void endGame(AbstractGameState gameState) {
        if (gameState.getCoreGameParameters().verbose) {
            System.out.println(Arrays.toString(gameState.getPlayerResults()));
        }

    }

    private void registerWinner(CheckersGameState gameState, int winningPlayer) {
//        if (chfm != null) chfm.WriteData(winningPlayer+",");
        gameState.setPlayerResult(CoreConstants.GameResult.WIN_GAME, winningPlayer);
        gameState.setPlayerResult(CoreConstants.GameResult.LOSE_GAME, 1 - winningPlayer);
        gameState.setGameStatus(CoreConstants.GameResult.GAME_END);
    }
}
