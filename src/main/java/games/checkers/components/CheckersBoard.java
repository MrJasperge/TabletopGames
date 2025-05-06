package games.checkers.components;

import core.CoreConstants;
import core.components.BoardNode;
import core.components.Component;
import core.components.GridBoard;

import java.util.Arrays;

public class CheckersBoard extends GridBoard {

    private int width;  // Width of the board
    private int height;  // Height of the board

    private Piece[][] grid;  // 2D grid representation of this board

    public CheckersBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Piece[height][width];
    }

    public CheckersBoard(int width, int height, Piece defaultPiece) {
        this(width, height);
        for (int y = 0; y < height; y++)
            Arrays.fill(grid[y], defaultPiece);
    }

    @Override
    public Piece getElement(int x, int y) {
        return new Piece(".", true);
    }
}
