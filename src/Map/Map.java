package Map;

import Settings.EParam;
import Settings.Settings;

import javax.swing.*;
import java.awt.*;

public class Map extends JPanel {

    private int[][] mapLayout;
    private int cellSize;
    private int xOffset;
    private int yOffset;
    private final int margin = 20; // Margin around the maze

    public Map() {
        this.mapLayout = MapData.initialMap;
        setOpaque(false);
    }
    
    public void calculateDimensions(int panelWidth, int panelHeight) {
        int numRows = mapLayout.length;
        int numCols = mapLayout[0].length;
        
        // Calculate dimensions for the maze area inside the margin
        int availableWidth = panelWidth - (2 * margin);
        int availableHeight = panelHeight - (2 * margin);
        
        int cellWidth = availableWidth / numCols;
        int cellHeight = availableHeight / numRows;
        cellSize = Math.min(cellWidth, cellHeight);

        int mapWidth = cellSize * numCols;
        int mapHeight = cellSize * numRows;

        // Center the maze area within the panel
        xOffset = (panelWidth - mapWidth) / 2;
        yOffset = (panelHeight - mapHeight) / 2;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        
        // Draw the black background for the maze area only
        g2d.setColor(Color.BLACK);
        g2d.fillRect(xOffset, yOffset, mapLayout[0].length * cellSize, mapLayout.length * cellSize);
        
        // Draw the walls in a dark blue
        g2d.setColor(new Color(0, 0, 139)); // Dark Blue

        for (int row = 0; row < mapLayout.length; row++) {
            for (int col = 0; col < mapLayout[0].length; col++) {
                if (mapLayout[row][col] == 1) {
                    g2d.fillRect(xOffset + col * cellSize, yOffset + row * cellSize, cellSize, cellSize);
                }
            }
        }
    }

    public int[][] getMapLayout() {
        return mapLayout;
    }

    public int getCellSize() {
        return cellSize;
    }

    public boolean isWall(int row, int col) {
        if (row < 0 || row >= mapLayout.length || col < 0 || col >= mapLayout[0].length) {
            return true; // Treat out-of-bounds as a wall
        }
        return mapLayout[row][col] == 1;
    }

    public Point getCellCoordinates(Point pixelPoint) {
        if (cellSize == 0) return null;
        int col = (pixelPoint.x - xOffset) / cellSize;
        int row = (pixelPoint.y - yOffset) / cellSize;
        return new Point(col, row);
    }

    public Point getPixelCoordinates(int row, int col) {
        int x = xOffset + col * cellSize + cellSize / 2;
        int y = yOffset + row * cellSize + cellSize / 2;
        return new Point(x, y);
    }
}
