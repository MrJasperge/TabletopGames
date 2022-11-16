package games.checkers.actions;

import core.AbstractGameState;
import core.actions.AbstractAction;
import games.checkers.CheckersConstants;
import games.checkers.CheckersGameState;
import games.checkers.components.Piece;
import utilities.Pair;

public class Capture extends AbstractAction {

    private final int playerID;
    private final Pair<Integer, Integer> fromCell;
    private final Pair<Integer, Integer> toCell;
    private final Pair<Integer, Integer> capturedCell;
    private final boolean endOfTurn;

    public Capture (int playerID, Pair<Integer, Integer> fromCell, Pair<Integer, Integer> toCell, Pair<Integer, Integer> capturedCell, boolean endOfTurn) {
        this.playerID = playerID;
        this.fromCell = fromCell;
        this.toCell = toCell;
        this.capturedCell = capturedCell;
        this.endOfTurn = endOfTurn;
    }
    @Override
    public boolean execute(AbstractGameState gs) {
        CheckersGameState chgs = (CheckersGameState) gs;
        chgs.getGridBoard().setElement(getFromX(), getFromY(), new Piece(CheckersConstants.emptyCell));
        chgs.getGridBoard().setElement(getCapturedX(), getCapturedY(), new Piece(CheckersConstants.emptyCell));
        chgs.getGridBoard().setElement(getToX(), getToY(), CheckersConstants.playerMapping.get(playerID));

        if (endOfTurn)  chgs.getTurnOrder().endPlayerTurn(chgs);
        return true;
    }

    public int getFromX() {
        return fromCell.a;
    }

    public int getFromY() {
        return fromCell.b;
    }

    public int getToX() {
        return toCell.a;
    }

    public int getToY() {
        return toCell.b;
    }

    public int getCapturedX() {
        return capturedCell.a;
    }

    public int getCapturedY() {
        return capturedCell.b;
    }

    @Override
    public AbstractAction copy() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Capture)) return false;
        Capture capt = (Capture) obj;
        return playerID == capt.playerID && fromCell == capt.fromCell && toCell == capt.toCell && capturedCell == capt.capturedCell;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String getString(AbstractGameState gameState) {
        return "Capture";
    }
}
