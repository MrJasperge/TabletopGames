package games.checkers.actions;

import core.AbstractGameState;
import core.actions.AbstractAction;
import games.checkers.CheckersConstants;
import games.checkers.CheckersGameState;
import games.checkers.components.Piece;
import utilities.Pair;

import java.util.ArrayList;
import java.util.Objects;

public class Capture extends AbstractAction {

    private final int playerID;
    private final Pair<Integer, Integer> fromCell;
    private final Pair<Integer, Integer> toCell;

    public Capture (int playerID, Pair<Integer, Integer> fromCell, Pair<Integer, Integer> toCell) {
        this.playerID = playerID;
        this.fromCell = fromCell;
        this.toCell = toCell;
    }

    @Override
    public boolean execute(AbstractGameState gs) {
        CheckersGameState chgs = (CheckersGameState) gs;
        boolean isKing = ((Piece)chgs.getGridBoard().getElement(getFromX(), getFromY())).isKing();
        if (((playerID == 0) && (getToY() == (chgs.getGridBoard().getHeight() - 1))) || ((playerID == 1) && (getToY() == 0))) {
            isKing = true;
        }
        chgs.getGridBoard().setElement(getFromX(), getFromY(), new Piece(CheckersConstants.emptyCell));

        // Capture the piece in between
        Pair<Integer, Integer> cell = new Pair<>((getFromX() + getToX()) / 2, (getFromY() + getToY()) / 2);
        chgs.getGridBoard().setElement(cell.a, cell.b, new Piece(CheckersConstants.emptyCell));

        // capture all pieces in between
        ArrayList<Pair<Integer, Integer>> cellsBetween = getCellsBetween(fromCell, toCell);
        for (Pair<Integer, Integer> c : cellsBetween) {
            chgs.getGridBoard().setElement(c.a, c.b, new Piece(CheckersConstants.emptyCell));
        }


        chgs.getGridBoard().setElement(getToX(), getToY(), new Piece(CheckersConstants.playerMapping.get(playerID).toString(),isKing));

        return true;
    }

    // get cells between two cells
    private ArrayList<Pair<Integer, Integer>> getCellsBetween(Pair<Integer, Integer> from, Pair<Integer, Integer> to) {
        ArrayList<Pair<Integer, Integer>> cells = new ArrayList<>();
        int dx = (to.a - from.a) / Math.max(1, Math.abs(to.a - from.a));
        int dy = (to.b - from.b) / Math.max(1, Math.abs(to.b - from.b));
        int x = from.a + dx;
        int y = from.b + dy;

        while (x != to.a || y != to.b) {
            cells.add(new Pair<>(x, y));
            x += dx;
            y += dy;
        }

        return cells;
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
        return playerID == capt.playerID
                && Objects.equals(fromCell, capt.fromCell)
                && Objects.equals(toCell, capt.toCell);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String getString(AbstractGameState gameState) {
        return "Capture from [" + getFromX() + "," + getFromY() + "] to [" + getToX() + "," + getToY() + "]";
    }
}
