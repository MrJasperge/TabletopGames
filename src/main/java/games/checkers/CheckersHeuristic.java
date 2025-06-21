package games.checkers;

import core.AbstractGameState;
import core.AbstractParameters;
import core.CoreConstants;
import core.actions.AbstractAction;
import core.interfaces.IStateHeuristic;
import evaluation.optimisation.TunableParameters;
import games.checkers.actions.Move;
import games.checkers.components.Piece;
import org.apache.spark.sql.catalyst.expressions.Abs;
import org.apache.spark.sql.sources.In;
import utilities.Pair;
import utilities.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CheckersHeuristic extends TunableParameters implements IStateHeuristic {

    // Main heuristics for Checkers:
    // 1. Consider winning and losing conditions.
    //    Return 1 if the player has won, -1 if the player has lost, and continue evaluating otherwise.
    // 2. Count the number of pieces for each player and count a score based on the ratio.
    // 3. Consider the capture actions of both players.
    // 4. Consider the number of move actions available.


    @Override
    public double evaluateState(AbstractGameState gs, int playerId) {

        // Check if the game is over
        CheckersGameState chgs = (CheckersGameState) gs;
        CoreConstants.GameResult playerResult = gs.getPlayerResults()[playerId];

        // If the game is over, return the result
        if(playerResult == CoreConstants.GameResult.LOSE_GAME) return -1;
        if(playerResult == CoreConstants.GameResult.WIN_GAME) return 1;

        // Check if this exact game state has been evaluated before
        // DONE: use gs.getHistory() to check if the game state has been evaluated before

        // Okay, we need to recognise when two players are stuck in a loop
        // To do this, we can check the history of actions taken by the player
        // If the last so many actions are the same, we can assume the player is stuck in a loop
        // Get the history of actions taken by the player

        if (gs.getHistory().size() < 2) return 0; // Not enough history to determine a loop

        int lastActionsCount = 15; // Number of last actions to check for loops
        List<Pair<Integer, AbstractAction>> history = gs.getHistory();
        List<AbstractAction> lastActions = new ArrayList<>();
        for (int i = history.size() - 1; i >= 0 && lastActions.size() < lastActionsCount; i--) {
            Pair<Integer, AbstractAction> actionPair = history.get(i);
            if (actionPair.a == playerId) {
                lastActions.add(actionPair.b);
            }
        }
        // Check if the last actions are the same
        boolean isLoop = false;
        int counter = 0;
        for (int i = 1; i < lastActions.size(); i++) {
            if (lastActions.get(i).equals(lastActions.get(0))) {
                counter++;
            }
            if (counter >= 3) {
                isLoop = true; // If the last actions are the same, the player is stuck in a loop
                break;
            }
        }
        boolean debug = false; // Set to true to enable debug output
        if (isLoop) {
            // If the player is stuck in a loop, return a score of -1 so this state is not selected
            if (CheckersConstants.DEBUG || debug) {
                System.out.println("Player " + playerId + " is stuck in a loop: " + lastActions.get(0).toString() + " Returning score -1.");
            }
            return -1;
        }

        // Compose list of pieces for each player
        ArrayList<Piece> playerPieces = new ArrayList<>();
        ArrayList<Piece> opponentPieces = new ArrayList<>();

        for (int x = 0; x < chgs.gridBoard.getWidth(); x++)
            for (int y = 0; y < chgs.gridBoard.getHeight(); y++) {
                Piece piece = (Piece) chgs.gridBoard.getElement(x, y);
                if (piece.getName().equals(CheckersConstants.playerMapping.get(playerId).getName())) {
                    playerPieces.add(piece);
                }
                if (piece.getName().equals(CheckersConstants.playerMapping.get(1-playerId).getName())) {
                    opponentPieces.add(piece);
                }
            }

        // count how many pieces each player has left on the board
        int nPlayer = playerPieces.size(), nOpponent = opponentPieces.size();

        // Some more checks to ensure the game is still ongoing
        if (nOpponent == 0 && nPlayer == 0) return 0;   // theoretically impossible to have 0 pieces, but edge case
        if (nOpponent == 0) return 1;   // opponent has no pieces left, player wins
        if (nPlayer == 0) return -1;    // player has no pieces left, opponent wins

        // Weights for the heuristic evaluation. Should add up to 1.0
        double[] weights = {
                0.5, // Number of pieces
                0.3, // Capture actions
                0.2  // Number of actions
        };

        // Calculate the score based on the number of pieces, capture actions, and move actions
        double score = 0.0;
        double kingWeight = 0.5; // Weight for king pieces, can be adjusted
        double pieceScore = calculatePieceScore(playerPieces, opponentPieces, kingWeight);
        double captureScore = calculateCaptureScore(gs, playerId);
        double moveScore = calculateMoveScore(gs, playerId); // Placeholder for move actions, can be implemented later
        score += weights[0] * pieceScore;
        score += weights[1] * captureScore;
        score += weights[2] * moveScore;

        score = Utils.clamp(score, -1.0, 1.0); // Ensure the score is within [-1, 1]

        // print debug information
        if (CheckersConstants.DEBUG) {
            System.out.println("Checkers Heuristic Evaluation:");
            System.out.println("GridBoard: \n" + chgs.gridBoard.toString());
            System.out.println("Player ID: " + playerId);
            System.out.println("Player Pieces: " + nPlayer + ", Opponent Pieces: " + nOpponent);
            System.out.println("Piece Score: " + pieceScore);
            System.out.println("Capture Score: " + captureScore);
            System.out.println("Move Score: " + moveScore);
            System.out.println("Final Score: " + score + "\n");
        }

        return score;  // calculate score between [-1, 1] based on ratio
    }

    private double calculateMoveScore(AbstractGameState gs, int playerId) {
        CheckersGameState chgs = (CheckersGameState) gs;

        int opponentMoveActions = 0;
        int playerMoveActions = 0;
        for (int x = 0; x < chgs.gridBoard.getWidth(); x++)
            for (int y = 0; y < chgs.gridBoard.getHeight(); y++) {
                Piece piece = (Piece) chgs.gridBoard.getElement(x, y);
                if (piece.getName().equals(CheckersConstants.playerMapping.get(1 - playerId).getName())) {
                    opponentMoveActions += chgs.getMoveActions(new Pair<>(x, y)).size();
                }
                if (piece.getName().equals(CheckersConstants.playerMapping.get(playerId).getName())) {
                    playerMoveActions += chgs.getMoveActions(new Pair<>(x, y)).size();
                }
            }

        double score = 0.0;
        // If the opponent has move actions, subtract the number of move actions from the score
        if (opponentMoveActions > 0) {
            score -= opponentMoveActions * 0.1; // Weight for opponent's move actions
        }
        // If the player has move actions, add the number of move actions to the score
        if (playerMoveActions > 0) {
            score += playerMoveActions * 0.1; // Weight for player's move actions
        }

        return Utils.clamp(score, -1.0, 1.0); // Ensure the score is within [-1, 1]
    }

    private double calculateCaptureScore(AbstractGameState gs, int playerId) {
        // First, check if opponent has possible capture actions
        CheckersGameState chgs = (CheckersGameState) gs;

        int opponentCaptureActions = 0;
        int playerCaptureActions = 0;
        for (int x = 0; x < chgs.gridBoard.getWidth(); x++)
            for (int y = 0; y < chgs.gridBoard.getHeight(); y++) {
                Piece piece = (Piece) chgs.gridBoard.getElement(x, y);
                if (piece.getName().equals(CheckersConstants.playerMapping.get(1 - playerId).getName())) {
                    opponentCaptureActions += chgs.getCaptureActions(new Pair<>(x, y)).size();
                }
                if (piece.getName().equals(CheckersConstants.playerMapping.get(playerId).getName())) {
                    playerCaptureActions += chgs.getCaptureActions(new Pair<>(x, y)).size();
                    // TODO consider the number of pieces captured in the future
                }
            }

        // If the opponent has capture actions, return a negative score based on the number of capture actions
        if (opponentCaptureActions > 0) {
            return Utils.clamp(-1.0 * opponentCaptureActions + 0.2 * playerCaptureActions, -1.0, 1.0);
        }
        // If the opponent has no capture actions, check if the player has any
        if (playerCaptureActions > 0) {
            return 1;
        }
        // If neither player has capture actions, return 0
        return 0;
    }

    // calculate score between [-1, 1] based on ratio between pieces of player and opponent
    private double calculatePieceScore(ArrayList<Piece> playerPieces, ArrayList<Piece> opponentPieces, double kingWeight) {
        double res = 0;
        int nPlayer = playerPieces.size();
        int nOpponent = opponentPieces.size();

        for (Piece piece : playerPieces) {
            if (piece.isKing()) res += kingWeight; // King pieces are worth more
        }
        for (Piece piece : opponentPieces) {
            if (piece.isKing()) res -= kingWeight; // King pieces are worth more
        }

        if (nPlayer > nOpponent) res = 1 - (double) nOpponent / (double) nPlayer;
        if (nOpponent > nPlayer) res = -1 * (1 - (double) nPlayer / (double) nOpponent);
        return res;
    }

    @Override
    protected AbstractParameters _copy() {
        return new CheckersHeuristic();
    }

    @Override
    protected boolean _equals(Object o) {
        return o instanceof CheckersHeuristic;
    }

    @Override
    public Object instantiate() {
        return this._copy();
    }

    @Override
    public void _reset() {
        // nothing to reset
    }
}
