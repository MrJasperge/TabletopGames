package games.checkers;

import core.AbstractGameState;
import core.AbstractParameters;
import core.components.Component;
import core.components.GridBoard;
import games.GameType;
import games.checkers.actions.Capture;
import games.checkers.actions.Move;
import games.checkers.components.Piece;
import utilities.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CheckersGameState extends AbstractGameState {

    GridBoard gridBoard;
    private boolean skipTurn = false;

    public CheckersGameState(AbstractParameters gameParameters, int nPlayers) {
        super(gameParameters, nPlayers);
    }

    public GridBoard getGridBoard() {
        return this.gridBoard;
    }

    @Override
    protected GameType _getGameType() {
        return GameType.Checkers;
    }

    @Override
    protected List<Component> _getAllComponents() {
        return new ArrayList<>() {{
            add(gridBoard);
        }};
    }

    @Override
    protected AbstractGameState _copy(int playerId) {
        CheckersGameState copy = new CheckersGameState(gameParameters.copy(), getNPlayers());
        copy.gridBoard = gridBoard.copy();
        copy.skipTurn = skipTurn;

        // Copy the grid board
        for (int x = 0; x < gridBoard.getWidth(); x++) {
            for (int y = 0; y < gridBoard.getHeight(); y++) {
                Piece piece = (Piece) gridBoard.getElement(x, y);
                if (piece != null) {
                    copy.gridBoard.setElement(x, y, piece.copy());
                }
            }
        }


        return copy;
    }

    public int getNextPlayer() {
        int nextPlayer = 1 - getCurrentPlayer();
        if (skipTurn) {
            nextPlayer = getCurrentPlayer();
        }
        return nextPlayer;
    }

    public boolean isSkipTurn() {
        return skipTurn;
    }

    public void setSkipTurn(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }


    @Override
    protected double _getHeuristicScore(int playerId) {
        return new CheckersHeuristic().evaluateState(this, playerId);
    }

    @Override
    public double getGameScore(int playerId) {
        return playerResults[playerId].value;
    }

    protected void _reset() {
        gridBoard = null;
    }

    @Override
    protected boolean _equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CheckersGameState that)) return false;
        return Objects.equals(gridBoard, that.gridBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gridBoard);
    }

    @Override
    public String toString() {
        String sb = Objects.hash(gameParameters) + "|" +
                Objects.hash(getAllComponents()) + "|" +
                Objects.hash(gameStatus) + "|" +
                Objects.hash(gamePhase) + "|*|" +
                Objects.hash(gridBoard);
        return sb;
    }

    public int getPieceCount(int playerId) {
        int count = 0;
        for (int x = 0; x < gridBoard.getWidth(); x++) {
            for (int y = 0; y < gridBoard.getHeight(); y++) {
                Piece piece = (Piece) gridBoard.getElement(x, y);
                if (piece != null && piece.getName().equals(CheckersConstants.playerMapping.get(playerId).getName())) {
                    count++;
                }
            }
        }
        return count;
    }

    public ArrayList<Move> getMoveActions(Pair<Integer, Integer> p) {
        ArrayList<Move> moves = new ArrayList<>();

        GridBoard board = getGridBoard();
        int gridWidth = board.getWidth();
        int gridHeight = board.getHeight();

        Pair<Integer, Integer> startPiece = new Pair<>(p.a, p.b);
        Piece startPieceObj = (Piece) board.getElement(p.a, p.b);
        int player = startPieceObj.getPlayerID();
        boolean isKing = ((Piece) board.getElement(p.a, p.b)).isKing();


        for (int i = -1; i <= 1; i+=2) {
            for (int j = -1; j <= 1; j += 2) {
                int dist = 1;
                while (p.a+i*dist >= 0 && p.a+i*dist <= (gridWidth-1) && p.b+j*dist >= 0 && p.b+j*dist <= (gridHeight-1)) {
                    int x = p.a+i*dist, y = p.b+j*dist;
                    Piece piece = (Piece) board.getElement(x, y);

                    // check if empty cell
                    if (!piece.getName().equals(CheckersConstants.emptyCell)) {
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

    public ArrayList<Capture> getCaptureActions (Pair<Integer, Integer> p) {
        ArrayList<Capture> captures = new ArrayList<>();

        GridBoard board = getGridBoard();
        int gridWidth = board.getWidth();
        int gridHeight = board.getHeight();

        Pair<Integer, Integer> startPiece = new Pair<>(p.a, p.b);
        Piece startPieceObj = (Piece) board.getElement(p.a, p.b);
        int player = startPieceObj.getPlayerID();

//        if (debug)
//            System.out.println(": "+p.a +","+p.b);

        // 4 directions
        for (int i = -1; i <= 1; i+=2) { // horizontal
            for (int j = -1; j<=1; j+=2) { // vertical
                int dist = 1; // distance from start piece
                boolean markCaptured = false;
                boolean markAction = false;
                Pair<Integer, Integer> capturedPiece = new Pair<>(0,0);

                // check if inside board area
                while (p.a+i*dist >= 0 && p.a+i*dist <= (gridWidth-1) && p.b+j*dist >= 0 && p.b+j*dist <= (gridHeight-1)) {
                    Piece piece = (Piece)board.getElement(p.a+i*dist, p.b+j*dist);

//                    if (debug)
//                        System.out.print("[" + (p.a+i*dist) + "," + (p.b+j*dist) + "]");

                    // check if own piece
                    if (piece.getName().equals(CheckersConstants.playerMapping.get(player).getName())) {
                        // stop checking this direction
//                        if (debug)   System.out.print("p");
                        break;
                    }
                    // check if opponent piece
                    if (piece.getName().equals(CheckersConstants.playerMapping.get(1 - player).getName())) {
                        if (markCaptured) {
                            // if already marked a piece, stop checking this direction. 2 opponent pieces in a row
//                            if (debug)  System.out.print("c");
                            break;
                        }
//                        if (debug)  System.out.print("O");
                        markCaptured = true;
                    }

                    // check if empty square
                    if (piece.getName().equals(CheckersConstants.emptyCell)) {
                        // if no king
                        if (!startPieceObj.isKing() && !markCaptured) {
                            break;
                        }
                        // capture action possible
                        if (markCaptured && (!markAction || startPieceObj.isKing())) {
//                            if (debug)  System.out.print("C");
                            Pair<Integer, Integer> endPiece = new Pair<>(p.a + i * dist, p.b + j * dist);
                            Capture c = new Capture(player, startPiece, endPiece);
                            captures.add(c);
                            markAction = true;
                        }
                    }
                    dist++;
                }
            }
        }

//        if (debug)
//            System.out.println();

//        if (debug)  System.out.println(captures.size() + " [getCaptureActions] captures");
        return captures;
    }

    public void printToConsole() {
        System.out.println(gridBoard.toString());
    }

}
