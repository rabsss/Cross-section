import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

class Coordinate {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

class CrossSectionPanel extends JPanel {
    private List<Coordinate> coordinates;

    public CrossSectionPanel(List<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int margin = 40;
        int width = getWidth() - 2 * margin;
        int height = getHeight() - 2 * margin;

        g.setColor(Color.BLACK);

        // Draw X-axis
        g.drawLine(margin, margin + height / 2, margin + width, margin + height / 2);
        g.drawString("Distance", getWidth() - margin, margin + height / 2 + 15);

        // Calculate the range of x and y values
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxY = Integer.MIN_VALUE;
        for (Coordinate coordinate : coordinates) {
            minX = Math.min(minX, coordinate.getX());
            maxX = Math.max(maxX, coordinate.getX());
            minY = Math.min(minY, coordinate.getY());
            maxY = Math.max(maxY, coordinate.getY());
        }

        // Calculate the intervals for x and y divisions
        int xInterval = calculateInterval(minX, maxX);
        int yInterval = calculateInterval(minY, maxY);

        // Draw X-axis divisions
        for (int x = minX + xInterval; x < maxX; x += xInterval) {
            int xPixel = margin + (x - minX) * width / (maxX - minX);
            g.drawLine(xPixel, margin + height / 2 - 5, xPixel, margin + height / 2 + 5);
        }

        // Draw Y-axis
        g.drawLine(margin, margin, margin, margin + height);
        g.drawString("Elevation", margin - 45, margin - 10);

        // Draw Y-axis divisions
        for (int y = minY + yInterval; y < maxY; y += yInterval) {
            int yPixel = margin + height - (y - minY) * height / (maxY - minY);
            g.drawLine(margin - 5, yPixel, margin + 5, yPixel);
        }

        g.setColor(Color.RED);
        for (int i = 0; i < coordinates.size() - 1; i++) {
            Coordinate current = coordinates.get(i);
            Coordinate next = coordinates.get(i + 1);

            int x1 = margin + (current.getX() - minX) * width / (maxX - minX);
            int y1 = margin + height - (current.getY() - minY) * height / (maxY - minY);
            int x2 = margin + (next.getX() - minX) * width / (maxX - minX);
            int y2 = margin + height - (next.getY() - minY) * height / (maxY - minY);

            g.drawLine(x1, y1, x2, y2);
        }
    }

    private int calculateInterval(int min, int max) {
        int range = max - min;
        int log10 = (int) Math.log10(range);
        int pow10 = (int) Math.pow(10, log10);
        int interval = pow10 / 2;

        if (range <= 2 * interval) {
            return interval / 5;
        } else if (range <= 4 * interval) {
            return interval / 2;
        } else {
            return interval;
        }
    }
}

public class CrossSectionGraph {
    public static void main(String[] args) {
        int numPoints = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of points:"));

        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < numPoints; i++) {
            int x = Integer.parseInt(JOptionPane.showInputDialog("Enter the distance for point " + (i + 1) + ":"));
            int y = Integer.parseInt(JOptionPane.showInputDialog("Enter the elevation for point " + (i + 1) + ":"));
            coordinates.add(new Coordinate(x, y));
        }

        JFrame frame = new JFrame("Cross Section Graph");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        CrossSectionPanel panel = new CrossSectionPanel(coordinates);
        frame.add(panel);

        frame.setVisible(true);
    }
}
