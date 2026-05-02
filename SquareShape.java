import java.awt.*;

/**
 * SquareShape.java
 * ─────────────────────────────────────────────────────────────────
 * Represents a Square (all four sides equal).
 * ─────────────────────────────────────────────────────────────────
 */
public class SquareShape extends Shape {

    private double side; // Length of one side

    public SquareShape(double side) {
        super("Square");
        this.side = side;
    }

    public double getSide() {
        return side;
    }

    /**
     * draw() – Draws a filled square centred in the panel.
     * Uses fillRect() for the fill and drawRect() for the border.
     */
    @Override
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 50;
        int size    = Math.min(width, height) - padding * 2;
        if (size < 10) size = 10;

        int drawX = x + (width  - size) / 2;
        int drawY = y + (height - size) / 2;

        // ── Fill ──────────────────────────────────────────────
        g2d.setColor(new Color(144, 238, 144, 180)); // semi-transparent light green
        g2d.fillRect(drawX, drawY, size, size);

        // ── Border ────────────────────────────────────────────
        g2d.setColor(new Color(34, 139, 34)); // forest green
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawRect(drawX, drawY, size, size);

        // ── Right-angle symbol at top-left corner ─────────────
        int sq = 12;
        g2d.setColor(new Color(34, 139, 34));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawLine(drawX + sq, drawY,      drawX + sq, drawY + sq);
        g2d.drawLine(drawX,      drawY + sq, drawX + sq, drawY + sq);

        // ── Side label (top) ──────────────────────────────────
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.setColor(new Color(0, 100, 0));
        String sideLabel = "s = " + side;
        FontMetrics fm = g2d.getFontMetrics();
        int labelW = fm.stringWidth(sideLabel);
        g2d.drawString(sideLabel, drawX + (size - labelW) / 2, drawY - 8);

        // ── Tick marks on all four sides ──────────────────────
        g2d.setColor(new Color(34, 139, 34));
        int mid = drawX + size / 2;
        int midY = drawY + size / 2;
        // top / bottom
        g2d.drawLine(mid - 5, drawY + 4, mid + 5, drawY + 4);
        g2d.drawLine(mid - 5, drawY + size - 4, mid + 5, drawY + size - 4);
        // left / right
        g2d.drawLine(drawX + 4, midY - 5, drawX + 4, midY + 5);
        g2d.drawLine(drawX + size - 4, midY - 5, drawX + size - 4, midY + 5);

        // ── Formula labels ────────────────────────────────────
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(String.format("Area = s² = %.2f",      getArea()),
                       x + 10, y + height - 28);
        g2d.drawString(String.format("Perimeter = 4s = %.2f", getPerimeter()),
                       x + 10, y + height - 10);
    }

    /** Area = side² */
    @Override
    public double getArea() {
        return side * side;
    }

    /** Perimeter = 4 × side */
    @Override
    public double getPerimeter() {
        return 4 * side;
    }

    @Override
    public String getDimensionsString() {
        return "side=" + side;
    }
}
