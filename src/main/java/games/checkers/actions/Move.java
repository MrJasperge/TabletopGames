package games.checkers.actions;

import core.AbstractGameState;
import core.actions.AbstractAction;
import core.actions.SetGridValueAction;
import core.components.Token;
import core.interfaces.IPrintable;
import games.checkers.CheckersConstants;
import games.checkers.CheckersGameParameters;
import games.checkers.CheckersGameState;
import utilities.Pair;

public class Move extends AbstractAction {

    private final int playerID;
    private final Pair<Integer, Integer> fromCell;
    private final Pair<Integer, Integer> toCell;

    public Move(int playerID, Pair<Integer, Integer> fromCell, Pair<Integer, Integer> toCell) {
        this.playerID = playerID;
        this.fromCell = fromCell;
        this.toCell = toCell;
    }

    @Override
    public boolean execute(AbstractGameState gs) {
//        System.out.println("Execute ");
//        System.out.print("([" + getFromX() + "," + getFromY() + "] to ["
//                + getToX() + "," + getToY() + "])\n");

        CheckersGameState chgs = (CheckersGameState) gs;

        chgs.getGridBoard().setElement(getFromX(), getFromY(), new Token(CheckersConstants.emptyCell));
        chgs.getGridBoard().setElement(getToX(), getToY(),CheckersConstants.playerMapping.get(playerID));
        chgs.getTurnOrder().endPlayerTurn(chgs);

        return true;
    }

    public int getToX() {
        return toCell.a;
    }

    public int getToY() {
        return toCell.b;
    }

    public int getFromX() {
        return fromCell.a;
    }

    public int getFromY() {
        return fromCell.b;
    }

    @Override
    public AbstractAction copy() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Move)) return false;
        Move move = (Move) obj;
        return playerID == move.playerID && fromCell == move.fromCell && toCell == move.toCell;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String getString(AbstractGameState gameState) {
        return "Move";
    }
}
