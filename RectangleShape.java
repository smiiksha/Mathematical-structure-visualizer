import java.awt.*;

/**
 * RectangleShape.java
 * ─────────────────────────────────────────────────────────────────
 * Represents a Rectangle with a distinct length and width.
 * Note: We named this class "RectangleShape" to avoid a name
 * clash with java.awt.Rectangle which is a built-in Java class.
 * ─────────────────────────────────────────────────────────────────
 */
public class RectangleShape extends Shape {

    private double length; // The longer dimension
    private double rectWidth;  // The shorter dimension (named rectWidth to avoid conflict)

    public RectangleShape(double length, double width) {
        super("Rectangle");
        this.length    = length;
        this.rectWidth = width;
    }

    public double getLength()    { return length;    }
    public double getRectWidth() { return rectWidth; }

    /**
     * draw() – Draws the rectangle scaled to fit the panel
     * while preserving the length:width aspect ratio.
     */
    @Override
    public void draw(Graphics2D g2d, int px, int py, int pWidth, int pHeight) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 50;
        int maxW    = pWidth  - padding * 2;
        int maxH    = pHeight - padding * 2;

        // ── Scale to fit while keeping aspect ratio ────────────
        double ratio = length / rectWidth;
        int rectW, rectH;
        if (ratio >= 1) {
            // Landscape
            rectW = maxW;
            rectH = (int)(maxW / ratio);
            if (rectH > maxH) { rectH = maxH; rectW = (int)(maxH * ratio); }
        } else {
            // Portrait
            rectH = maxH;
            rectW = (int)(maxH * ratio);
            if (rectW > maxW) { rectW = maxW; rectH = (int)(maxW / ratio); }
        }
        if (rectW < 10) rectW = 10;
        if (rectH < 10) rectH = 10;

        int drawX = px + (pWidth  - rectW) / 2;
        int drawY = py + (pHeight - rectH) / 2;

        // ── Fill ──────────────────────────────────────────────
        g2d.setColor(new Color(255, 182, 193, 180)); // semi-transparent light pink
        g2d.fillRect(drawX, drawY, rectW, rectH);

        // ── Border ────────────────────────────────────────────
        g2d.setColor(new Color(180, 20, 80));
        g2d.setStroke(new BasicStroke(2.5f));
        g2d.drawRect(drawX, drawY, rectW, rectH);

        // ── Dimension arrows ──────────────────────────────────
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.setColor(new Color(120, 0, 60));
        FontMetrics fm = g2d.getFontMetrics();

        // Length label centred on top edge
        String lenLabel = "l = " + length;
        int lw = fm.stringWidth(lenLabel);
        g2d.drawString(lenLabel, drawX + (rectW - lw) / 2, drawY - 8);

        // Width label centred on right edge (rotated)
        Graphics2D g2dCopy = (Graphics2D) g2d.create();
        String wLabel = "w = " + rectWidth;
        g2dCopy.rotate(-Math.PI / 2, drawX + rectW + 20, drawY + rectH / 2);
        g2dCopy.setFont(new Font("Arial", Font.BOLD, 13));
        g2dCopy.setColor(new Color(120, 0, 60));
        g2dCopy.drawString(wLabel, drawX + rectW + 20 - fm.stringWidth(wLabel) / 2,
                           drawY + rectH / 2 + 5);
        g2dCopy.dispose();

        // ── Diagonal to show it's not a square ────────────────
        g2d.setColor(new Color(200, 150, 150, 100));
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10, new float[]{4, 4}, 0));
        g2d.drawLine(drawX, drawY, drawX + rectW, drawY + rectH);

        // ── Formula labels ────────────────────────────────────
        g2d.setStroke(new BasicStroke(1));
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.setColor(Color.DARK_GRAY);
        g2d.drawString(String.format("Area = l × w = %.2f",          getArea()),
                       px + 10, py + pHeight - 28);
        g2d.drawString(String.format("Perimeter = 2(l+w) = %.2f",    getPerimeter()),
                       px + 10, py + pHeight - 10);
    }

    /** Area = length × width */
    @Override
    public double getArea() {
        return length * rectWidth;
    }

    /** Perimeter = 2 × (length + width) */
    @Override
    public double getPerimeter() {
        return 2 * (length + rectWidth);
    }

    @Override
    public String getDimensionsString() {
        return "length=" + length + ", width=" + rectWidth;
    }
}
