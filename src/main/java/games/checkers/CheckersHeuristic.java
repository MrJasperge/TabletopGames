package games.checkers;

import core.AbstractGameState;
import core.AbstractParameters;
import core.interfaces.IStateHeuristic;
import evaluation.TunableParameters;
import utilities.Utils;

public class CheckersHeuristic extends TunableParameters implements IStateHeuristic {

    @Override
    public double evaluateState(AbstractGameState gs, int playerId) {
        // simple heuristic: amount of player's pieces should be more than opponent's
        CheckersGameState chgs = (CheckersGameState) gs;
        Utils.GameResult playerResult = gs.getPlayerResults()[playerId];

        if(playerResult == Utils.GameResult.LOSE) {
            return -1;
        }
        if(playerResult == Utils.GameResult.WIN) {
            return 1;
        }

        // count how many pieces of player characters
        int nPlayer = 0, nOpponent = 0;
        for (int x = 0; x < chgs.gridBoard.getWidth(); x++)
            for (int y = 0; y < chgs.gridBoard.getHeight(); y++) {
                if (chgs.gridBoard.getElement(x, y).equals(CheckersConstants.playerMapping.get(0))) nPlayer++;
                if (chgs.gridBoard.getElement(x, y).equals(CheckersConstants.playerMapping.get(1))) nOpponent++;
            }

        if (nOpponent == 0 && nPlayer == 0) return 0;   // theoretically impossible to have 0 pieces
        if (nOpponent == 0) return 1;   // opponent has no pieces left, player wins
        if (nPlayer == 0) return -1;    // player has no pieces left, opponent wins

        return calculateScore(nPlayer, nOpponent);  // calculate score between [-1, 1] based on ratio
    }

    // calculate score between [-1, 1] based on ratio between pieces of player and opponent
    private double calculateScore(int nPlayer, int nOpponent) {
        double res = 0;
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
