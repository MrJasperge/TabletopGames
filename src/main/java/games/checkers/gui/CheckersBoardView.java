package games.checkers.gui;

import core.actions.AbstractAction;
import core.components.GridBoard;
import core.components.Token;
import games.checkers.actions.Capture;
import games.checkers.actions.Move;
import games.checkers.components.Piece;
import games.checkers.CheckersForwardModel;
import gui.ScreenHighlight;
import gui.views.ComponentView;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static gui.GUI.defaultItemSize;

public class CheckersBoardView extends ComponentView implements ScreenHighlight {

    Rectangle[] rects;
    ArrayList<Rectangle> highlight;
    ArrayList<Rectangle> moveHighlight;
    ArrayList<Rectangle> captureHighlight;
    List<AbstractAction> actions;


    public CheckersBoardView(GridBoard<Piece> gridBoard) {
        super(gridBoard, gridBoard.getWidth() * defaultItemSize, gridBoard.getHeight() * defaultItemSize);
        rects = new Rectangle[gridBoard.getWidth() * gridBoard.getHeight()];
        highlight = new ArrayList<>();
        moveHighlight = new ArrayList<>();
        captureHighlight = new ArrayList<>();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    // Left click, highlight cell
                    for (Rectangle r : rects) {
                        if (r != null && r.contains(e.getPoint())) {
                            highlight.clear();
                            highlight.add(r);
                            break;
                        }
                    }
                } else {
                    // Remove highlight
                    highlight.clear();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        drawGridBoard((Graphics2D)g, (GridBoard<Piece>) component, 0, 0);

        if (highlight.size() > 0) {
            g.setColor(Color.green);
            Stroke s = ((Graphics2D) g).getStroke();
            ((Graphics2D) g).setStroke(new BasicStroke(3));

            Rectangle r = highlight.get(0);
            g.drawRect(r.x, r.y, r.width, r.height);
            ((Graphics2D) g).setStroke(s);
        }

        if (moveHighlight.size() > 0) {
            g.setColor(Color.yellow);
            Stroke s = ((Graphics2D) g).getStroke();
            ((Graphics2D) g).setStroke(new BasicStroke(3));

            for (Rectangle r : moveHighlight) {
                g.drawRect(r.x, r.y, r.width, r.height);
                ((Graphics2D) g).setStroke(s);
            }
        }
        if (captureHighlight.size() > 0) {
            g.setColor(Color.red);
            Stroke s = ((Graphics2D) g).getStroke();
            ((Graphics2D) g).setStroke(new BasicStroke(3));

            for (Rectangle r : captureHighlight) {
                g.drawRect(r.x, r.y, r.width, r.height);
                ((Graphics2D) g).setStroke(s);
            }
        }
    }

    public void drawGridBoard(Graphics2D g, GridBoard<Piece> gridBoard, int x, int y) {
        int width = gridBoard.getWidth() * defaultItemSize;
        int height = gridBoard.getHeight() * defaultItemSize;

        // Draw background
        g.setColor(Color.lightGray);
        g.fillRect(x, y, width-1, height-1);
        g.setColor(Color.black);

        // Draw cells
        for (int i = 0; i < gridBoard.getHeight(); i++) {
            for (int j = 0; j < gridBoard.getWidth(); j++) {
                int xC = x + j * defaultItemSize;
                int yC = y + i * defaultItemSize;
                drawCell(g, gridBoard.getElement(j, i), xC, yC);

                // Save rect where cell is drawn
                int idx = i * gridBoard.getWidth() + j;
                if (rects[idx] == null) {
                    rects[idx] = new Rectangle(xC, yC, defaultItemSize, defaultItemSize);
                }
            }
        }
    }

    private void drawCell(Graphics2D g, Token element, int x, int y) {
        // Paint cell background
        g.setColor(Color.lightGray);
        g.fillRect(x, y, defaultItemSize, defaultItemSize);
        g.setColor(Color.black);
        g.drawRect(x, y, defaultItemSize, defaultItemSize);

        // Paint element in cell
        if (element != null) {
            Font f = g.getFont();
            g.setFont(new Font(f.getName(), Font.BOLD, defaultItemSize * 3 / 2));
            g.drawString(element.toString(), x + defaultItemSize / 16, y + defaultItemSize - defaultItemSize / 16);
            g.setFont(f);
        }
    }

    public void setActions(List<AbstractAction> a) {
        actions = a;
        moveHighlight.clear();
        captureHighlight.clear();
        for (AbstractAction abAction : actions) {
            if (abAction instanceof Move) {
                Move action = (Move) abAction;
                Rectangle r = new Rectangle();
                r.x = action.getToX() * defaultItemSize;
                r.y = action.getToY() * defaultItemSize;
                r.height = defaultItemSize; r.width = defaultItemSize;

                moveHighlight.add(r);
            }
            if (abAction instanceof Capture) {
                Capture action = (Capture) abAction;
                Rectangle r = new Rectangle();
                r.x = action.getToX() * defaultItemSize;
                r.y = action.getToY() * defaultItemSize;
                r.height = defaultItemSize; r.width = defaultItemSize;

                captureHighlight.add(r);
            }
        }
    }

    public ArrayList<Rectangle> getHighlight() {
        return highlight;
    }
    public ArrayList<Rectangle> getMoveHighlight() {
        return moveHighlight;
    }

    @Override
    public void clearHighlights() {
        highlight.clear();
    }
}
