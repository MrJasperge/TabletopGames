package games.checkers.actions;

import core.AbstractGameState;
import core.actions.AbstractAction;
import games.checkers.CheckersConstants;
import games.checkers.CheckersGameState;
import games.checkers.components.Piece;
import utilities.Pair;

import java.util.ArrayList;

public class Capture extends AbstractAction {

    private final int playerID;
    private final Pair<Integer, Integer> fromCell;
    private final Pair<Integer, Integer> toCell;
    private final ArrayList<Pair<Integer, Integer>> capturedCells;
    private boolean endOfTurn;

    public Capture (int playerID, Pair<Integer, Integer> fromCell, Pair<Integer, Integer> toCell, ArrayList<Pair<Integer, Integer>> capturedCells, boolean endOfTurn) {
        this.playerID = playerID;
        this.fromCell = fromCell;
        this.toCell = toCell;
        this.capturedCells = capturedCells;
        this.endOfTurn = endOfTurn;
    }
    public Capture (int playerID, Pair<Integer, Integer> fromCell, Pair<Integer, Integer> toCell, Pair<Integer, Integer> capturedCell, boolean endOfTurn) {
        this.playerID = playerID;
        this.fromCell = fromCell;
        this.toCell = toCell;
        this.capturedCells = new ArrayList<>();
        this.capturedCells.add(capturedCell);
        this.endOfTurn = endOfTurn;
    }
    @Override
    public boolean execute(AbstractGameState gs) {
        CheckersGameState chgs = (CheckersGameState) gs;
        boolean isKing = chgs.getGridBoard().getElement(getFromX(), getFromY()).isKing();
        if (((playerID == 0) && (getToY() == (chgs.getHeight() - 1))) || ((playerID == 1) && (getToY() == 0))) {
            isKing = true;
        }
        chgs.getGridBoard().setElement(getFromX(), getFromY(), new Piece(CheckersConstants.emptyCell));
        for (Pair<Integer, Integer> cell : capturedCells) {
            chgs.getGridBoard().setElement(cell.a, cell.b, new Piece(CheckersConstants.emptyCell));
        }
        chgs.getGridBoard().setElement(getToX(), getToY(), new Piece(CheckersConstants.playerMapping.get(playerID).toString(),isKing));

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

    public Pair<Integer, Integer> getFromCell() {
        return fromCell;
    }
    public Pair<Integer, Integer> getToCell() {
        return toCell;
    }
    public void setEndOfTurn(boolean end) {
        this.endOfTurn = end;
    }

    // TODO: return list of captured cells
    public ArrayList<Pair<Integer, Integer>> getCapturedCells() {
        return capturedCells;
    }

    public int getCapturedX() {
        return 0;
    }

    public int getCapturedY() {
        return 0;
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
        return playerID == capt.playerID && fromCell == capt.fromCell && toCell == capt.toCell && capturedCells == capt.capturedCells;
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
