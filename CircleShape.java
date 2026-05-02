import java.awt.*;

/**
 * CircleShape.java
 * ─────────────────────────────────────────────────────────────────
 * Represents a Circle. Stores the radius and knows how to:
 *   - Calculate area and circumference
 *   - Draw itself on a Swing panel
 * ─────────────────────────────────────────────────────────────────
 */
public class CircleShape extends Shape {

    private double radius; // The radius of the circle

    /** Constructor: takes radius, passes "Circle" to parent */
    public CircleShape(double radius) {
        super("Circle");
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    /**
     * draw() – Paints the circle centered in the drawing panel.
     * fillOval() draws a filled ellipse; when width == height it
     * becomes a perfect circle.
     */
    @Override
    public void draw(Graphics2D g2d, int x, int y, int width, int height) {
        // Enable smooth (anti-aliased) rendering
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        // Leave padding so the shape doesn't touch the edges
        int padding = 40;
        int diameter = Math.min(width, height) - padding * 2;
        if (diameter < 10) diameter = 10; // minimum size guard

        // Center the circle in the panel
        int drawX = x + (width  - diameter) / 2;
        int drawY = y + (height - diameter) / 2;

        // ── Fill ──────────────────────────────────────────────
        g2d.setColor(new Color(100, 149, 237, 180)); // semi-transparent cornflower blue
        g2d.fillOval(drawX, drawY, diameter, diameter);

        // ── Border ────────────────────────────────────────────
        g2d.setColor(new Color(30, 70, 160));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawOval(drawX, drawY, diameter, diameter);

        // ── Centre dot ────────────────────────────────────────
        int cx = drawX + diameter / 2;
        int cy = drawY + diameter / 2;
        g2d.setColor(Color.RED);
        g2d.fillOval(cx - 4, cy - 4, 8, 8);

        // ── Radius line ───────────────────────────────────────
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10, new float[]{6, 4}, 0)); // dashed
        g2d.drawLine(cx, cy, drawX + diameter, cy);

        // ── Label on radius line ──────────────────────────────
        g2d.setStroke(new BasicStroke(1));
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.setColor(new Color(150, 0, 0));
        g2d.drawString("r = " + radius, cx + 8, cy - 6);

        // ── Formula labels at bottom ──────────────────────────
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.DARK_GRAY);
        String areaText = String.format("Area = π·r² = %.2f", getArea());
        String circText = String.format("Circumference = 2πr = %.2f", getPerimeter());
        g2d.drawString(areaText,  x + 10, y + height - 28);
        g2d.drawString(circText,  x + 10, y + height - 10);
    }

    /** Area = π × r² */
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }

    /** Circumference = 2 × π × r */
    @Override
    public double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    public String getDimensionsString() {
        return "radius=" + radius;
    }
}
