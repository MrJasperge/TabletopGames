package games.checkers.components;

import core.CoreConstants;
import core.components.BoardNode;
import core.components.Component;
import core.components.GridBoard;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public Piece getElement(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height)
            return grid[y][x];
        return null;
    }
//    @Override
//    public CheckersBoard copy() {
//        Piece[][] gridCopy = new Piece[getHeight()][getWidth()];
//        Map<Integer, Piece> nodeCopies = new HashMap<>();
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                if (grid[i][j] != null) {
//                    gridCopy[i][j] = new Piece(grid[i][j]);
//                    nodeCopies.put(gridCopy[i][j].componentID, gridCopy[i][j]);
//                }
//            }
//        }
//        for (int i = 0; i < height; i++) {
//            for (int j = 0; j < width; j++) {
//                if (grid[i][j] != null) {
//                    for (Map.Entry<Piece, Double> neighbour : grid[i][j].getNeighbours().entrySet()) {
//                        gridCopy[i][j].addNeighbourWithCost(nodeCopies.get(neighbour.getKey().componentID), neighbour.getValue());
//                    }
//                    for (Map.Entry<Piece, Integer> neighbour : grid[i][j].getNeighbourSideMapping().entrySet()) {
//                        gridCopy[i][j].addNeighbourOnSide(nodeCopies.get(neighbour.getKey().componentID), neighbour.getValue());
//                    }
//                }
//            }
//        }
//        GridBoard g = new GridBoard(gridCopy, componentID);
//        copyComponentTo(g);
//        return g;
//    }

    public boolean setElement(int x, int y, Piece value) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            grid[y][x] = value;
            return true;
        } else
            return false;
    }
}
