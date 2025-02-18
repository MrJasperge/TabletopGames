package utilities.plotting;

import javax.swing.*;
import java.awt.*;

public class DotPlot extends JComponent {
    double[] data;
    int minY, maxY;
    int xTickWidth;

    final Dimension size;
    int padding = 2;
    int maxWidth = 300, maxHeight = 20, dotRadius = maxHeight/2;
    Color dotColor = new Color(22, 230, 250, 40);
    Color outlineColor = Color.white;

    public DotPlot(double[] data, int minY, int maxY) {
        this.data = data;
        this.minY = minY;
        this.maxY = maxY;
        xTickWidth = Math.max(maxWidth / maxY, 1);
        size = new Dimension(maxWidth + padding*3, maxHeight+padding);
    }
    public DotPlot(Double[] data, int minY, int maxY) {
        this.data = new double[data.length];
        for (int i = 0 ; i < data.length; i++) {
            this.data[i] = data[i];
        }
        this.minY = minY;
        this.maxY = maxY;
        xTickWidth = Math.max(maxWidth / maxY, 1);
        size = new Dimension(maxWidth + padding*3, maxHeight+padding);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Draw data
        int yPos = maxHeight/2 - dotRadius/2;
        for (int i = 0; i < data.length; i++) {
            // Draw dots
            g2.setColor(dotColor);
            int xPos = padding + (int)data[i] * xTickWidth + xTickWidth/2 - dotRadius/2;
            g2.fillOval(xPos, yPos, dotRadius, dotRadius);
        }

        // Draw outline
        g2.setColor(outlineColor);
        g2.drawRect(0, 0, maxWidth + padding*2, maxHeight);

    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setDotRadius(int dotRadius) {
        this.dotRadius = dotRadius;
    }

    public void setDotColor(Color dotColor) {
        this.dotColor = dotColor;
    }

    public void setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
    }

    @Override
    public Dimension getPreferredSize() {
        return size;
    }
}
